package me.FallingDownLib.CommonClasses.www;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class StandardTwoColumnPage implements ToCodeConverter {

    private StringBuilder two_col_page_builder;
    private Pass page_pass = null;
    private HttpServletRequest request;
    private String title;
    private String leftcol_content = "";
    private String rightcol_content = "";

    //TODO 
    private ToCodeConverter left_col;

    protected StandardTwoColumnPage(){
        two_col_page_builder= new StringBuilder();
    }

    public String getHTMLCode() {
        buildPage();
        return two_col_page_builder.toString();
    }

    private void buildPage() {
        two_col_page_builder.append(SiteElements.getDoctypeHTML());
        two_col_page_builder.append("<html>");
        buildHead();
        buildBody();
        buildFooter();
        two_col_page_builder.append("</html>");
    }

    /**
     * Construct body
     */
    private void buildBody() {
        two_col_page_builder.append("<body>");
        two_col_page_builder.append("<div id=\"container\">");
        two_col_page_builder.append("<div id=\"content\">");
        two_col_page_builder.append("</div>");
        two_col_page_builder.append("</div>");
        two_col_page_builder.append("</body>");
    }
    /**
     * Build Head
     */
    private void buildHead() {
        two_col_page_builder.append("<head>");
        two_col_page_builder.append(SiteDecorations.setFavIcon());
        two_col_page_builder.append(SiteElements.getCommonCSSStyle());
        two_col_page_builder.append(SiteElements.getCommonScripts());
        two_col_page_builder.append(SiteElements.getTwoColumnStyle());
        two_col_page_builder.append("</head>");

    }

    /**
     * 
     */
    private void buildFooter() {
        two_col_page_builder.append(SiteElements.displayFooter(page_pass));
    }

}
