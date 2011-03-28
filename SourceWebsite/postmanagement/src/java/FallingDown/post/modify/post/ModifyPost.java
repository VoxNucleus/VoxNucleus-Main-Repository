package FallingDown.post.modify.post;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.identification.Rights;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;

/**
 *
 * @author victork
 */
public class ModifyPost extends HttpServlet {


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
        Pass uPass = Pass.getPass(request);
        uPass.launchAuthentifiate();
        String postId = getPostId(request);
        if (!postId.isEmpty() && Rights.hashRightToModifyPost(uPass, postId)) {
            try {
                PostHash phash = new PostHash(Post.getPostFromDatabase(postId));
                PrintModifyPost modif_post = PrintModifyPost.getInstance(postId, uPass);
                modif_post.attachPostHash(phash);
                Browser.sendResponseToBrowser(request, response, modif_post);
            } catch (Exception ex) {
                sendErrorPage(uPass, request, response);
            }
        } else {
            sendErrorPage(uPass, request, response);
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
     *
     * @param request
     * @return postId
     */
    private String getPostId(HttpServletRequest request){
        return (request.getParameterValues("postId")==null)?"":request.getParameterValues("postId")[0];
        
    }

    private void sendErrorPage(Pass uPass, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        StandardOneColumnPage emptyidpage = StandardOneColumnPage.getInstance(request);
        emptyidpage.attachAuthentifiedPass(uPass);
        emptyidpage.setTitle("VoxNucleus : Erreur modification du noyau");
        emptyidpage.setContent("Nous sommes désolés mais il n'y aucun noyau qui "
                + "correspond à la description ou vous n'avez pas les droits pour "
                + "modifier ce noyau.");
        Browser.sendResponseToBrowser(request, response, emptyidpage);
    }

}
