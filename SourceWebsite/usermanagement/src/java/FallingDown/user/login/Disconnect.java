package FallingDown.user.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.SessionConnector;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.util.CookiesUtil;

/**
 *
 * @author victork
 */
public class Disconnect extends HttpServlet {

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
        if (pass.getIsAuthentified()) {
            SessionConnector sessionC = SessionConnector.getConnector(CookiesUtil.findStoredSessionFromCookie(request.getCookies()));
            sessionC.deleteSession();
        }
        response.sendRedirect("/");
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
