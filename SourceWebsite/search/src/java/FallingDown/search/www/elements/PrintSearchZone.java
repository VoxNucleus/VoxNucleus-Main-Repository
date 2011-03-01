package FallingDown.search.www.elements;

import FallingDown.search.SearchOptions;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintSearchZone implements ToCodeConverter {
    private HttpServletRequest request;
    private StringBuilder search_zone_build;
    private SearchOptions previous_search_options;

    protected PrintSearchZone(HttpServletRequest in_request){
        request =in_request;
        search_zone_build = new StringBuilder();
    }

    public static PrintSearchZone getInstance(HttpServletRequest in_request){
        return new PrintSearchZone(in_request);
    }

    public String getHTMLCode() {
        buildSearchZone();
        return search_zone_build.toString();
    }

    private void buildSearchZone() {
        search_zone_build.append("<div id=\"big_search_zone\">");
        search_zone_build.append("<form id=\"form_search_zone\">");
        search_zone_build.append("<input id=\"search_input\" name=\"q\" "
                + "value=\"").append(previous_search_options.getSearchRequest()).append("\" "
                + ">");
        search_zone_build.append("<button id=\"big_search_button\" "
                + "type=\"submit\">Recherche VoxNucleus</button>");

        search_zone_build.append("</form>");
        search_zone_build.append("</div>");
    }
}
