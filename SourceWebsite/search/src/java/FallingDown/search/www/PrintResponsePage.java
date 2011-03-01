package FallingDown.search.www;

import FallingDown.search.SearchOptions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.PageList;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class PrintResponsePage implements ToCodeConverter {

    private SearchOptions search_options;
    private StringBuilder response_page_builder;
    private Pass pass;
    private HttpServletRequest request;
    private SolrDocumentList listAnswers;

    public void attachSearchOptions(SearchOptions options) {
        search_options = options;
    }

    public void attachPass(Pass uPass) {
        pass = uPass;
    }

    public void attachRequest(HttpServletRequest uRequest){
        request=uRequest;
    }

    public void attachListAnswsers(SolrDocumentList u_ListAnswers){
        listAnswers=u_ListAnswers;
    }

    protected PrintResponsePage() {
        response_page_builder = new StringBuilder();
    }

    public static PrintResponsePage getInstance() {
        return new PrintResponsePage();
    }


    private void printResult() {

        response_page_builder.append("Nombre de résultats trouvés : ").append(listAnswers.getNumFound());
        for (int index = 0; index < listAnswers.size(); index++) {
            SolrDocument sDoc = (SolrDocument) listAnswers.get(index);
            try {
                Answer oneResult = new Answer(sDoc);
                response_page_builder.append(oneResult.toHTML());
            } catch (PostDoesNotExist ex) {
                //TODO change that
            } catch (PoolExhaustedException ex) {
            } catch (TException ex) {
            } catch (Exception ex) {
            }
        }
    }

    private String getSearchStyle() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/search/search.css\" >");
        return buffer.toString();
    }

    private String getPurgedParameter(String parameters) {
        Pattern p = Pattern.compile("&page=[^&]*");
        Matcher m = p.matcher(parameters);
        return m.replaceAll("");
    }

    public String getHTMLCode() {
        buildPage();
        return response_page_builder.toString();
    }

    private void buildPage() {
        buildHead();
        response_page_builder.append("<body>");
        buildBody();
        buildFooter();
        response_page_builder.append(GoogleAnalytics.getAnalyticsCode());
        response_page_builder.append("</body>");
        response_page_builder.append("</html>");
    }

    private void buildHead() {
        Metadata meta = Metadata.getInstance();
        response_page_builder.append("<html>");
        response_page_builder.append("<head>");
        response_page_builder.append(meta.getHTMLCode());
        response_page_builder.append(SiteElements.getOneColumnStyle());
        response_page_builder.append(SiteDecorations.setFavIcon());
        response_page_builder.append(getSearchStyle());
        response_page_builder.append(SiteElements.getCommonCSSStyle());
        response_page_builder.append(SiteElements.getCommonScripts());
        response_page_builder.append("<title>").append(search_options.getSearchRequest());
        response_page_builder.append(" : Recherche VoxNucleus</title>");
        response_page_builder.append("</head>");
    }

    private void buildBody() {
        response_page_builder.append(SiteElements.displayMenu("Tout", "Tout", "", pass));
        response_page_builder.append("<div id=\"container\">");
        response_page_builder.append("<div id=\"content\">");
        response_page_builder.append("<h1>Résultats de la recherche : </h1>");
        printResult();
        PageList p_list = PageList.getInstance(request.getRequestURI() + "?"
                + getPurgedParameter(request.getQueryString()) + "&page=", search_options.getPageNumber()
                , 6, (int)Math.ceil(listAnswers.getNumFound()/((float)10)));
        response_page_builder.append(p_list.getHTMLCode());
        response_page_builder.append("</div>");
        response_page_builder.append("</div>");
    }

    private void buildFooter() {
        response_page_builder.append(SiteElements.displayFooter(pass));
    }
}
