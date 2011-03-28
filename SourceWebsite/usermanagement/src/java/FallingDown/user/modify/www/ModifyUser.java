package FallingDown.user.modify.www;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Exceptions.UserDoesNotExist;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;

import org.apache.cassandra.thrift.NotFoundException;

/**
 *
 * @author victork
 */
public class ModifyUser extends HttpServlet {
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

        Pass modifyPass = Pass.getPass(request);
        modifyPass.launchAuthentifiate();
        if (modifyPass.getIsAuthentified()) {
            PrintModifyUser modUser;
            try {
                modUser = PrintModifyUser.getInstance(request, modifyPass.getUsername());
                Browser.sendResponseToBrowser(request, response, modUser.getHTMLCode());
            } catch (UserDoesNotExist ex) {
                Browser.sendResponseToBrowser(request, response, PrintModifyUser.notConnected(request));
            } catch (NotFoundException ex) {
                Logger.getLogger(ModifyUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ModifyUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.sendRedirect("/usermanagement/login.jsp");
        }
    }

    /**
     *
     * @param request
     * @param response
     * @param resultToSend
     * @throws IOException
     */
    private void sendResponseToBrowser(HttpServletRequest request,
            HttpServletResponse response, String resultToSend) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(resultToSend);
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


}
