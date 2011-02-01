package FallingDown.www.latest;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.boxes.BestBox;
import me.FallingDownLib.CommonClasses.www.boxes.ExplanationBox;
import me.FallingDownLib.CommonClasses.www.footer.Footer;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;

/**
 *
 * @author victork
 */
public class PrintLastest {

    private int page_number;
    private StringBuilder page_builder;
    private String category;
    private String sub_category;
    private HttpServletRequest request;
    private static String[] list_args = {"category", "sub_category", "page_number"};
    private String[] list_values;
    private Pass pass;

    public PrintLastest(String category, String sub_category,
            HttpServletRequest request, int page_number){
        this.category=category;
        this.sub_category=sub_category;
        page_builder = new StringBuilder();
        this.request=request;
        this.page_number=page_number;
        list_values= new String[3];
        list_values[0]=category;
        list_values[1]=sub_category;
        list_values[2]=Integer.toString(page_number);
        pass=Pass.getPass(request);
        pass.launchAuthentifiate();
    }

    /**
     * pass content to the page.
     * @param postId
     */

    public void setContent(List<String> post_list){
        ArrayList<String> arr_post_list = new ArrayList<String>(post_list);
        Metadata meta = Metadata.getInstance();
        meta.setDescription("Les derniers ajouts au site dans les catégories qui "
                + "vous intéressent.");
        meta.setAdditionnalKeywords("derniers, noyaux,"+category+","+sub_category);
        page_builder.append(SiteElements.getDoctypeHTML());
        page_builder.append("<html>");
        page_builder.append("<head>");
        page_builder.append(meta.getHTMLCode());
        page_builder.append(SiteDecorations.setFavIcon());
        page_builder.append("<title>").append(SiteElements
                .getFilterTitle(" Les derniers noyaux",category, sub_category )).append("</title>");
        pageinsertCSSStyle();
        insertJavascriptInclude();
        page_builder.append("</head>");
        page_builder.append("<body>");
        page_builder.append(SiteElements.displayMenu(category, sub_category, "latest",pass));
        page_builder.append("<div id=\"container\">");
        page_builder.append("<div id=\"content\">");
        page_builder.append(SiteElements.getPageSubtitle("Les derniers noyaux postés par les utilisateurs"));
        page_builder.append(SiteElements.insertListPosts(arr_post_list));
        page_builder.append(SiteElements.getPages("/lastest/" + category + "/" + sub_category, page_number));
        page_builder.append("</div>");
        pageInsertLateralRight();
        page_builder.append(SiteElements.getHiddenZone(list_args, list_values));
        page_builder.append("</div>");
        Footer footer = Footer.getInstance(pass, request);
        page_builder.append(footer.getHTMLCode());
        page_builder.append(GoogleAnalytics.getAnalyticsCode());
        page_builder.append(SiteElements.getsublogOverlay());
        page_builder.append("</body>");
        page_builder.append("</html>");

    }


    private void pageInsertLateralRight(){
        ExplanationBox box = ExplanationBox.getInstance(category, sub_category);
        BestBox best_box = BestBox.getInstance();
        page_builder.append("<div id=\"float_right\">");
        page_builder.append(best_box.getHTMLCode());
        page_builder.append(box.getHTMLCode());
        page_builder.append("</div>");

    }


    /**
     *
     * @return HTML code of the page
     */
    public String getPageResult(){
        pass.launchAuthentifiate();
        return page_builder.toString();
    }

    /**
     * insert CSS style
     */

    private void pageinsertCSSStyle() {
        page_builder.append(SiteDecorations.getExplanationBoxStyle());
        page_builder.append(SiteElements.getCommonCSSStyle());
        page_builder.append(SiteElements.getTwoColumnStyle());
        page_builder.append(SiteDecorations.getRankZoneStyle());
        page_builder.append(SiteDecorations.getOverlayStyle());
        page_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/post.css\">");
    }


    /**
     * Insert needed javascript entries
     */

    private void insertJavascriptInclude() {
        page_builder.append(SiteElements.getCommonScripts());
        page_builder.append("<script type=\"text/javascript\" src=\"/jsp/reloader/jloadLatestEntries.js\"></script>");
        page_builder.append(SiteElements.getJSReloaderCode("retrieveLastEntries", category, sub_category, null));
        page_builder.append("<script type=\"text/javascript\" src=\"/jsp/jloadPosts.js\"></script>");
        page_builder.append(SiteElements.getOverlayScript());
        page_builder.append(SiteElements.getExplanationBoxScript());
    }

}
