package FallingDown.post.comment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Comment;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectCommentException;
import me.FallingDownLib.CommonClasses.FallingDownSession;
import me.FallingDownLib.CommonClasses.util.CookiesUtil;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.prettyprint.cassandra.service.PoolExhaustedException;

/**
 * Method that
 * @author victork
 */
public class ValidateComment extends HttpServlet {

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
        boolean authentified = false;
        authentified = FallingDownSession.authentifiateFromData(request);

        if (authentified) {
            try {
                Comment newComment = new Comment((HashMap<String, String[]>) request.getParameterMap(),
                        CookiesUtil.findUsernameFromCookie(request.getCookies()));
               newComment.validateComment();
                SaveCommentToCassandra save = new SaveCommentToCassandra(newComment);
                response.sendRedirect("/post/"+newComment.getRelatedPostId());
            } catch (IncorrectCommentException ex) {
                
                StandardOneColumnPage errorPage=StandardOneColumnPage.getInstance(request);
                errorPage.setTitle("VoxNucleus - Erreur dans le commentaire.");
                errorPage.setContent("<h1> Erreur dans le commentaire </h1> Nous sommes désolés"
                        + "le commentaire n'a pas pu être validé. "+ex.giveReason());
                sendResponseToBrowser(request,response,errorPage.getHTMLCode());
                Logger.getLogger(ValidateComment.class.getName()).log(Level.SEVERE, null, ex);

            } catch (PoolExhaustedException ex) {
                //TODO : Implement
                PrintWriter out = response.getWriter();
                out.print(ex.toString());
                Logger.getLogger(ValidateComment.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                //TODO : Implement
                PrintWriter out = response.getWriter();
                out.print(ex.toString());
                Logger.getLogger(ValidateComment.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else {
            response.sendRedirect("/usermanagement/login.jsp");
        }

    }

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


}
