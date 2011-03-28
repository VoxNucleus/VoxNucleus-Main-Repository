package FallingDown.user.messages;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * Class
 * @author victork
 */
public class PrintMessages implements ToCodeConverter {

    StringBuilder messages_builder;
    HttpServletRequest associated_request;
    Pass userPass;

    protected PrintMessages(){
        messages_builder = new StringBuilder();
    }

    private PrintMessages(HttpServletRequest request) {
        this();
        associated_request=request;

    }


    public static PrintMessages getMessages(HttpServletRequest request){
        return new PrintMessages(request);
    }

    /**
     * Set pass for authentification
     * @param pass
     */

    public void setPass(Pass pass){
        userPass=pass;
    }

    /**
     *
     */
    private void buildPage(){
        buildHead();
        buildBody();
        buildFooter();
    }

    @Override
    public String getHTMLCode() {
        buildPage();
        return messages_builder.toString();
    }

    /**
     *
     */
    private void buildHead() {
        messages_builder.append("<head>");
        messages_builder.append(SiteElements.getCommonCSSStyle());
        messages_builder.append(SiteElements.getOneColumnStyle());
        messages_builder.append(SiteElements.getCommonScripts());
        messages_builder.append("</head>");
    }

    private void buildBody() {
        messages_builder.append("<body>");
        messages_builder.append(SiteElements.displayMenu("", "", "", userPass));
        messages_builder.append("<div id=\"container\">");
        messages_builder.append("<div id=\"content\">");

        messages_builder.append("Non encore implémenté...<br>");
        messages_builder.append("Bientôt !");
        messages_builder.append("</div>");
        messages_builder.append("</div>");
        messages_builder.append("</body>");
    }

    private void buildFooter() {
        messages_builder.append(SiteElements.displayFooter(userPass));
    }



}
