package me.voxnucleus.nucleus.vote;

/**
 *
 * @author victork
 */
public class IncorrectVoteNucleus extends Exception {

    public static final int ERROR_UNKNOWN=0;
    public static final int ERROR_NOT_CONNECTED=1;
    public static final int DOES_NOT_EXIST=2;
    public static final int ALREADY_VOTED=3;

    private String reason;
    private int error_code;

    public IncorrectVoteNucleus(int err_code, String in_reason){
        reason=in_reason;
        error_code=err_code;
    }

    public String get_reason(){
        return reason;
    }

    public int get_error_code(){
        return error_code;
    }

}
