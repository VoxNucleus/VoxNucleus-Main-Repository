package me.FallingDownLib.CommonClasses.www.overlay;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintQuickLogin implements ToCodeConverter {

    private StringBuilder overlayBuilder;
    private HttpServletRequest client_request;

    public static String QUICK_FORM="quick";
    public static String ASSOCIATED_REQUEST="associated_request";

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
        overlayBuilder.append("<div class=\"logsuboverlay\" id=\"logsub\" style=\"display:none\">");
        buildTabs();
        buildPanes();
        overlayBuilder.append("<div class=\"close\"></div>");
        overlayBuilder.append("</div>");
    }

    private void buildTabs(){
        overlayBuilder.append("<ul class=\"logsub_tabs\">");
        overlayBuilder.append("<li><a href=\"#first_tab\"> Se connecter</a></li>");
        overlayBuilder.append("<li><a href=\"#second_tab\"> S'enregistrer</a></li>");
        overlayBuilder.append("</ul>");
    }

    private void buildPanes(){
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

    private void buildLoginPane(){
        overlayBuilder.append("<form method=\"POST\" action=\"/usermanagement/login\">");
        overlayBuilder.append("<fieldset id=\"login_zone\">");
        overlayBuilder.append("<legend align=\"top\">Se connecter</legend>");
        overlayBuilder.append("<table>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Utilisateur : </td>");
        overlayBuilder.append("<td> <INPUT type=\"TEXT\" NAME=\"").append(UserFields.HTTP_USERNAME).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Mot de passe : </td>");
        overlayBuilder.append("<td> <INPUT type=\"password\" NAME=\"")
                .append(UserFields.HTTP_PASSWORD).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("</table>");
        overlayBuilder.append("</fieldset>");
        overlayBuilder.append("<p align=\"center\"> <button type=\"submit\">Se connecter</button>");
        overlayBuilder.append("</form>");
        
    }

    @Override
    public String getHTMLCode() {
        buildOverlay();
        return overlayBuilder.toString();
    }

    private void buildSubscribePane() {
        overlayBuilder.append("<form method=\"POST\" action=\"/usermanagement/createUser\">");
        overlayBuilder.append("<fieldset id=\"login_zone\">");
        overlayBuilder.append("<legend align=\"top\">S'enregistrer</legend>");
        overlayBuilder.append("S'enregistrer est facile et rapide, vous n'avez "
                + "besoin que de remplir 3 champs.");
        overlayBuilder.append("<table>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Nom d'Utilisateur : </td>");
        overlayBuilder.append("<td> <INPUT type=\"text\" name=\"")
                .append(UserFields.HTTP_USERNAME).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Adresse mail : </td>");
        overlayBuilder.append("<td> <INPUT type=\"text\" name=\"")
                .append(UserFields.HTTP_EMAIL).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Mot de passe : </td>");
        overlayBuilder.append("<td> <input type=\"password\" name=\"")
                .append(UserFields.HTTP_PASSWORD).append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Entrez le texte : </td>");
        overlayBuilder.append("<td> <INPUT type=\"text\" name=\"")
                .append("j_captcha_response").append("\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("<tr>");
        overlayBuilder.append("<td> Mot de passe : </td>");
        overlayBuilder.append("<td> <img src=\"/usermanagement/CaptchaCreate?").append(QUICK_FORM).append("=true&")
                .append(ASSOCIATED_REQUEST).append("=").append(client_request.getSession().getId())
                .append("\" alt=\"Erreur Captcha\"> </td>");
        overlayBuilder.append("</tr>");
        overlayBuilder.append("</table>");
        overlayBuilder.append("</fieldset>");
        overlayBuilder.append("<td> <INPUT type=\"hidden\" value=\"")
                .append(client_request.getSession().getId()).append("\" name=\"")
                .append(ASSOCIATED_REQUEST).append("\"> </td>");
        overlayBuilder.append("<td> <INPUT type=\"hidden\" value=\"true\" name=\"")
                .append(QUICK_FORM).append("\"> </td>");
        overlayBuilder.append("<p align=\"center\"> <button type=\"submit\">Accepter et s'enregistrer</button>");
        overlayBuilder.append("</form>");
    }
}
