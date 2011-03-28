package FallingDown.viewUser;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.util.CookiesUtil;

/**
 *
 * @author victork
 */
public class ViewUser extends HttpServlet {

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
        String userId = getUser(request,response,request.getPathInfo());
        if(userId.isEmpty()){
            response.sendRedirect("/usermanagement/subscribe.jsp");
        } else {
            PrintUser user = new PrintUser(userId,request);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.print(user.getPageResult());
            } finally {
                out.close();
            }
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


    private String getUser(HttpServletRequest request,HttpServletResponse response,String path){
        if(!(path.length()>1)){
            String foundUsername=CookiesUtil.findUsernameFromCookie(request.getCookies());
            return foundUsername;

        }else
        {
            return path.substring(1);
        }
    }

}
