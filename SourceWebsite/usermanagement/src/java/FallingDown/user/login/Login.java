package FallingDown.user.login;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import me.FallingDownLib.CommonClasses.FallingDownSession;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.util.CookiesUtil;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.UnavailableException;

/**
 *
 * @author victork
 */
public class Login extends HttpServlet {

    private static final int COOKIE_MAX_INTERVAL = 1000;
    private static final int COOKIE_MAX_LIFETIME = 3600 * 24 * 30;

    private String whereFrom;

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
        Identification identif = new Identification(request.getParameter(UserFields.HTTP_LOGIN_USERNAME), request.getParameter(UserFields.HTTP_PASSWORD));
        if (identif.isValid()) {
            HttpSession session = request.getSession();
            setCookies(request, session, response);
            Calendar cal = Calendar.getInstance();
            FallingDownSession fdSession = new FallingDownSession(session.getId(),
                    request.getParameter(UserFields.HTTP_LOGIN_USERNAME), cal.getTimeInMillis() + COOKIE_MAX_LIFETIME * 1000l, request.getRemoteAddr());
            try {
                fdSession.saveSessionToDabase();
                setWhereFrom(request);
                response.sendRedirect(whereFrom);
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotFoundException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidRequestException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnavailableException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            StandardOneColumnPage loginErrorPage = StandardOneColumnPage.getInstance(request);
            loginErrorPage.setTitle("VoxNucleus : ");
            loginErrorPage.setContent("Erreur dans la combinaison utilisateur/mot de passe.<br>"
                    + "Vous pouvez revenir en arrière et réessayer ou contacter "
                    + "un administrateur si vous êtes certains de ne pas vous être "
                    + "trompé.");
            Browser.sendResponseToBrowser(request, response, loginErrorPage.getHTMLCode());
        }
    }

    private void setCookies(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        session.setMaxInactiveInterval(COOKIE_MAX_INTERVAL);
        Cookie httpsession = new Cookie(CookiesUtil.COOKIE_STORED_SESSION, session.getId());
        Cookie cUsername = new Cookie(CookiesUtil.COOKIE_USERNAME,
                request.getParameter(UserFields.HTTP_LOGIN_USERNAME));
        httpsession.setPath("/");
        cUsername.setPath("/");

        Cookie jSessionCookie = new Cookie(CookiesUtil.COOKIE_JSESSION,session.getId());
        jSessionCookie.setPath("/");

        if (isLongSession(request)) {
            httpsession.setMaxAge(COOKIE_MAX_LIFETIME);
            cUsername.setMaxAge(COOKIE_MAX_LIFETIME);
        }

        response.addCookie(jSessionCookie);
        response.addCookie(cUsername);
        response.addCookie(httpsession);
    }

    /**
     * Determines if the session must be long of not
     * @param request
     * @return
     */
    private boolean isLongSession(HttpServletRequest request) {
        String long_session = request.getParameter("long_session");
        if (long_session != null) {
            return Boolean.parseBoolean(long_session);
        } else {
            return false;
        }
    }

    private void setWhereFrom(HttpServletRequest request){
        String[] tempWhereFrom=request.getParameterValues("wherefrom");
        if(tempWhereFrom==null){
            whereFrom="/";
        }else{
            whereFrom=tempWhereFrom[0];
        }
    }
}
