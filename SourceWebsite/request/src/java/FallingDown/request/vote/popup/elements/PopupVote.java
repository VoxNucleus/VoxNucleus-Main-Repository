package FallingDown.request.vote.popup.elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.util.MyPattern;
import me.FallingDownLib.CommonClasses.www.post.PrintPost;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PopupVote implements ToCodeConverter{

    private StringBuilder vote_zone_build;
    private PostHash pHash;

    /**
     * Default constructor. Needs post information and 
     * @param postInfo
     */
    protected PopupVote(PostHash postInfo){
        pHash = postInfo;
        vote_zone_build = new StringBuilder();
    }

    /**
     *
     * @param postInfo
     * @return an instance of a votepopup
     */

    public static PopupVote getInstance(PostHash postInfo){
        return new PopupVote(postInfo);
    }

    /**
     * 
     * @return
     */

    public String getHTMLCode() {
        buildPopupCorpse();
        return vote_zone_build.toString();
    }

    /**
     *
     * @return img tag
     */

    public String insertPostImage(){
        return PrintPost.buildPostImage(pHash.getKey(),pHash.getFilename());
    }

    

    protected void buildVoteZone(){
        vote_zone_build.append("<div id=\"vote_zone\">");
        vote_zone_build.append("<button onClick=\"javascript:doVote('").append(pHash.getKey()).append("');\" id=\"vote\">Voter </button>");
        vote_zone_build.append("<button id=\"cancel\">"
                + "Non, fermer la fenêtre</button>");
        vote_zone_build.append("</div>");
    }

    /**
     * Construct the entire vote zone
     */
    private void buildPopupCorpse() {
        buildHead();
        buildVoteZone();
        vote_zone_build.append(PrintPost.buildNbVotesOnly(pHash.getKey(),pHash.getNbVotes()));
        vote_zone_build.append("<div id=\"nucleus_description\">");
        vote_zone_build.append(MyPattern.filterNewLine(pHash.getDescription()));
        vote_zone_build.append("</div>");
        insertNbCommentaires();
        vote_zone_build.append("<div id=\"link_to_nucleus\">");
        vote_zone_build.append("<a target=\"_blank\" href='/post/").append(pHash.getKey());
        vote_zone_build.append("' >"
                + "Voir le noyau complet sur VoxNucleus...</a>");
        vote_zone_build.append("</div>");

    }

    /**
     * Insert the commentaires
     */

    private void insertNbCommentaires(){
        vote_zone_build.append("<div id=\"comments\" style=\"text-align:right;\">");
        int nbComments;
        try{
        nbComments= Integer.parseInt(pHash.getNbComments());
        }catch(NumberFormatException ex){
            nbComments=0;
        }
        if(nbComments>0){
            vote_zone_build.append("<a target=\"_blank\" href='/post/").append(pHash.getKey());
            vote_zone_build.append("' >");
            vote_zone_build.append(pHash.getNbComments());
            vote_zone_build.append(" commentaires pour ce noyau</a>");
        }else{
            vote_zone_build.append("<a target=\"_blank\" href='/post/").append(pHash.getKey());
            vote_zone_build.append("' >");
            vote_zone_build.append("Aucun commentaire, cliquez pour "
                    + "ajouter le votre...").append("</a>");
        }
        vote_zone_build.append("</div>");
    }
    /**
     * Construct date
     */
    private void constructDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MyPattern.POST_DATE_PATTERN);
            vote_zone_build.append("<i>").append(dateFormat.format(new Date(Long.parseLong(pHash.getTimestamp())))).append("</i>");
        } catch (NumberFormatException ex) {
            vote_zone_build.append("<i> Erreur inattendue dans la date </i>");
        }
    }

    /**
     * Build head
     */

    private void buildHead() {
        vote_zone_build.append("<div id=\"nucleus_head\">");
        vote_zone_build.append("<div id=\"image\" >").append(insertPostImage()).append("</div>");
        vote_zone_build.append("<h1><a target=\"_blank\" href=\"/r/");
        vote_zone_build.append(pHash.getKey()).append("\"  >");
        vote_zone_build.append(pHash.getTitle()).append("</a> </h1>");
        constructDate();
        vote_zone_build.append("<span id=\"author\" >").append("<a href=\"/user/").
                append(pHash.getAuthor()).append("\">").append(pHash.getAuthor()).append("</a>").append("</span>");
        vote_zone_build.append("</div>");
    }

    /**
     * Can be accessed
     * @param toAppend
     */
    protected void append(String toAppend){
        vote_zone_build.append(toAppend);
    }


}
