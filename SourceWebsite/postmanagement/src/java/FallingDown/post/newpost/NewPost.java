package FallingDown.post.newpost;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.www.Browser;

/**
 *
 * @author victork
 */
public class NewPost extends HttpServlet {


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
        PostFiller postfill= PostFiller.getInstance(request);
        postfill.launchParameterRerieve();
        PrintNewPost newpost_Printer= PrintNewPost.getInstance(request);
        newpost_Printer.attachPostFiller(postfill);
        Browser.sendResponseToBrowser(request,response,newpost_Printer.getHTMLCode());
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
        request.setCharacterEncoding("UTF-8");
        PrintNewPost newpost_Printer= PrintNewPost.getInstance(request);
        PostFiller postfill= PostFiller.getInstance(request);
        postfill.setIsPOST(true);
        postfill.launchParameterRerieve();
        newpost_Printer.attachPostFiller(postfill);
        Browser.sendResponseToBrowser(request,response,newpost_Printer.getHTMLCode());
    }



}
