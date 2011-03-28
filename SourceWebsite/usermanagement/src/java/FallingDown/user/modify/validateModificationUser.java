package FallingDown.user.modify;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Exceptions.FileIsNotAnImage;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class validateModificationUser extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");
        Pass modifPass = Pass.getPass(request);
        modifPass.launchAuthentifiate();
        if (modifPass.getIsAuthentified() && ServletFileUpload.isMultipartContent(request)) {
            UserModificator modificator = UserModificator.getInstance(modifPass.getUsername(),request, response);
            try {
                modificator.saveModifications();
                 StandardOneColumnPage incorrectInfoPage = StandardOneColumnPage.getInstance(request);
                incorrectInfoPage.setTitle("VoxNucleus - Modification(s) enregistrée(s)");
                incorrectInfoPage.setContent("Les informations concernant votre compte ont été modifée avec succès"
                        + "<br> <a href=\"/\"> Revenir au à la page principale </a>  "
                        + "<br> <a href=\"/user/"+modifPass.getUsername()+"\" > Voir son compte </a>");
                Browser.sendResponseToBrowser(request, response, incorrectInfoPage.getHTMLCode());

            } catch (IncorrectUserInfo ex) {
                StandardOneColumnPage incorrectInfoPage = StandardOneColumnPage.getInstance(request);
                incorrectInfoPage.setTitle("VoxNucleus - Erreur dans les informations entrées");
                incorrectInfoPage.setContent("Nous sommes désolés mais les informations que vous tentez de modifier ne"
                        + " sont pas correctes. Les erreurs sont listés plus bas, merci de revenir en arrière "
                        + "et de les corriger.<br> " +"<b>" +ex.toString()+"</b>");
                Browser.sendResponseToBrowser(request, response, incorrectInfoPage.getHTMLCode());
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotFoundException ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileIsNotAnImage ex) {
                 StandardOneColumnPage incorrectInfoPage = StandardOneColumnPage.getInstance(request);
                incorrectInfoPage.setTitle("VoxNucleus - Erreur dans l'image");
                incorrectInfoPage.setContent("Nous sommes désolés mais le fichier fourni "
                        + "n'est pas compatible. Cela peut être dû au fait que le "
                        + "fichier n'est pas une image ou certaines images ne sont pas "
                        + "encore prises en charge par nos serveurs. <br> Merci de choisir un "
                        + "autre fichier."
                        + " <br> " +"<b>" +ex.toString()+"</b>");
                Browser.sendResponseToBrowser(request, response, incorrectInfoPage.getHTMLCode());
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(validateModificationUser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
        }


    }


}
