package me.FallingDownLib.CommonClasses.Exceptions;

/**
 * Thrown when user try to vote for a Post he has already voted for before.
 * @author victork
 */
public class UserHasAlreadyVoted extends Exception {
    String reason;

    public UserHasAlreadyVoted(String reason){
    this.reason=reason;
    }

    @Override
    public String toString(){
        return reason;
    }


}
