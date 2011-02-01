package FallingDown.www.best;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.util.PathParser;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.functions.bestbydate.RetrieveBestByDate;

/**
 *
 * @author victork
 */
public class Best extends HttpServlet {

    private static final String[] parser_list = {"category","sub_category","period","page"};
    private static final String[] defaultarguments={"Tout","Tout","Best24Hours","0"};
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
        PathParser parser = new PathParser(parser_list,defaultarguments);
        parser.setPath(request.getPathInfo());
        HashMap<String, String> arguments = parser.getArguments();
        int page_number;
        try {
            page_number = Integer.parseInt(arguments.get("page"));
        } catch (NumberFormatException ex) {
            page_number = 0;
        }
        PrintBest bestPrinter = new PrintBest(arguments.get("category"),arguments.get("sub_category"),
                arguments.get("period"),request,page_number);
        response.setContentType("text/html;charset=UTF-8");

        RetrieveBestByDate best = new RetrieveBestByDate(arguments.get("period"),page_number*10,
                10,arguments.get("category"),arguments.get("sub_category"));
        try {
            bestPrinter.setContent(best.getKeys());
            Browser.sendResponseToBrowser(request, response, bestPrinter.getPageResult());
        } catch (Exception ex) {
            Logger.getLogger(Best.class.getName()).log(Level.SEVERE, null, ex);
            ImportantErrorMessage(request,response,ex);
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

    /**
     * Display an error message
     * @param request
     * @param reponse
     * @param ex
     * @throws IOException
     */
    private void ImportantErrorMessage(HttpServletRequest request, HttpServletResponse reponse,Exception ex) throws IOException{
        StandardOneColumnPage major_error_page= StandardOneColumnPage.getInstance(request);
        major_error_page.setTitle("VoxNucleus : Erreur grave...");
        major_error_page.setContent("Nous sommes désolés mais une erreur interne "
                + "s'est produite.<br> " + " Vous pouvez rafraîchir cette page, les "
                + "choses seront peut être rétablies.<br>"
                + "Nous nous excusons pour cette erreur et nous travaillons à sa réparation !");
        Browser.sendResponseToBrowser(request, reponse, major_error_page.getHTMLCode());
    }

}
