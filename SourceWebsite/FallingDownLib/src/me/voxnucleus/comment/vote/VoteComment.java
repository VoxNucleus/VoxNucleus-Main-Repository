package me.voxnucleus.comment.vote;

/**
 *
 * @author victork
 */
public class VoteComment {

    private String pId;
    private String UUID_string;
    private int vote_int;


    protected VoteComment(String postId, String UUID_as_string, int vote_result){
        postId=pId;
        UUID_string=UUID_as_string;
        vote_int=vote_result;
    }

    public static VoteComment getVoteInstance(String postId, String UUID_as_string, int vote_result){
        return new VoteComment(postId, UUID_as_string,vote_result);
    }

    /**
     * When fired the vote is processed
     */
    public void vote(){
        checkVoteInformation();
        updateCommentScore();
        updateReputation();
    }

    private void checkVoteInformation() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateCommentScore() {
        
    }

    private void updateReputation() {
        updateReputationCassandra();
        updateReputationSQL();
    }

    private void updateReputationCassandra() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateReputationSQL() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
