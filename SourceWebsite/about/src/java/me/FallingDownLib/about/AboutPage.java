package me.FallingDownLib.about;

import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class AboutPage implements ToCodeConverter{

    private StringBuilder about_builder ;
    private String bodyContent;
    private String title;
    private String in_page_title;
    private Metadata about_metadata=null;

    private boolean isTeamPage=false;

    /**
     * 
     */
    protected AboutPage(){
        in_page_title="";
        bodyContent="";
        title="";
        about_builder = new StringBuilder();
    }
    /**
     *
     * @return
     */
    public static AboutPage getInstance(){
        return new AboutPage();
    }
    /**
     *
     * @param inside
     */
    public void setBodyContent(ToCodeConverter inside){
        bodyContent=inside.getHTMLCode();
    }

    /**
     *
     * @param page_title
     */
    public void setIn_Page_Title(String page_title){
        in_page_title= page_title;
    }

    /**
     *
     * @param bool
     */
    public void setPageTeam(boolean bool){
        isTeamPage=bool;
    }

    public void setMetadata(Metadata meta){
        about_metadata=meta;
    }

    public String getHTMLCode() {
        buildPage();
        return about_builder.toString();
    }

    private void buildPage() {
        buildHead();
        buildBody();

    }

    private void buildHead() {
        about_builder.append(SiteElements.getDoctypeHTML());
        about_builder.append("<head>");
        about_builder.append(SiteDecorations.setFavIcon());
        about_builder.append("<title>").append(title).append("</title>");
        if (about_metadata != null) {
            about_builder.append(about_metadata.getHTMLCode());
        }
        about_builder.append(AboutElements.getAboutBasicElements());
        about_builder.append("</head>");
    }

    private void buildBody() {
        about_builder.append("<body>");
        about_builder.append("<div id=\"header\">");
        about_builder.append("<div id=\"top_about\">");
        about_builder.append("<div class=\"left_zone\">").append("<a href=\"/about/\">"
                + "<img id=\"about_logo\" alt=\"about_logo\""
                + " src=\"/image/website/about/logo_about.png\"></a>").append("</div>");
        about_builder.append("<div class=\"right_zone\">").append(in_page_title).append("</div>");
        about_builder.append("</div>");

        about_builder.append("</div>");

        about_builder.append("<div id=\"content\">");
        about_builder.append("<div id=\"bottom_about\">");
        AboutMenu menu = AboutMenu.getMenu();
        menu.pageTeam(isTeamPage);
        about_builder.append("<div class=\"left_zone\">").append(menu.getHTMLCode())
                .append("</div>");
        about_builder.append("<div class=\"right_zone\">").append(bodyContent)
                .append("</div>");
        about_builder.append("</div>");
        about_builder.append("</div>");

        buildFooter();
        about_builder.append(GoogleAnalytics.getAnalyticsCode());
        about_builder.append("</body>");
    }

    /**
     * Set title of the AboutPage
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    private void buildFooter() {
        AboutFooter abFoot = AboutFooter.getInstance();
        about_builder.append("<div id=\"footer\">");
        about_builder.append(abFoot.getHTMLCode());
        about_builder.append("</div>");
    }

}
