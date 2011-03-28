package FallingDown.user.modify.password.reset;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;

/**
 *
 * @author victork
 */
public class PasswordActivate extends HttpServlet {

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
        String userId=getUserId(request);
        String rand_seq=getRandomSeq(request);
        if (!(userId.isEmpty() || rand_seq.isEmpty())) {
            PasswordReactivator pass_reactivator = PasswordReactivator.getPassReactivator(rand_seq, userId);
            try {
                pass_reactivator.doReactivate();
                printSuccessPage(request,response);
            } catch (IncorrectUserInfo ex) {
                printErrorPage(request,response,ex.toString());
                Logger.getLogger(PasswordActivate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                printErrorPage(request,response,"Une erreur inconnue s'est produire."
                        + " Nous allons travailler à la résoudre, merci de réessayer dans "
                        + "quelques temps.");

                Logger.getLogger(PasswordActivate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            printErrorPage(request,response,"Des données nécessaires sont manquantes. "
                    + "Par conséquent le serveur ne peut pas continuer à procéder "
                    + "à votre requête...");
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

    private void printErrorPage(HttpServletRequest request,HttpServletResponse response, String message) throws IOException{
        StandardOneColumnPage error_page = StandardOneColumnPage.getInstance(request);
        error_page.setTitle("VoxNucleus : Erreur");
        StringBuilder content_builder = new StringBuilder();
        content_builder.append("<h1>La réinitialisation du mot de passe a échoué </h1>");
        content_builder.append("Lors de la réinitialisation du mot de passe de "
                + "l'utilisateur que vous avez indiqué, l'erreur suivante s'est "
                + "produite :<br>");
        content_builder.append("<b>").append(message).append("</b>");
        error_page.setContent(content_builder.toString());
        Browser.sendResponseToBrowser(request,response, error_page);
    }

    private void printSuccessPage(HttpServletRequest request,HttpServletResponse response) throws IOException{
        StandardOneColumnPage error_page = StandardOneColumnPage.getInstance(request);
        error_page.setTitle("VoxNucleus : Succès !");
        StringBuilder content_builder = new StringBuilder();
        content_builder.append("<h1>Le mot de passe vient d'être réinitialisé </h1>");
        content_builder.append("Un email contenant le nouveau mot de passe va "
                + "être envoyé dans les moments qui suivent.");
        error_page.setContent(content_builder.toString());
        Browser.sendResponseToBrowser(request,response, error_page);
    }


    private String getUserId(HttpServletRequest request){
        return (request.getParameterValues(UserFields.HTTP_USERNAME)==null)
                ?"":request.getParameterValues(UserFields.HTTP_USERNAME)[0];
    }

    private String getRandomSeq(HttpServletRequest request){
        return (request.getParameterValues(UserFields.HTTP_PASSWORD_RESET)==null)
                ?"":request.getParameterValues(UserFields.HTTP_PASSWORD_RESET)[0];
    }


}
