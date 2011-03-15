/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class PostAlreadyExist extends Exception{
    String reason;

    public PostAlreadyExist(String in){
        this.reason=in;
    }


}
