/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class IncorrectImage extends Exception {

    String reason;
     public IncorrectImage(String in){
         reason=in;
     }

    @Override
     public String toString(){
         return reason;
     }

}
