package FallingDown.viewUser;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Exceptions.UserDoesNotExist;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class PrintUser {

    private String idUser;
    private StringBuilder page;
    private HttpServletRequest request;
    private Pass pass;
    UserHash userInfo;
    /**
     * Build a page from data or Exception caught.
     * TODO : Finish caughting all exceptions
     * @param id
     */
    public PrintUser(String id,HttpServletRequest req) {
        this.idUser = id;
        page = new StringBuilder();
        request=req;
        pass = Pass.getPass(request);
        pass.launchAuthentifiate();
        try {
            HashMap<String, String> result = User.getUserFromDatabase(idUser);
            buildPageFromHash(result);
        } catch (UserDoesNotExist ex) {
            buildPageUserNotFound();
        } catch (InvalidRequestException ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            buildPageUserNotFound();
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PrintUser.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /**
     * Display informations of a given user.
     * @param map
     */
    private void buildPageFromHash(HashMap<String, String> map) {
        userInfo = new UserHash(map);
        Metadata meta = Metadata.getInstance();
        meta.setAdditionnalKeywords("utilisateur,"+idUser);
        page.append(SiteElements.getDoctypeHTML());
        page.append("<html>");
        page.append("<head>");
        page.append(meta.getHTMLCode());
        page.append(SiteDecorations.setFavIcon());
        page.append("<title> VoxNucleus : Utilisateur : ").append(userInfo.getUsername()).append(" </title>");
        insertCSSStyle();
        insertJavascriptInclude();
        page.append("</head>");
        page.append("<body>");
        page.append(SiteElements.displayMenu("Tout", "Tout", "",pass));
        page.append("<div id=\"container\">");
        page.append("<div id=\"content\">");
        buildInside();
        page.append("</div>");
        page.append("</div>");
        page.append("</div>");
        page.append(SiteElements.getsublogOverlay());
        page.append(SiteElements.displayFooter(pass));
        page.append(GoogleAnalytics.getAnalyticsCode());
        page.append("</body>");
        page.append("</html>");

    }

    /**
     * Print an error when the user isn't found.
     */
    private void buildPageUserNotFound() {
        StandardOneColumnPage userNotFoundPage = StandardOneColumnPage.getInstance(request);
        userNotFoundPage.setTitle("VoxNucleus - Erreur utilisateur non trouvé");
        userNotFoundPage.setContent("Nous sommes désolés mais l'utilisateur demandé ne semble pas "
                + "exister dans nos bases de données. Verifiez son nom ou contacter un administrateur");
        page.append(userNotFoundPage.getHTMLCode());
    }

    /**
     * Convert the resulting page to a long String
     * @return
     */
    public String getPageResult() {
        return page.toString();
    }

    /**
     * Add CSS style to the page
     */
    private void insertCSSStyle() {
        page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/post.css\" />");
        page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/user.css\" />");
        page.append(SiteDecorations.getRankZoneStyle());
        page.append(SiteDecorations.getOverlayStyle());
        page.append(SiteElements.getOneColumnStyle());
        page.append(SiteElements.getCommonCSSStyle());
        
    }

    /**
     * Add javascript scripts to the page.
     */
    private void insertJavascriptInclude() {
        page.append(SiteElements.getCommonScripts());
        page.append(SiteElements.getOverlayScript());
        page.append("<script type=\"text/javascript\" src=\"/jsp/user/jloadpostbyuser.js\"></script>");
        page.append("<script type=\"text/javascript\" src=\"/jsp/jloadPosts.js\"></script>");
        page.append("<script> $(document).ready(function () { retrievepostbyuser('").append(idUser).append("','").append(10).append("'); });</script>");
    }

    private void buildInside() {
        UserLeftPanel leftPanel = UserLeftPanel.getLeftPanel(userInfo);
        UserRightPanel rightPanel = UserRightPanel.getRightPanel(userInfo);
        page.append("<ol id=\"info_user\">");
        page.append(leftPanel.getHTMLCode());
        page.append(rightPanel.getHTMLCode());
        page.append("</ol>");
    }
}


/**
 *         page.append("<h1>").append(idUser).append("</h1>");
        page.append("<div id=\"userinformations\" >");
        printUserInformations(userInfo);
        page.append("</div>");
        page.append("Derniers messages postés par l'utilisateur : <br>");
        page.append("<div id=\"listposts\" />");
        insertZonePostByUser();
 */