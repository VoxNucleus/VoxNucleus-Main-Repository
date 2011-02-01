package FallingDown.www.latest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.util.PathParser;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.voxnucleus.sql.select.NucleusSELECTOperations;


/**
 *
 * @author victork
 */
public class Lastest extends HttpServlet {

    private static final String[] parser_list = {"category","sub_category","page"};
    private static final String[] defaultarguments={"Tout","Tout","0"};

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

        PathParser parser = new PathParser(parser_list, defaultarguments);
        parser.setPath(request.getPathInfo());
        HashMap<String, String> arguments = parser.getArguments();

        int page_number;
        try {
            page_number = Integer.parseInt(arguments.get("page"));
        } catch (NumberFormatException ex) {
            page_number = 0;
        }
        PrintLastest lastPrinter = new PrintLastest(arguments.get("category"), arguments.get("sub_category"),request,page_number);
        try {
            NucleusSELECTOperations sel_op = NucleusSELECTOperations.getInstance();
            List<String> post_list=sel_op.getNew(arguments.get("category"), arguments.get("sub_category"), page_number*10);
            lastPrinter.setContent(post_list);
            Browser.sendResponseToBrowser(request, response, lastPrinter.getPageResult());
        } catch (Exception ex) {
            Logger.getLogger(Lastest.class.getName()).log(Level.SEVERE, null, ex);
            ImportantErrorMessage(request,  response, ex);
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

    private void ImportantErrorMessage(HttpServletRequest request,
            HttpServletResponse reponse, Exception ex) throws IOException {
        StandardOneColumnPage major_error_page = StandardOneColumnPage.getInstance(request);
        major_error_page.setTitle("VoxNucleus : Erreur grave...");
        major_error_page.setContent("Nous sommes désolés mais une erreur interne "
                + "s'est produite.<br> " + " Vous pouvez rafraîchir cette page, les "
                + "choses seront peut être rétablies.<br>"
                + "Nous nous excusons pour cette erreur et nous travaillons à sa réparation !");
        Browser.sendResponseToBrowser(request, reponse, major_error_page.getHTMLCode());
    }
}
