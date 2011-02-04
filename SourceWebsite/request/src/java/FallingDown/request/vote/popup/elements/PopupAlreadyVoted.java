package FallingDown.request.vote.popup.elements;

import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PopupAlreadyVoted extends PopupVote implements ToCodeConverter {

    /**
     * Constructor
     * @param pHash
     */
    protected PopupAlreadyVoted(PostHash pHash){
        super(pHash);
    }

    /**
     * 
     * @param pHash
     * @return
     */
    public static PopupAlreadyVoted getInstance(PostHash pHash){
        return new PopupAlreadyVoted(pHash);
        
    }

    /**
     * 
     */
    @Override
    protected void buildVoteZone() {
        super.append("<div id=\"vote_zone\" style=\"height=31px;font-size:170%;"
                + "font-weight:bolder;text-align:center;\">");
        super.append("Vous avez déjà voté pour ce noyau");
        super.append("</div>");
    }
}
