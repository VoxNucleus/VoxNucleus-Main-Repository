package FallingDown.user.login;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.www.SubscribeZone;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintQuickLogin implements ToCodeConverter {

    private StringBuilder overlayBuilder;
    private HttpServletRequest client_request;
    public static String QUICK_FORM = "quick";
    public static String ASSOCIATED_REQUEST = "associated_request";

    protected PrintQuickLogin() {
        overlayBuilder = new StringBuilder();
    }

    public static PrintQuickLogin getOverlay(HttpServletRequest request) {
        return new PrintQuickLogin( request);
    }

    /**
     * Obliged to put a request for jcaptcha
     * @param request
     */
    private PrintQuickLogin(HttpServletRequest request) {
        this();
        client_request=request;

    }

    private void buildOverlay() {
        buildPanes();
    }

    private void buildPanes() {
        overlayBuilder.append("<div class=\"logsub_panes\">");
        overlayBuilder.append("<div>");
        buildLoginPane();
        overlayBuilder.append("</div>");
        overlayBuilder.append("<div>");
        buildSubscribePane();
        overlayBuilder.append("</div>");
        overlayBuilder.append("</div>");
    }

    /**
     * Login page
     */
    private void buildLoginPane() {
        overlayBuilder.append("<div id=\"quick_login\">");
        overlayBuilder.append("<form id=\"quick_login_form\" method=\"POST\" action=\"/usermanagement/login\">");
        overlayBuilder.append("<table style=\"width:100%;\">");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Utilisateur </td>");
        overlayBuilder.append("<td> <input required=\"required\" type=\"text\" name=\"").append(UserFields.HTTP_LOGIN_USERNAME).append("\"> </td>");
        overlayBuilder.append("<td > Mot de passe </td>");
        overlayBuilder.append("<td> <input required=\"required\" type=\"password\" name=\"").append(UserFields.HTTP_PASSWORD).append("\"> </td>");
        overlayBuilder.append("<td> <button type=\"submit\">Connexion</button><td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("</table>");
        overlayBuilder.append("</form>");
        overlayBuilder.append("<a style=\"color:#fff;font-size:80%;\" href=\"usermanagement/modify/passwordreset\"> "+
                        "Mot de passe oublié ? Cliquez ici pour résoudre ce problème.</a>");
        overlayBuilder.append("</div>");
    }

    @Override
    public String getHTMLCode() {
        buildOverlay();
        return overlayBuilder.toString();
    }

    private void buildSubscribePane() {
        overlayBuilder.append("<div id=\"quick_subscribe\">");
        overlayBuilder.append("<div style=\"padding: 5px 0 0 0\" id=\"subscribe_title\"> S'inscrire </div>");
        overlayBuilder.append("<div style=\"padding: 5px 0\" class=\"disclaimer\">"
                + " S'enregistrer prend quelques secondes, nous nous engageons à "
                + "respecter votre vie privée ainsi vos informations personnelles."
                + "<br>"
                + "Simple, non ? </div>");
        SubscribeZone sub_zone = SubscribeZone.getInstance(client_request);
        overlayBuilder.append(sub_zone.getHTMLCode());
/*
        overlayBuilder.append("<form id=\"quick_subscribe_form\" method=\"POST\" action=\"/usermanagement/createUser\">");
        overlayBuilder.append("<table>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td class=\"left_col\"> Nom d'Utilisateur </td>");
        overlayBuilder.append("<td> <input required=\"required\" type=\"text\" name=\"").append(UserFields.HTTP_USERNAME).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td class=\"left_col\"> Adresse mail </td>");
        overlayBuilder.append("<td> <input required=\"required\" type=\"email\" name=\"").append(UserFields.HTTP_EMAIL).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td class=\"left_col\"> Mot de passe </td>");
        overlayBuilder.append("<td> <input required=\"required\" pattern=\"[a-zA-Z0-9]{5,12}\" type=\"password\" name=\"").append(UserFields.HTTP_PASSWORD).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td class=\"left_col\"> Entrez le texte ci-dessous </td>");
        overlayBuilder.append("<td> <input required=\"required\" pattern=\"[a-zA-Z0-9]{3,8}\" type=\"text\" name=\"").append("j_captcha_response").append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr id=\"captcha_row\">");
        overlayBuilder.append("<td class=\"left_col\"></td>");
        overlayBuilder.append("<td> <img src=\"/usermanagement/CaptchaCreate?").append(QUICK_FORM).append("=true&").append(ASSOCIATED_REQUEST).append("=").append(client_request.getSession().getId()).append("\" alt=\"Erreur Captcha\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("</table>");
        overlayBuilder.append("<td> <INPUT type=\"hidden\" value=\"").append(client_request.getSession().getId()).append("\" name=\"").append(ASSOCIATED_REQUEST).append("\"> </td>");
        overlayBuilder.append("<td> <INPUT type=\"hidden\" value=\"true\" name=\"").append(QUICK_FORM).append("\"> </td>");
        overlayBuilder.append("<p align=\"center\"> <button type=\"submit\">S'enregistrer</button>");
        overlayBuilder.append("</form>");*/
        overlayBuilder.append("</div>");
    }
}
