package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class UserSuspended extends Exception {
    String reason;

    public UserSuspended(String reason){
    this.reason=reason;
    }

    @Override
    public String toString(){
        return reason;
    }

}
