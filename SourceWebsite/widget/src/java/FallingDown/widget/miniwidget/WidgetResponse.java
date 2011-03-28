package FallingDown.widget.miniwidget;

/**
 *
 * @author victork
 */
public class WidgetResponse {

    private boolean isInDatabase=false;
    private int nbVotes=0;
    private String postId="test";

    public WidgetResponse(boolean inDatabase, int nVotes, String post_key){
        postId=post_key;
        nbVotes=nVotes;
        isInDatabase=inDatabase;
    }

}
