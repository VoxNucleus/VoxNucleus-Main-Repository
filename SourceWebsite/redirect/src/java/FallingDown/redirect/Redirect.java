package FallingDown.redirect;

import FallingDown.redirect.exception.NoLinkAssociated;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class Redirect extends HttpServlet {
   

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
        LinkGrabber grabber=null;
        String key="";
        try {
            key=request.getPathInfo().substring(1);
            request.getPathTranslated();
            grabber = new LinkGrabber(key);
            response.sendRedirect(grabber.getLink());
        }catch(NoLinkAssociated ex){
            this.printNoLinkAssociatedPage(request, response, grabber.getLink());
        }
        catch (PoolExhaustedException ex) {

        } catch (TException ex) {
        } catch (NotFoundException ex) {
            StandardOneColumnPage notfoundPage = StandardOneColumnPage.getInstance(request);
            notfoundPage.setContent("Nous sommes désolés mais le lien associé à la clef "
                    + key + "n'a pas pu être trouvé dans nos bases de données.<br>"
                    + "Veuillez verifier les informations qui vous ont été données.");
            notfoundPage.setTitle("VoxNucleus : Erreur le lien n'existe pas !");
            Browser.sendResponseToBrowser(request, response, notfoundPage.getHTMLCode());
            printNotFoundPage(request,response);
        } catch (InvalidRequestException ex) {

        } catch (UnavailableException ex) {

        } catch (Exception ex) {

        }
    }



    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }
    /**
     * Displayan error page if no link has be found
     * @param request
     * @param response
     * @throws IOException
     */

    private void printNotFoundPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pass pass = Pass.getPass(request);
        pass.launchAuthentifiate();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println(SiteElements.getCommonCSSStyle());
            out.println(SiteElements.getCommonScripts());
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/menu.css\" />");
            out.println("<title>Non trouvé</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(SiteElements.displayBasicMenu(pass));
            out.println("<div id=\"content\">");
            out.println("Le liens associé à "+request.getPathInfo() +"n'a pas pu être trouvé");
            out.println("</div>");
            out.println(SiteElements.displayFooter());
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }


     private void printNoLinkAssociatedPage(HttpServletRequest request, HttpServletResponse response,String postId) throws IOException {
        Pass pass = Pass.getPass(request);
        pass.launchAuthentifiate();
         response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println(SiteElements.getCommonCSSStyle());
            out.println(SiteElements.getCommonScripts());
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/menu.css\" />");
            out.println("<title>Aucun lien n'est associé à cette adresse</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(SiteElements.displayBasicMenu(pass));
            out.println("<div id=\"content\">");
            out.println("Le liens associé à "+request.getPathInfo() +"n'a pas pu être trouvé");
            out.println("<a href=\"/post"+ postId + "\" >Voir le message original</a>");
            out.println("</div>");
            out.println(SiteElements.displayFooter());
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
}
