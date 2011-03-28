package FallingDown.post.viewPost;

import FallingDown.post.viewPost.elements.NucleusComments;
import FallingDown.post.viewPost.elements.NucleusCorpse;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.CommonClasses.www.boxes.BestBox;
import me.FallingDownLib.CommonClasses.www.boxes.SimilarPostsBox;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.thrift.TException;

/**
 * Class that prints the post
 * @author victork
 */
public class PrintPost implements ToCodeConverter {

    public static final String WINDOW_OPTION = "'toolbar=no,resizable=no,menubar=no,location=no'";
    private String postId;
    private StringBuilder page;
    private HttpServletRequest request;
    private PostHash pHash;
    private Pass pass;
    private boolean isStatic;
    private boolean canBeModified;
    private int commentNumber;

    /**
     * 
     */
    private PrintPost() {
        page = new StringBuilder();
        canBeModified=false;
    }

    /**
     *
     * @param request
     * @param id
     */
    private PrintPost(HttpServletRequest request, String id) {
        this();
        postId = id;
        this.request = request;
        pass=Pass.getPass(request);
        pass.launchAuthentifiate();

    }

    /**
     *
     * @param request
     * @param postId
     * @return an instance of PrintPost
     */
    public static PrintPost getInstance(HttpServletRequest request, String postId) {
        return new PrintPost(request, postId);
    }

    /**
     * Build the webpage from the data stored
     * @param data
     */
    private void buildPageFromHash(HashMap<String, String> data) {
        pHash = new PostHash(data);
        Metadata meta = Metadata.getInstance();
        meta.setAdditionnalKeywords("noyau," + pHash.getSubcategory() + "," + pHash.getCategory() + "," + pHash.getTags());
        page.append(SiteElements.getDoctypeHTML());
        page.append("<html>");
        page.append("<head>");
        page.append(meta.getHTMLCode());
        page.append(SiteDecorations.setFavIcon());
        page.append("<title> VoxNucleus : ").append(pHash.getTitle()).append("</title>");
        insertCSSStyle();
        insertJavascript();
        insertScript(data);
        page.append("</head>");
        page.append("<body>");
        page.append(SiteElements.displayMenu(pHash.getCategory(),
                pHash.getSubcategory(), "",pass));
        page.append("<div id=\"container\">");
        page.append("<div id=\"content\">");
        NucleusCorpse nuc_corpse =NucleusCorpse.getInstance(pHash);
        nuc_corpse.setCanBeModified(canBeModified);
        page.append(nuc_corpse.getHTMLCode());

        NucleusComments nuc_comments = NucleusComments.getInstance(postId);
        nuc_comments.setIsStaticComment(isStatic);
        nuc_comments.setHttpRequest(request);
        page.append(nuc_comments.getHTMLCode());
        //insertComments(data);
        page.append(SiteElements.printCommentingArea(postId,pass));
        insertReportZone();
        page.append("</div>");
        pageInsertLateralRight();
        page.append("</div>");
        page.append(SiteElements.getsublogOverlay());
        page.append(SiteElements.displayFooter(pass));
        page.append(GoogleAnalytics.getAnalyticsCode());
        page.append("</body>");
        page.append("</html>");
    }


    /**
     *
     */
    private void pageInsertLateralRight() {
        BestBox best_box = BestBox.getInstance();
        SimilarPostsBox similar_box = SimilarPostsBox.getInstance(postId);
        page.append("<div id=\"float_right\">");
        page.append(best_box.getHTMLCode());
        page.append(similar_box.getHTMLCode());
        page.append("</div>");
    }

    /**
     * Add CSS style to the page
     */
    private void insertCSSStyle() {
        page.append(SiteElements.getCommonCSSStyle());
        page.append(SiteElements.getTwoColumnStyle());
        page.append(SiteDecorations.getRankZoneStyle());
        page.append(SiteDecorations.getOverlayStyle());
        page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/post_page.css\" >");
    }

    /**
     * Add javascript scripts to the page.
     */
    private void insertJavascript() {
        page.append(SiteElements.getCommonScripts());
        page.append(SiteElements.getOverlayScript());
        page.append("<script type=\"text/javascript\" src=\"/jsp/plugins/autoresize.jquery.min.js\"></script>");
        page.append("<script type=\"text/javascript\" src=\"/jsp/post/jloadcomments.js\"></script>");
        page.append("<script type=\"text/javascript\" src=\"/jsp/post/post.js\"></script>");
        page.append("<script type=\"text/javascript\" src=\"/jsp/deleter/jdeleter.js\"></script>");

    }

    /**
     * This function insert script in the "head" 
     * @param data
     */
    private void insertScript(HashMap<String, String> data) {
        page.append("<script type=\"text/javascript\"> $(document).ready(function(){retrieveComments(\"")
                .append(data.get("key")).append("\");});</script>");
    }

    /**
     * 
     */
    private void insertReportZone() {
        page.append("<div id=\"report_area\">");
        page.append("<span onclick=\"window.open('/postmanagement/reportpost?postid=").append(pHash.getKey()).append("','Report window'," + WINDOW_OPTION + ")\" class=\"click_zone\"> Signaler du contenu </span>");
        page.append("</div>");
    }

    /**
     *
     * @return HTML code
     */
    public String getHTMLCode() {
        try {
            HashMap<String, String> result = Post.getPostFromDatabase(postId);
            Post.updateViewCount(postId);
            buildPageFromHash(result);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(PrintPost.class.getName()).log(Level.SEVERE, null, ex);
            return OverchargeErrorMessage(request, ex);
        } catch (PostDoesNotExist ex) {
            Logger.getLogger(PrintPost.class.getName()).log(Level.SEVERE, null, ex);
            buildPagePostNotFound();
        } catch (TException ex) {
            Logger.getLogger(PrintPost.class.getName()).log(Level.SEVERE, null, ex);
            return ImportantErrorMessage(request, ex);
        } catch (Exception ex) {
            Logger.getLogger(PrintPost.class.getName()).log(Level.SEVERE, null, ex);
            return ImportantErrorMessage(request, ex);
        }
        return page.toString();
    }




    /**
     * 
     * @param request
     * @param ex
     * @return
     */

    private String OverchargeErrorMessage(HttpServletRequest request,Exception ex){
        StandardOneColumnPage major_error_page= StandardOneColumnPage.getInstance(request);
        major_error_page.setTitle("VoxNucleus : Serveur surchargé...");
        major_error_page.setContent("Le serveur est surchargé pour le moment, "
                + "merci de réessayer plus tard.<br>"
                + "Nous nous excusons pour cette erreur nous travaillons à l'amélioration "
                + "de nos services.");
        return major_error_page.getHTMLCode();
    }

    /**
     * 
     * @param request
     * @param ex
     * @return
     */


    private String ImportantErrorMessage(HttpServletRequest request, Exception ex) {
        StandardOneColumnPage major_error_page = StandardOneColumnPage.getInstance(request);
        major_error_page.setTitle("VoxNucleus : Erreur grave...");
        major_error_page.setContent("Nous sommes désolés mais une erreur interne "
                + "s'est produite.<br> " + " Vous pouvez rafraîchir cette page, les "
                + "choses seront peut être rétablies.<br>"
                + "Nous nous excusons pour cette erreur et nous travaillons à sa réparation !");
        return major_error_page.getHTMLCode();
    }

    /**
     * This page is build when a page is not found.
     */
    private void buildPagePostNotFound() {
        StandardOneColumnPage error_page = StandardOneColumnPage.getInstance(request);
        error_page.setTitle("Vox Nucleus : Erreur post non trouvé");
        error_page.setContent("<h1>Noyau non trouvé</h1>Le noyau \""
                + postId + "\" n'a pas été trouvé dans la base de données.<br> Si c'est une erreur n'hésitez pas à contacter l'administrateur.");
        page.append(error_page.getHTMLCode());
    }

    /**
     *
     * @param commentstatic
     */
    public void setIsStatic(boolean commentstatic){
        isStatic=commentstatic;
    }

    /**
     * 
     * @param comment_number
     */
    public void setCommentNumber(int comment_number){
        commentNumber=comment_number;
    }
    /**
     *
     * @param beModified
     */

    public void setCanBeModified(boolean beModified){
        canBeModified=beModified;
    }


}
