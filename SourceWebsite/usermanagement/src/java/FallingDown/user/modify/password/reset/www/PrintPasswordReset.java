package FallingDown.user.modify.password.reset.www;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintPasswordReset implements ToCodeConverter {

    private StringBuilder pass_reset_builder;
    private HttpServletRequest request;
    StandardOneColumnPage pass_reset_page;

    protected PrintPasswordReset(HttpServletRequest req){
        request = req;
        pass_reset_builder = new StringBuilder();
    }

    public static PrintPasswordReset getInstance(HttpServletRequest req){
        return new PrintPasswordReset(req);
    }

    @Override
    public String getHTMLCode() {
        buildPage();
        return pass_reset_page.getHTMLCode();
    }

    private void buildPage() {
        pass_reset_page =StandardOneColumnPage.getInstance(request);
        pass_reset_page.addStyle("<link rel=\"stylesheet\" type=\"text/css\" "
                + "href=\"/css/usermanagement/passwordreset.css\" >");
        pass_reset_page.addScript("<script type=\"text/javascript\" "
                + "src=\"/jsp/usermanagement/jpasswordreset.js\"></script>");

        pass_reset_page.setTitle("VoxNucleus : Mot de passe oublié");
        buildResetPassForm();
        pass_reset_page.setContent(pass_reset_builder.toString());
    }

    private void buildResetPassForm() {
        pass_reset_builder.append("<h1>Réinitiliaser votre mot de passe</h1>");
        pass_reset_builder.append("<div  class=\"explanation\">");
        pass_reset_builder.append("Entrez votre nom d'utilisateur et "
                + "l'adresse mail que vous avez associés au compte pour "
                + "recevoir la suite des instructions par mail.");
        pass_reset_builder.append("</div>");
        pass_reset_builder.append("<form id=\"form_reset_password\" "
                + "action=\"/usermanagement/modify/passwordreset\" "
                + "method=\"post\">");
        pass_reset_builder.append("<table>");

        pass_reset_builder.append("<tr>");
        pass_reset_builder.append("<td> Utilisateur </td>");
        pass_reset_builder.append("<td> <input type=\"text\" required=\"required\""
                + " name=\"").append(UserFields.HTTP_LOGIN_USERNAME).append("\"> </td>");
        pass_reset_builder.append("</tr>");

        pass_reset_builder.append("<tr>");
        pass_reset_builder.append("<td> Adresse email </td>");
        pass_reset_builder.append("<td> <input type=\"email\" required=\"required\""
                + "name=\"").append(UserFields.HTTP_EMAIL).append("\"> </td>");

        pass_reset_builder.append("</tr>");
        
        pass_reset_builder.append("</table>");
        pass_reset_builder.append("<div class=\"button_container\">");
        pass_reset_builder.append("<button type=\"submit\">Envoyer</button>");
        pass_reset_builder.append("</div>");

        pass_reset_builder.append("</form>");

    }


}
