/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class FileToBigException extends Exception{
    String reason;

    public FileToBigException(String in){
        super(in);
        reason=in;
    }

    @Override
    public String toString(){
        return reason;
    }

}
