package FallingDown.user.option;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/** Option page
 *
 * @author victork
 */
public class PrintOption implements ToCodeConverter {

    StringBuilder option_page_builder;
    HttpServletRequest request;
    private Pass pass;

    /**
     *
     */

    private PrintOption(){
        option_page_builder = new StringBuilder();
    }

    /**
     *
     * @param req
     */

    private PrintOption(HttpServletRequest req) {
        this();
        request=req;
        pass = Pass.getPass(request);
        pass.launchAuthentifiate();

        
    }

    public static PrintOption getInstance(HttpServletRequest request){
        return new PrintOption(request);
    }




    private void buildOptionPage(){
        option_page_builder.append(SiteElements.getDoctypeHTML());
        option_page_builder.append("<html>");
        buildHead();
        buildBody();
        buildFooter();
        option_page_builder.append("</html>");
    }

    /**
     * 
     * @return
     */

    public String getHTMLCode() {
        buildOptionPage();
        return option_page_builder.toString();
    }

    /**
     *
     */
    private void buildHead() {
        Metadata meta= Metadata.getInstance();

        option_page_builder.append("<head>");
        option_page_builder.append(meta.getHTMLCode());
        option_page_builder.append(SiteDecorations.setFavIcon());
        option_page_builder.append("<title>").append("VoxNucleus : Mes Options... ").append("</title>");
        option_page_builder.append(SiteElements.getCommonScripts());
        insertStyle();
        option_page_builder.append("<head>");
    }

    /**
     *
     */
    private void insertStyle(){
        option_page_builder.append(SiteElements.getCommonCSSStyle());
        option_page_builder.append(SiteElements.getOneColumnStyle());
        option_page_builder.append("<link rel=\"stylesheet\" "
                + "type=\"text/css\" href=\"/css/option/option_page.css\" >");
    }

    /**
     *
     */

    private void buildBody() {
        option_page_builder.append("<body>");
        option_page_builder.append(SiteElements.displayMenu("", "", "",pass));
        option_page_builder.append("<div id=\"container\">");
        option_page_builder.append("<div id=\"content\">");
        displayOptions();
        option_page_builder.append("</div>");
        option_page_builder.append("</div>");
        option_page_builder.append(GoogleAnalytics.getAnalyticsCode());
        option_page_builder.append("</body>");

    }

    /**
     *
     */

    private void displayOptions(){
        option_page_builder.append("<h1> Options du compte</h1>");
        option_page_builder.append("<div id=\"disclaimer\"><i>Comme le site est encore au stade de la beta,"
                + " les options sont pour le moment limitées mais soyez certains "
                + "que les fonctionnalités s'enrichiront au fur et à mesure.<i></div>");
        option_page_builder.append("<div id=\"option_disconnect\" >"
                + "<a href=\"/usermanagement/disconnect\"> Se déconnecter </a> </div>");
        option_page_builder.append("<div id=\"option_modify\">"
                + "<a href=\"/usermanagement/modifyuser\"> Modifier mon compte </a></div>");
        option_page_builder.append("<div id=\"option_modify\">"
                + "<a href=\"/usermanagement/messages\"> Mes messages </a></div>");
    }


    private void buildFooter() {
        option_page_builder.append(SiteElements.displayFooter(pass));
    }

}
