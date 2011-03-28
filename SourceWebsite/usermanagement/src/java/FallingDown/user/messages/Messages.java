package FallingDown.user.messages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;

/**
 *
 * @author victork
 */
public class Messages extends HttpServlet {
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
        Pass messagePass = Pass.getPass(request);
        messagePass.launchAuthentifiate();

        if(messagePass.getIsAuthentified()){
            PrintMessages printMessages =PrintMessages.getMessages(request);
            printMessages.setPass(messagePass);
            Browser.sendResponseToBrowser(request, response, printMessages);
        }else{
            response.sendRedirect("/usermanagement/login.jsp");
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


}
