package me.FallingDownLib.CommonClasses.www;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class SubscribeZone implements ToCodeConverter {
    public static String QUICK_FORM = "quick";
    public static String ASSOCIATED_REQUEST = "associated_request";
    private StringBuilder sub_zone_build;
    private HttpServletRequest request;

    protected SubscribeZone(HttpServletRequest uRequest){
        request = uRequest;
        sub_zone_build = new StringBuilder();
    }

    public static SubscribeZone getInstance(HttpServletRequest uRequest){
        return new SubscribeZone(uRequest);
        
    }

    public String getHTMLCode() {
        buildSubscribeZone();
        return sub_zone_build.toString();
    }

    private void buildSubscribeZone() {

        sub_zone_build.append("<form id=\"quick_subscribe_form\" method=\"POST\" action=\"/usermanagement/createUser\">");
        sub_zone_build.append("<table>");
        sub_zone_build.append("<tr>");
        sub_zone_build.append("<td class=\"left_col\"> Nom d'Utilisateur </td>");
        sub_zone_build.append("<td> <input required=\"required\" type=\"text\" name=\"").append(UserFields.HTTP_USERNAME).append("\"> </td>");
        sub_zone_build.append("</tr>");
        sub_zone_build.append("<tr>");
        sub_zone_build.append("<td class=\"left_col\"> Adresse mail </td>");
        sub_zone_build.append("<td> <input required=\"required\" type=\"email\" name=\"").append(UserFields.HTTP_EMAIL).append("\"> </td>");
        sub_zone_build.append("</tr>");
        sub_zone_build.append("<tr>");
        sub_zone_build.append("<td class=\"left_col\"> Mot de passe </td>");
        sub_zone_build.append("<td> <input required=\"required\" pattern=\"[a-zA-Z0-9]{5,12}\" "
                + "type=\"password\" name=\"").append(UserFields.HTTP_PASSWORD).append("\"> </td>");
        sub_zone_build.append("</tr>");
        sub_zone_build.append("<tr>");
        sub_zone_build.append("<td class=\"left_col\"> Entrez le texte ci-dessous </td>");
        sub_zone_build.append("<td> <input required=\"required\" pattern=\"[a-zA-Z0-9]{3,8}\" "
                + "type=\"text\" name=\"").append("j_captcha_response").append("\"> </td>");
        sub_zone_build.append("</tr>");
        sub_zone_build.append("<tr id=\"captcha_row\">");
        sub_zone_build.append("<td class=\"left_col\"></td>");
        sub_zone_build.append("<td> <img src=\"/usermanagement/CaptchaCreate?");
        sub_zone_build.append(QUICK_FORM).append("=true&").append(ASSOCIATED_REQUEST);
        sub_zone_build.append("=").append(request.getSession().getId()).append("\" alt=\"Erreur Captcha\"> </td>");
        sub_zone_build.append("</tr>");
        sub_zone_build.append("</table>");
        sub_zone_build.append("<td> <INPUT type=\"hidden\" value=\"").append(request.getSession().getId());
        sub_zone_build.append("\" name=\"").append(ASSOCIATED_REQUEST).append("\"> </td>");
        sub_zone_build.append("<td> <INPUT type=\"hidden\" value=\"true\" name=\"").append(QUICK_FORM).append("\"> </td>");
        sub_zone_build.append("<p align=\"center\"> <button type=\"submit\">S'enregistrer</button>");
        sub_zone_build.append("</form>");

    }



}
