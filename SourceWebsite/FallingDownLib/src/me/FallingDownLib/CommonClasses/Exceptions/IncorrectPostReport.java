package me.FallingDownLib.CommonClasses.Exceptions;

/**
 *
 * @author victork
 */
public class IncorrectPostReport extends Exception {

    private String reason;

    public IncorrectPostReport(String in) {
        reason = in;
    }

    @Override
    public String toString() {
        return reason;
    }

    public String giveReason() {
        return reason;
    }
}
