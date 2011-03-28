package FallingDown.redirect.exception;

/**
 * <code> NoLinkAssociated </code> is thrown when the Post has no link associated.
 * Thus there can be no redirection.
 * @author victork
 */
public class NoLinkAssociated extends Exception {
    String reason;


    /**
     * Constructor
     * @param in
     */
    public NoLinkAssociated(String in){
        this.reason=in;
    }

    @Override
    public String toString(){
        return reason;
    }

}
