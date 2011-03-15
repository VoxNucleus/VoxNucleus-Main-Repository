/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 * This exception is thrown when a Post is not correctly done.
 * @author victork
 */
public class IncorrectPostInfo extends Exception {

    String reason;

    public IncorrectPostInfo(String message){
        this.reason=message;
    }

    @Override
    public String toString(){
        return reason;
    }

    public String giveReason(){
        return reason;
    }

}
