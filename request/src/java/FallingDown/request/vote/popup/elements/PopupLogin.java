package FallingDown.request.vote.popup.elements;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.www.LoginZone;
import me.FallingDownLib.CommonClasses.www.SubscribeZone;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PopupLogin implements ToCodeConverter {

    private StringBuilder pop_login_build;
    private HttpServletRequest request;

    /**
     * 
     * @param uRequest
     */
    protected PopupLogin(HttpServletRequest uRequest) {
        request = uRequest;
        pop_login_build = new StringBuilder();
    }

    /**
     * 
     * @param uRequest
     * @return
     */

    public static PopupLogin getInstance(HttpServletRequest uRequest) {
        return new PopupLogin(uRequest);
    }

    /**
     * 
     * @return
     */

    public String getHTMLCode() {
        buildPopUpLogin();
        buildSubscribeZone();
        return pop_login_build.toString();
    }

    /**
     * 
     */

    private void buildPopUpLogin() {
        pop_login_build.append("<div id=\"login_zone\">");
        pop_login_build.append("<div class=\"title\">Se connecter</div>");
        LoginZone log_zone = new LoginZone(request);
        pop_login_build.append(log_zone.getHTMLCode());
        pop_login_build.append("</div>");
    }

    /**
     * 
     */

    private void buildSubscribeZone() {
        pop_login_build.append("<div id=\"suscribe_zone\">");

        pop_login_build.append("<div id=\"subscribe_click\">");
        pop_login_build.append("<div class=\"title\">Pas encore inscrit ?</div>");
        pop_login_build.append("Cliquez ici pour vous inscrire "
                + "en une minute !");
        pop_login_build.append("</div>");
        pop_login_build.append("<div id=\"subscribe\" style=\"display:none;\">");
        SubscribeZone sub_zone = SubscribeZone.getInstance(request);
        pop_login_build.append(sub_zone.getHTMLCode());
        pop_login_build.append("</div>");


        pop_login_build.append("</div>");

    }
}
