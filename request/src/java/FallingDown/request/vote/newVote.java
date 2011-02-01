package FallingDown.request.vote;

import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.connectors.PostConnector;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CassandraConnection.connectors.VoteByPostConnector;
import me.FallingDownLib.CommonClasses.Exceptions.UserHasAlreadyVoted;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.functions.interestingposts.InterestingPostHub;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.voxnucleus.cassandra.connector.VoteByUserConnector;
import me.voxnucleus.nucleus.vote.IncorrectVoteNucleus;
import me.voxnucleus.sql.update.NucleusUPDATEOperations;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *Class which is created for each vote (Need to be synchronized maybe ?)
 * @author victork
 */
public class newVote {

    private byte[] uuid_vote;
    private FallingDownConnector connector;
    private final String post_id;
    private final String user_id;
    private int result_vote;
    private PostHash p_hash;
/**
 *
 * @param username
 * @param postId
 * @param resultVote
 * @throws IncorrectVoteNucleus
 */
    public newVote(String u_id, String postId, int resultVote) throws IncorrectVoteNucleus {
        post_id = postId;
        user_id = u_id;
        result_vote = resultVote;
        uuid_vote = EasyUUIDget.getByteUUID();
        
        try {
            p_hash = new PostHash(Post.getPostFromDatabase(postId));
            if (!has_user_voted()) {
                NucleusUPDATEOperations.updateVote(post_id, result_vote);
                update_vote_number();
                update_vote_by_post();
                insert_in_interesting_nucleus();
                update_user();
                insert_in_list_votes_by_user();
            } else {
                throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ALREADY_VOTED, "L'utilisateur " + u_id + "a déjà voté");
            }
        } catch (IncorrectVoteNucleus ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ALREADY_VOTED, "L'utilisateur " + u_id + "a déjà voté");
        }
    }

    /**
     * 
     * @return true if user has voted
     * @throws IncorrectVoteNucleus
     */
    private boolean has_user_voted() throws IncorrectVoteNucleus {
        try {
            VoteByUserConnector vbu_connector = VoteByUserConnector.getConnector(user_id);
            return vbu_connector.has_user_voted(post_id);
        } catch (Exception ex) {
            throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Une erreur inconnue est survenue");
        }

    }

    /**
     * Insert a new vote in listVotesByUser
     * Key is the username of user
     * Column is the Id of the post, value is the timeUUID
     * @throws IncorrectVoteNucleus
     */
    private void insert_in_list_votes_by_user() throws IncorrectVoteNucleus {
        VoteByUserConnector vbu_connector = VoteByUserConnector.getConnector(user_id);
        try {
            vbu_connector.add_vote(post_id, uuid_vote);
        } catch (Exception ex) {
            throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Erreur interne");
        }
    }

    /**
     * Add one vote to the counter "nbVotes" in User database
     *
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws NotFoundException
     * @throws Exception
     */
    private void update_user() throws Exception {
        UserConnector u_connector = UserConnector.getInstance(user_id);
        u_connector.increment_vote_count();
    }

    /**
     * In case of error, the user will see his vote deleted
     * TODO : finish that
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */
    private void deleteInListVotesByUser() throws Exception {
        //TODO
    }

    /**
     * Add vote to the interesting list
     */
    private void insert_in_interesting_nucleus() {
        InterestingPostHub interestingHub = InterestingPostHub.getInstance();
        interestingHub.addVote(p_hash.getCategory(), p_hash.getSubcategory(), post_id);
    }

    /**
     * 
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */
    private void update_vote_number() throws Exception {
        PostConnector p_connector = PostConnector.getInstance(post_id);
        p_connector.add_to_votes(result_vote);
    }

    /**
     * 
     * @throws Exception
     */
    private void update_vote_by_post() throws Exception {
        VoteByPostConnector v_post_connector = VoteByPostConnector.getConnector(post_id);
        v_post_connector.insertVote(user_id, uuid_vote);
    }
}
