package FallingDown.search;

import FallingDown.search.exception.InvalidSearchRequestException;
import FallingDown.search.www.PrintResponsePage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.search.ConnectionToSolR;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author victork
 */
public class search extends HttpServlet {

    private String searchRequest;
    private int query_start;

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Pass pass = Pass.getPass(request);
        pass.launchAuthentifiate();

        try {
            SearchOptions options = SearchOptions.getInstance(request);
            options.processRequest();
            SolrServer server = ConnectionToSolR.openConnectionToSolR();
            DoSearch searchQ = null;
            try {
                searchQ = new DoSearch(server, options.getSearchRequest());
                searchQ.attachSearchOptions(options);
                searchQ.launchSearch();
                PrintResponsePage pageSearch = PrintResponsePage.getInstance();
                pageSearch.attachListAnswsers(searchQ.getResults());
                pageSearch.attachRequest(request);
                pageSearch.attachSearchOptions(options);
                pageSearch.attachPass(pass);
                Browser.sendResponseToBrowser(request, response, pageSearch);
            } catch (SolrServerException ex) {
                Logger.getLogger(search.class.getName()).log(Level.SEVERE, null, ex);
                StandardOneColumnPage serverError = StandardOneColumnPage.getInstance(request);
                serverError.setTitle("VoxNucleus : Erreur recherche");
                serverError.setContent("Nous sommes désolés, la recherche n'est pas "
                        + "disponible pour le moment. <br>"
                        + "Vous pouvez réessayer dans quelques temps, nous travaillons à"
                        + " résoudre ce problème aussi vite que possible.");
                Browser.sendResponseToBrowser(request, response, searchRequest);
            }
        } catch (InvalidSearchRequestException ex) {
            StandardOneColumnPage serverError = StandardOneColumnPage.getInstance(request);
            serverError.setTitle("VoxNucleus : Erreur recherche");
            serverError.setContent("Nous sommes désolés mais la recherche ne "
                    + "peut pas aboutir pour la raison suivante : <br>"
                    + ex.getReason());
            Browser.sendResponseToBrowser(request, response, serverError.getHTMLCode());
        }
    }

     protected void print(HttpServletRequest request, HttpServletResponse response,String toInsert)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(toInsert);
        } finally {
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    protected void getParameters(HttpServletRequest request) throws InvalidSearchRequestException {
        searchRequest = request.getParameter("q");
        if (searchRequest.length() < 2) {
            throw new InvalidSearchRequestException("La recherche est trop courte."
                    + " Pour être pertinente la recherche doit faire au moins deux "
                    + "caractères.");
        }
        int pageNumber;
        if (request.getParameter("page") != null) {

            try {
                pageNumber = Integer.parseInt(request.getParameter("page"));
                if (pageNumber < 0) {
                    throw new InvalidSearchRequestException("La page demandée est"
                            + " négative. Merci de reformuler votre recherche.");
                }
            } catch (NumberFormatException ex) {
                throw new InvalidSearchRequestException("Vous avez demandé la page"
                        + " numéro  "
                        + "\"" + request.getParameter("page") + "\". Cela ne correspond "
                        + " pas à un numéro de page valide. <br>"
                        + "Merci de reformuler");
            }
        } else {
            pageNumber = 1;
        }
        query_start = (pageNumber - 1) * 10;
    }

}
