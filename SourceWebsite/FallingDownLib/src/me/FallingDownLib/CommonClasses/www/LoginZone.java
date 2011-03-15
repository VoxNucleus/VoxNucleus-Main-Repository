package me.FallingDownLib.CommonClasses.www;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class LoginZone implements ToCodeConverter {

    private StringBuilder login_builder;
    private HttpServletRequest associated_request;

    public LoginZone(HttpServletRequest request) {
        login_builder = new StringBuilder();
        associated_request = request;
    }

    public String getHTMLCode() {

        
        buildLogin();
        return login_builder.toString();
    }

    private void buildLogin() {
        login_builder.append("<form id=\"login_form\" action=\"/usermanagement/login\" "
                + "method=\"POST\">");
        login_builder.append("<table>");
        login_builder.append("<tr>");
        login_builder.append("<td>");
        login_builder.append("Utilisateur :");
        login_builder.append("</td>");
        login_builder.append("<td>");
        login_builder.append("<input required=\"required\" type=\"text\" name=\"").
                append(UserFields.HTTP_LOGIN_USERNAME).append("\">");
        login_builder.append("</td>");
        login_builder.append("</tr>");

        login_builder.append("<tr>");
        login_builder.append("<td>");
        login_builder.append("Mot de passe :");
        login_builder.append("</td>");
        login_builder.append("<td>");
        login_builder.append("<input type=\"password\" required=\"required\" name=\"").
                append(UserFields.HTTP_PASSWORD).append("\">");
        login_builder.append("</td>");
        login_builder.append("</tr>");
        login_builder.append("<input type=\"hidden\" name=\"wherefrom\" value=\"")
                .append(associated_request.getRequestURI());
        if (associated_request.getQueryString() != null) {
            login_builder.append("?").append(associated_request.getQueryString());
        }
        login_builder.append("\">");
        login_builder.append("</table>");
        login_builder.append("<button type=\"submit\">Se connecter</button>");
        login_builder.append("</form>");
    }
}
