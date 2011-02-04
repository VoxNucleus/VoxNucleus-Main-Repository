package FallingDown.request.vote.comment;

import java.util.UUID;
import me.FallingDownLib.CassandraConnection.connectors.CommentConnector;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.voxnucleus.cassandra.connector.ListVotesByCommentConnector;
import me.voxnucleus.comment.vote.IncorrectVoteComment;
import me.voxnucleus.sql.update.UserUPDATEOperations;

/**
 * Class that actually do the vote
 * @author victork
 */
public class DoVoteComment {

    private String post_id;
    private String comment_uuid;
    private byte[] comment_uuid_as_byte;
    private Pass user_pass;
    private int to_add;

    /**
     * 
     * @param p_id
     * @param uuid
     * @param add
     * @param u_pass
     */

    protected DoVoteComment(String p_id, String uuid,int add, Pass u_pass){
        post_id = p_id;
        comment_uuid = uuid;
        comment_uuid_as_byte = EasyUUIDget.asByteArray(UUID.fromString(comment_uuid));
        to_add=add;
        user_pass=u_pass;
    }

    /**
     * 
     * @param p_id
     * @param uuid
     * @param add
     * @param u_pass
     * @return
     */

    public static DoVoteComment getInstance(String p_id,String uuid,int add, Pass u_pass){
        return new DoVoteComment(p_id, uuid,add, u_pass);
    }

    /**
     * 
     * @throws IncorrectVoteComment
     */

    public void doVote() throws IncorrectVoteComment{
        if (user_has_voted()) {
            throw new IncorrectVoteComment("Vous avez déjà voté pour ce commentaire",IncorrectVoteComment.ALREADY_VOTED);
            //TODO sent required message
        }else{
            update_comment_score();
            insert_user_vote();
            update_user_reputation();
        }
    }

    /**
     * Update the comment score
     */
    private void update_comment_score() throws IncorrectVoteComment {
        CommentConnector c_connector=CommentConnector.getInstance(post_id, comment_uuid_as_byte);
        try{
            c_connector.update_score(to_add);
        }catch(Exception ex){
            throw new IncorrectVoteComment("Erreur interne",IncorrectVoteComment.ERROR_UNKNOWN);
        }

    }

    /**
     * 
     * @throws IncorrectVoteComment
     */

    private void update_user_reputation() throws IncorrectVoteComment {
        UserConnector u_connector = UserConnector.getInstance(user_pass.getUsername());
        try {
            u_connector.update_reputation(to_add);
            UserUPDATEOperations.updateReputation(post_id, to_add);
        } catch (Exception ex) {
            throw new IncorrectVoteComment("Erreur interne",IncorrectVoteComment.ERROR_UNKNOWN);
        }
    }

    /**
     * 
     * @return true if the user has voted
     */

    private boolean user_has_voted() {
        ListVotesByCommentConnector comment_connector=
                ListVotesByCommentConnector.getInstance(user_pass.getUsername(),comment_uuid);
        return comment_connector.has_voted();
        //check if user has already voted for this.
    }

    /**
     *Insert a vote in the user who voted field
     */
    private void insert_user_vote() {
        ListVotesByCommentConnector comment_connector=
                ListVotesByCommentConnector.getInstance(user_pass.getUsername(),comment_uuid);
        comment_connector.insert_vote("1");
        
    }



}
