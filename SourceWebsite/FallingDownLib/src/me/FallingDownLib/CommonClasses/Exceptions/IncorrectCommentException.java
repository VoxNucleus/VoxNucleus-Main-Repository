package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class IncorrectCommentException extends Exception {

    String cause;

    public IncorrectCommentException(String reason){
        super(reason);
        cause=reason;

    }

    @Override
    public String toString(){
        return super.toString();
    }


    public String giveReason(){
        return cause;
    }

}
