package FallingDown.post.viewPost;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.identification.Rights;
import me.FallingDownLib.CommonClasses.www.Browser;

/**
 *
 * @author victork
 */
public class ViewPost extends HttpServlet {

    private boolean is_comment_static=false;
    private int comment_number=50;
    private String postId;

    public static final String STATIC_COMMENT="static";
    public static final String COMMENT_NUMBER="comment";
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
        response.setContentType("text/html;charset=UTF-8");
        Pass viewPass=Pass.getPass(request);
        viewPass.launchAuthentifiate();
        analyzeParameters(request);
        PrintPost postToPrint = PrintPost.getInstance(request, postId);
        postToPrint.setIsStatic(is_comment_static);
        postToPrint.setCommentNumber(comment_number);
        postToPrint.setCanBeModified(Rights.hashRightToModifyPost(viewPass, postId));
        Browser.sendResponseToBrowser(request, response, postToPrint.getHTMLCode());

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
     * Get the parameters
     * @param request
     */

    private void analyzeParameters(HttpServletRequest request){
        is_comment_static=Boolean.parseBoolean(request.getParameter(STATIC_COMMENT));
        try {
            comment_number = Integer.parseInt(request.getParameter(COMMENT_NUMBER));
        } catch (NumberFormatException ex) {
            comment_number=50;
        }
        postId=request.getPathInfo().substring(1);
    }


}
