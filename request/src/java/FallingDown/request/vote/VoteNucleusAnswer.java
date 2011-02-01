package FallingDown.request.vote;

/**
 *
 * @author victork
 */
public class VoteNucleusAnswer {

    private boolean is_vote_success=false;
    private int error_code;
    private String additional_comment;



    /**
     * Should be used ONLY in case of
     * @param is_success
     * @return
     */
    public static VoteNucleusAnswer getInstance(boolean is_success){
        return new VoteNucleusAnswer(is_success);
    }

    public static VoteNucleusAnswer getInstance(boolean is_success,int err_code,String reason){
        return new VoteNucleusAnswer(is_success,err_code,reason);
    }

    /**
     *
     * @param is_success
     */
    private VoteNucleusAnswer(boolean is_success) {
        is_vote_success=is_success;
    }

    /**
     *Is used mostly in false case.
     * @param is_success
     * @param error
     * @param comment
     */
    private VoteNucleusAnswer(boolean is_success, int error, String comment){
        is_vote_success=is_success;
        error_code=error;
        additional_comment=comment;
    }

}
