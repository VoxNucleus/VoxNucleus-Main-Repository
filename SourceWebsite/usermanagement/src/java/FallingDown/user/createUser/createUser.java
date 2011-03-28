package FallingDown.user.createUser;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.octo.captcha.service.CaptchaServiceException;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.FallingDownLib.CommonClasses.www.overlay.PrintQuickLogin;

/**
 *
 * @author victork
 */
public class createUser extends HttpServlet {


    private String captcha_session_ID;
    private Boolean isQuick;


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
        Boolean isResponseCorrect = Boolean.FALSE;
        captcha_session_ID = getSessionId(request);
        String answerToCaptcha = request.getParameter("j_captcha_response");
        try {
            isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captcha_session_ID,
                    answerToCaptcha);
        } catch (CaptchaServiceException e) {
            StandardOneColumnPage columnPage = StandardOneColumnPage.getInstance(request);
            columnPage.setTitle("VoxNucleus : Erreur serveur");
            columnPage.setContent("Erreur dans le système de captcha. "
                    + "Nous nous excusons pour le désagrément");
            Browser.sendResponseToBrowser(request, response, columnPage.getHTMLCode());
        }

        if (isResponseCorrect.booleanValue()) {
            try {
                User newUser = new User(request.getParameterMap(),isQuick);
                SaveUserToCassandra save = new SaveUserToCassandra(newUser);
                StandardOneColumnPage columnPage = StandardOneColumnPage.getInstance(request);
                columnPage.setContent("Le compte \"" + newUser.username + "\" vient d'etre créé.");
                columnPage.setTitle("VoxNucleus : Utilisateur créé");
                Browser.sendResponseToBrowser(request, response, columnPage.getHTMLCode());
            } catch (IncorrectUserInfo e) {
                StandardOneColumnPage columnPage = StandardOneColumnPage.getInstance(request);
                columnPage.setTitle("VoxNucleus : Erreur dans les informations");
                columnPage.setContent(e.toString());
                Browser.sendResponseToBrowser(request, response, columnPage.getHTMLCode());
            } catch (Exception e) {
                StandardOneColumnPage columnPage = StandardOneColumnPage.getInstance(request);
                columnPage.setTitle("VoxNucleus : Erreur dans les informations");
                columnPage.setContent("<h1>Erreur serveur</h1> Erreur serveur... ");
                Browser.sendResponseToBrowser(request, response, columnPage.getHTMLCode());
            }
        } else {
            printErrorCaptcha(request, response);
        }
    }

    /**
     * Print error if Captcha is wrong
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void printErrorCaptcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StandardOneColumnPage columnPage = StandardOneColumnPage.getInstance(request);
        columnPage.setTitle("VoxNucleus : Erreur dans le captcha");
        columnPage.setContent("Vous avez rentré un mauvais captcha.");
        Browser.sendResponseToBrowser(request, response, columnPage.getHTMLCode());
    }

    /**
     * 
     * @param request
     * @return session ID saved in request or simply the session ID of the current
     */
    private String getSessionId(HttpServletRequest request) {
        isQuick = Boolean.parseBoolean(request.getParameter(PrintQuickLogin.QUICK_FORM));
        if(isQuick.booleanValue()){
            return request.getParameter(PrintQuickLogin.ASSOCIATED_REQUEST);
        }else{
            return request.getSession().getId();
        }
    }
}
