package FallingDown.user.modify.www;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Exceptions.UserDoesNotExist;
import me.FallingDownLib.CommonClasses.SupportedLanguages;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import org.apache.cassandra.thrift.NotFoundException;

/**
 *
 * @author victork
 */
public class PrintModifyUser implements ToCodeConverter {

    UserHash userToModifyHash;
    private StringBuilder modify_builder;
    private HttpServletRequest request;
    private Pass pass;

    private PrintModifyUser(){
        SupportedLanguages.getInstance();
        modify_builder = new StringBuilder();
    }

    private PrintModifyUser(HttpServletRequest request,String user) throws UserDoesNotExist, NotFoundException, Exception{
        this();
        this.request=request;
        pass = Pass.getPass(request);
        userToModifyHash = new UserHash(User.getUserFromDatabase(user));
    }

    /**
     *
     * @param user
     * @return instance
     */
    public static PrintModifyUser getInstance(HttpServletRequest request,String user) throws UserDoesNotExist, NotFoundException, Exception{
        return new PrintModifyUser(request,user);

    }


    private void insertStyle(){
        modify_builder.append(SiteElements.getCommonCSSStyle());
        modify_builder.append(SiteElements.getOneColumnStyle());
        modify_builder.append("<link rel=\"stylesheet\" "
                + "type=\"text/css\" href=\"/css/option/modify_user.css\" >");
    }


    /**
     * Main building block
     */

    private void buildPage(){
        modify_builder.append(SiteElements.getDoctypeHTML());
        modify_builder.append("<html>");
        buildHead();
        buildBody();
        buildFooter();
        modify_builder.append("</html>");
    }

    /**
     * Build the code for the head of the document
     */
    private void buildHead() {
        modify_builder.append("<head>");
        modify_builder.append(SiteDecorations.setFavIcon());
        modify_builder.append("<title>").append("VoxNucleus : Modifier les informations du compte : ").append(userToModifyHash.getUsername()).append("</title>");
        modify_builder.append(SiteElements.getCommonScripts());
        modify_builder.append("<script type=\"text/javascript\" src=\"/jsp/plugins/autoresize.jquery.min.js\"></script>");
        modify_builder.append("<script type=\"text/javascript\" src=\"/jsp/usermanagement/modifyuser.js\"></script>");
        insertStyle();
        modify_builder.append("<head>");
    }

    /**
     * Construct body of the page
     */

    private void buildBody(){
        modify_builder.append(SiteElements.displayBasicMenu(pass));
        modify_builder.append("<body>");
        modify_builder.append("<div id=\"container\">");
        modify_builder.append("<div id=\"content\">");
        modify_builder.append("<h1> Modifier les informations  </h1>");
        displayInformationUser();
        modify_builder.append("</div>");
        modify_builder.append("</div>");
        modify_builder.append(GoogleAnalytics.getAnalyticsCode());
        modify_builder.append("</body>");
    }


    /**
     * Display information on the user
     */

    private void displayInformationUser(){
        modify_builder.append("<form method=\"POST\" "
                + "enctype=\"multipart/form-data\" "
                + "action=\"/usermanagement/validatemodificationuser\">");
        modify_builder.append("<div id=\"informations\" >");
        modify_builder.append("<fieldset id=\"mandatory_informations\">");
        modify_builder.append("<legend align=\"top\">Informations obligatoires</legend>");
        displayModifyMandatoryInformations();
        modify_builder.append("</fieldset>");

        modify_builder.append("<fieldset id=\"renew_password\">");
        modify_builder.append("<legend align=\"top\">Modification du mot de passe</legend>");
        displayPasswordChange();
        modify_builder.append("</fieldset>");

        modify_builder.append("<fieldset id=\"account_personnalization\">");
        modify_builder.append("<legend align=\"top\">Informations complémentaires</legend>");
        displayMoreInformations();
        modify_builder.append("</fieldset>");
        modify_builder.append("</div>");
        modify_builder.append("<p align=\"center\">");
        modify_builder.append("<button type=\"submit\">Enregistrer les modifications</button>");
        modify_builder.append("</form>");
    }


    private void buildFooter(){
        modify_builder.append(SiteElements.displayFooter(pass));
    }


    /**
     *
     * @return an error page if the user is not connected
     */
    public static String notConnected(HttpServletRequest request){
        StandardOneColumnPage not_connected_page = StandardOneColumnPage.getInstance(request);
        not_connected_page.setTitle("VoxNucleus : Erreur vous ne pouvez pas entrer sur cette page");
        not_connected_page.setContent("Vous ne pouvez pas !");
        return not_connected_page.getHTMLCode();
    }



    /**
     *
     * @return code HTML of the page
     */
    public String getHTMLCode() {
        pass.launchAuthentifiate();
        buildPage();
        return modify_builder.toString();
    }

    /**
     * Print all mandatory informations
     */

    private void displayModifyMandatoryInformations() {
        modify_builder.append("<table>");
        modify_builder.append("<tr>");
        modify_builder.append("<td class=\"left_col\">Langue :</td><td>").append(SupportedLanguages.getSELECT_HTML("fr")).append("</td>");
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Email :", UserFields.HTTP_EMAIL,
                userToModifyHash.getEmail()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Email (confirmation) :",
                UserFields.HTTP_EMAIL_CONFIRM, userToModifyHash.getEmail()));
        modify_builder.append("</tr>");
        modify_builder.append("</tr>");
        modify_builder.append("</table>");
    }

    /**
     * Print all other informations including the images
     */

    private void displayMoreInformations() {
        modify_builder.append("<table>");
        modify_builder.append("<tr>");
        modify_builder.append("<td class=\"left_col\">Nouvel avatar : </td>");
        modify_builder.append("<td >").append(getAvatarImage()).append(" </td>");
        modify_builder.append("</tr>");

        modify_builder.append("<tr>");
        modify_builder.append("<td class=\"left_col\">Avatar actuel : </td>");
        modify_builder.append("<td >").append(SiteElements.insertUserAvatar(userToModifyHash)).append(" </td>");
        modify_builder.append("</tr>");

        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Prénom :", UserFields.HTTP_FIRSTNAME,
                userToModifyHash.getFirstname()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Nom :", UserFields.HTTP_LASTNAME,
                userToModifyHash.getLastname()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Date de naissance<br>(jj/mm/aaaa) :",
                UserFields.HTTP_BIRTHDATE, userToModifyHash.getBirthDate()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Votre site web :",
                UserFields.HTTP_SITE_WEB, userToModifyHash.getWebsite()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append(getStandardLine("Centres d'intérêt :",
                UserFields.DB_CENTER_INTERESTS, userToModifyHash.getCenterOfInterest()));
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
        modify_builder.append("<td class=\"left_col\">Votre description : ").append("</td>");
        modify_builder.append("<td>").append("<textarea name=\"").append(UserFields.HTTP_DESCRIPTION).append("\" rows=5 cols=52 >").append(userToModifyHash.getDescription()).append("</textarea>").append("</td>");
        modify_builder.append("</tr>");
        modify_builder.append("</table>");
        
    }
    /**
     * 
     * @param colText
     * @param textarea_name
     * @param value
     * @return code for a line of a table
     */

    private String getStandardLine(String colText, String input_text_name,String value){
        return "<td class=\"left_col\">"+ colText+"</td>"+"<td><input type=text value=\"" + value+"\" name=\""+input_text_name+"\" ></td>";
    }

    /**
     *
     * @return
     */
    private String getAvatarImage(){
       return "<input name=\""+UserFields.HTTP_AVATAR+"\" "
                + " type=\"file\" id=\"avatar\">";
    }

    /**
     * Display zone for the password change
     */
    private void displayPasswordChange() {
        modify_builder.append("<table>");
        modify_builder.append("<tr>");
        modify_builder.append("<td class=\"left_col\">"+ "Mot de passe actuel :"
                +"</td>"+"<td><input type=password value=\"" + ""+"\" name=\""
                +UserFields.HTTP_OLD_PASSWORD+"\" ></td>");
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
                modify_builder.append("<td class=\"left_col\">"+ "Mot de passe actuel :"
                +"</td>"+"<td><input type=password value=\"" + ""+"\" name=\""
                +UserFields.HTTP_PASSWORD+"\" ></td>");
        modify_builder.append("</tr>");
        modify_builder.append("<tr>");
                       modify_builder.append("<td class=\"left_col\">"+ "Mot de passe actuel :"
                +"</td>"+"<td><input type=password value=\"" + ""+"\" name=\""
                +UserFields.HTTP_PASSWORD_CONFIRM+"\" ></td>");
        modify_builder.append("</tr>");
        modify_builder.append("</table>");

    }

}
