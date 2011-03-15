package me.voxnucleus.comment.vote;

/**
 * Generic error thrown by anything in interaction with
 * @author victork
 */
public class IncorrectVoteComment extends Exception {
    public static final int ERROR_UNKNOWN=0;
    public static final int ERROR_NOT_CONNECTED=1;
    public static final int DOES_NOT_EXIST=2;
    public static final int ALREADY_VOTED=3;

    private String reason;
    private int error_code;

    public IncorrectVoteComment(String in_reason,int err_code){
        reason=in_reason;
        error_code = err_code;
    }

    /**
     * 
     * @return
     */

    public String getReason(){
        return reason;
    }

    /**
     *
     * @return error code
     */
    public int getErrorCode() {
        return error_code;
    }

}
