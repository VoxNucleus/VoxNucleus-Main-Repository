/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 * Error thrown when a user tries to access a Post that does not exist
 * @author victork
 */
public class PostDoesNotExist extends Exception {

    String reason;

    public PostDoesNotExist(String in){
        this.reason=in;
    }

    @Override
    public String  toString(){
        return reason;
    }

}
