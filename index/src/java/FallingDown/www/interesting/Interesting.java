package FallingDown.www.interesting;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.util.PathParser;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.functions.interestingposts.InterestingResults;

/**
 *
 * @author victork
 */
public class Interesting extends HttpServlet {

    private static final String[] parser_list = {"category", "sub_category", "page"};
    private static final String[] defaultarguments = {"Tout", "Tout", "0"};


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
        PrintInteresting interestingPage = new PrintInteresting(arguments.get("category"),
                arguments.get("sub_category"),request,page_number);
        InterestingResults results=InterestingResults.getList(arguments.get("category"), arguments.get("sub_category"));
        interestingPage.setContent(results.getList(page_number*10, (page_number+1)*10));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            out.print(interestingPage.getPageResult());
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

        private void OverchargeErrorMessage(HttpServletRequest request, HttpServletResponse reponse,Exception ex) throws IOException{
        StandardOneColumnPage major_error_page= StandardOneColumnPage.getInstance(request);
        major_error_page.setTitle("VoxNucleus : Serveur surchargé...");
        major_error_page.setContent("Le serveur est surchargé pour le moment, "
                + "merci de réessayer plus tard.<br>"
                + "Nous nous excusons pour cette erreur nous travaillons à l'amélioration "
                + "de nos services.");
        Browser.sendResponseToBrowser(request, reponse, major_error_page.getHTMLCode());
    }


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
