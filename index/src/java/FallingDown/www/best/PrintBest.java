package FallingDown.www.best;

import java.util.ArrayList;
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
public class PrintBest {

    private static String[] list_args = {"category", "sub_category", "time_filter", "page_number"};
    private String[] list_values;
    private StringBuilder page_builder;
    private String category;
    private String sub_category;
    private String time_filter;
    private HttpServletRequest request;
    private int page_number;
    private Pass pass;

    /**
     * Builder
     * @param category
     * @param sub_category
     * @param time_filter
     * @param request
     * @param page_number
     */
    public PrintBest(String category, String sub_category, String time_filter,
            HttpServletRequest request, int page_number) {
        this.category = category;
        this.sub_category = sub_category;
        this.time_filter = time_filter;
        page_builder = new StringBuilder();
        this.request = request;
        this.page_number = page_number;
        list_values = new String[4];
        list_values[0] = category;
        list_values[1] = sub_category;
        list_values[2] = getValue();
        list_values[3] = Integer.toString(page_number);
        pass = Pass.getPass(request);
        pass.launchAuthentifiate();
    }

    /**
     * Set inner content, the ArrayList may not be null !
     * @param postId
     */
    public void setContent(ArrayList<String> postList) {
        Metadata metadata= Metadata.getInstance();
        metadata.setDescription("Le meilleur de l'actualité trié par catégorie.");
        metadata.setAdditionnalKeywords("meilleur," + time_filter+","+category+","+sub_category);
        page_builder.append(SiteElements.getDoctypeHTML());
        page_builder.append("<html>");
        page_builder.append("<head>");
        page_builder.append(SiteDecorations.setFavIcon());
        page_builder.append(metadata.getHTMLCode());
        page_builder.append("<title>").append(SiteElements
                .getFilterTitle(" Le meilleur des noyaux ",category, sub_category )).append("</title>");
        pageinsertCSSStyle();
        insertJavascriptInclude();
        page_builder.append("</head>");
        page_builder.append("<body>");
        page_builder.append(SiteElements.displayMenu(category, sub_category, time_filter,pass));
        page_builder.append("<div id=\"container\">");
        page_builder.append("<div id=\"content\">");
        page_builder.append(SiteElements.getPageSubtitle("Le meilleur de l'actualité..."));
        page_builder.append(SiteElements.insertListPosts(postList));
        page_builder.append(SiteElements.getPages("/best/" + category + "/" + sub_category+"/" +time_filter, page_number));
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

    /**
     *
     * @return the page code
     */
    public String getPageResult() {
        return page_builder.toString();
    }

    /**
     * insert general and special styles
     */
    private void pageinsertCSSStyle() {
        page_builder.append(SiteDecorations.getExplanationBoxStyle());
        page_builder.append(SiteElements.getCommonCSSStyle());
        page_builder.append(SiteElements.getTwoColumnStyle());
        page_builder.append(SiteDecorations.getRankZoneStyle());
        page_builder.append(SiteDecorations.getOverlayStyle());
        page_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/post.css\">");
    }

    private void insertJavascriptInclude() {
        page_builder.append(SiteElements.getCommonScripts());
        page_builder.append(SiteElements.getOverlayScript());
        page_builder.append("<script type=\"text/javascript\" src=\"/jsp/reloader/jloadBest.js\"></script>");
        page_builder.append("<script type=\"text/javascript\" src=\"/jsp/plugins/jquery.text-overflow.min.js\"></script>");
        page_builder.append(SiteElements.getJSReloaderCode("retrieveBestInPeriod", category, sub_category, null));
        page_builder.append("<script type=\"text/javascript\" src=\"/jsp/jloadPosts.js\"></script>");
        page_builder.append(SiteElements.getExplanationBoxScript());
    }

    private void pageInsertLateralRight() {
        ExplanationBox box = ExplanationBox.getInstance(category, sub_category);
        BestBox best_box = BestBox.getInstance();
        page_builder.append("<div id=\"float_right\">");
        page_builder.append(best_box.getHTMLCode());
        page_builder.append(box.getHTMLCode());
        page_builder.append("</div>");

    }

    /**
     *
     * @return value that must be put next to the javascript
     */
    private String getValue() {
        if (time_filter.equals("Best24Hours")) {
            return "24h";
        } else if (time_filter.equals("Best1Week")) {
            return "1week";
        } else if (time_filter.equals("Best1Month")) {
            return "1month";
        } else if (time_filter.equals("Best1Year")) {
            return "1year";
        }
        return "error";
    }

    
}
