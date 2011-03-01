/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.search.exception;

/**
 * Thrown when a search is misformed
 * @author victork
 */
public class InvalidSearchRequestException extends Exception{
    String reason;

    public InvalidSearchRequestException(String in) {
        super(in);
        reason = in;
    }

    public String getReason() {
        return reason;
    }

}
