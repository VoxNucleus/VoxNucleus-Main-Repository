package FallingDown.post.modify.post;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostInfo;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.identification.Rights;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;

/**
 *
 * @author victork
 */
public class DoModify extends HttpServlet {

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
        Pass uPass = Pass.getPass(request);
        uPass.launchAuthentifiate();
        String postId = getPostId(request);
        if (uPass.getIsAuthentified() && Rights.hashRightToModifyPost(uPass, postId)) {
            PostModificator pModificator = PostModificator.getInstance(request, response, postId);
            try {
                pModificator.saveModifications();
                StandardOneColumnPage noRightPage = StandardOneColumnPage.getInstance(request);
            noRightPage.attachAuthentifiedPass(uPass);
            noRightPage.setTitle("VoxNucleus : Les modifications ont été effectuées");
            noRightPage.setContent("<h1>Les modifications ont été effectuées avec succès</h1>"
                    + "Les changements que vous souhaitiez apportées ont été enregistrées "
                    + "avec succès");
            Browser.sendResponseToBrowser(request, response, noRightPage);
            } catch (IncorrectPostInfo ex) {
                Logger.getLogger(DoModify.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(DoModify.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            StandardOneColumnPage noRightPage = StandardOneColumnPage.getInstance(request);
            noRightPage.attachAuthentifiedPass(uPass);
            noRightPage.setTitle("VoxNucleus : Erreur vous ne pouvez pas faire ça");
            noRightPage.setContent("<h1>Vous n'avez pas les droits nécessaires</h1>"
                    + "Vous ne pouvez pas modifier un noyau si vous n'en êtes pas le "
                    + "propriétaire...");
            Browser.sendResponseToBrowser(request, response, noRightPage);

        }
    }

    /**
     *
     * @param request
     * @return the id of the post
     */

    private String getPostId(HttpServletRequest request) {
        return (request.getParameterValues(PostFields.HTTP_KEY) != null)
                ? request.getParameterValues(PostFields.HTTP_KEY)[0] : "";

    }
}
