package FallingDown.request.vote.popup.elements;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PopupNucleusNotExist implements ToCodeConverter{

    private StringBuilder pop_not_exist;

    protected PopupNucleusNotExist(String postId){
        pop_not_exist = new StringBuilder();
    }

    public String getHTMLCode() {
        buildDoesNotExist();
        return pop_not_exist.toString();
    }

    private void buildDoesNotExist() {
        
    }

}
