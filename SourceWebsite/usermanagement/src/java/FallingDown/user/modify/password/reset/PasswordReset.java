package FallingDown.user.modify.password.reset;

import FallingDown.user.modify.password.reset.www.PrintPasswordReset;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
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
public class PasswordReset extends HttpServlet {
   

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
        PrintPasswordReset page_pass_reset = PrintPasswordReset.getInstance(request);
        Browser.sendResponseToBrowser(request, response, page_pass_reset);
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

        String username = getUsername(request);
        String email = getEmail(request);
        if(!(username.isEmpty() || email.isEmpty())){
            PasswordReseter p_reseter = PasswordReseter.getReseter(username, email);
            try {
                p_reseter.reset();
                printSuccessPage(request,response,email);
            } catch (IncorrectUserInfo ex) {
                printErrorPage(request,response,ex.toString());
                Logger.getLogger(PasswordReset.class.getName()).log(Level.SEVERE, null, ex);
            } catch(MessagingException ex){
                printErrorPage(request,response,"Le mail n'a pas pu être envoyé, "
                        + "c'est de notre faute...<br>"
                        + "Nous travaillons à réparer l'erreur aussi vite que "
                        + "possible.");
                Logger.getLogger(PasswordReset.class.getName()).log(Level.SEVERE, null, ex);
            }catch (Exception ex) {
                printErrorPage(request,response,"Une erreur de type inconnue (probablement "
                        + "base de données) s'est produite. Nous sommes désolés "
                        + "pour le désagrément occasionné.");
                Logger.getLogger(PasswordReset.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            printErrorPage(request,response,"Vous n'avez pas rempli un des deux informations"
                    + " demandées. Merci de revenir en arrière et de refaire "
                    + "l'opération.");
        }
        
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

    private void printSuccessPage(HttpServletRequest request,HttpServletResponse response,String email) throws IOException{
        StandardOneColumnPage error_page = StandardOneColumnPage.getInstance(request);
        error_page.setTitle("VoxNucleus : Succès !");
        StringBuilder content_builder = new StringBuilder();
        content_builder.append("<h1>La réinitialisation du mot de passe s'est bien déroulée </h1>");
        content_builder.append("Vous allez bientôt recevoir un mail à l'adresse <em>")
                .append(email).append("</em>. La suite des instructions vous sera communiquée.");
        error_page.setContent(content_builder.toString());
        Browser.sendResponseToBrowser(request,response, error_page);
    }

    private String getEmail(HttpServletRequest request){
        return (request.getParameterValues(UserFields.HTTP_EMAIL)==null)
                ?"":request.getParameterValues(UserFields.HTTP_EMAIL)[0];
    }


    private String getUsername(HttpServletRequest request){
        return (request.getParameterValues(UserFields.HTTP_LOGIN_USERNAME)==null)
                ?"":request.getParameterValues(UserFields.HTTP_LOGIN_USERNAME)[0];
    }



}
