/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 * Thrown when a User isn't in the database
 * @author victork
 */
public class UserDoesNotExist extends Exception{

    String reason;

    public UserDoesNotExist(String in){
        this.reason=in;
        
    }

    @Override
    public String toString(){
        return reason;
    }

}
