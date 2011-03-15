/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class IncorrectUserInfo extends Exception {

    String reason;

    public IncorrectUserInfo(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return reason;
    }
}
