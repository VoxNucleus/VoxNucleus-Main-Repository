package me.FallingDownLib.CommonClasses.identification;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.FallingDownSession;
import me.FallingDownLib.CommonClasses.util.CookiesUtil;

/**
 * MAJOR CLASS 
 * @author victork
 */
public class Pass {

    public static final int I_ROLE_NONE=0;
    public static final int I_ROLE_USER=1;
    public static final int I_ROLE_MANAGER=2;
    public static final int I_ROLE_ADMINISTRATOR=3;

    public static final String S_ROLE_NONE="none";
    public static final String S_ROLE_USER="user";
    public static final String S_ROLE_MANAGER="manager";
    public static final String S_ROLE_ADMINISTRATOR="administrator";

    private String username;
    private String sessionId;
    private boolean isAuthentified;
    private int role_id;
    private HttpServletRequest associatedRequest;

    /**
     * Constructor of the pass
     * @param request
     */
    private Pass(HttpServletRequest request){
        associatedRequest=request;
        isAuthentified=false;
    }

    /**
     * 
     * @param request
     * @return a Pass
     */
    public static Pass getPass(HttpServletRequest request){
        return new Pass(request);
    }

    /**
     * Go & check that all provided informations are correct.
     * Without that authentification is not going to be checked
     */
    public void launchAuthentifiate(){
        if(FallingDownSession.authentifiateFromData(associatedRequest)){
            isAuthentified=true;
            username=CookiesUtil.findUsernameFromCookie(associatedRequest.getCookies());
            sessionId=CookiesUtil.findStoredSessionFromCookie(associatedRequest.getCookies());
            UserConnector uConnector= UserConnector.getInstance(username);
            getRoleNumber(uConnector.getRole());
        }else{
            isAuthentified=false;
        }
    }

    /**
     *
     * @param foundRole
     */
    private void getRoleNumber(String foundRole){
        if(foundRole.equals(S_ROLE_USER)){
            role_id=I_ROLE_USER;
        }else if(foundRole.equals(S_ROLE_NONE)){
            role_id=I_ROLE_NONE;
        }else if(foundRole.equals(S_ROLE_MANAGER)){
            role_id=I_ROLE_MANAGER;
        }else if(foundRole.equals(S_ROLE_ADMINISTRATOR)){
            role_id=I_ROLE_ADMINISTRATOR;
        }else{
            role_id=I_ROLE_NONE;
        }
    }

    /**
     *
     * @return role as an int
     */
    public int getRole(){
        return role_id;
    }

    /**
     *
     * @return the username associated to the pass
     */
    public String getUsername(){
        return username;
    }

/**
 *
 * @return true if the pass is associated to a manager's account
 */
    public boolean isManager(){
        if(role_id==I_ROLE_MANAGER)
            return true;
        else
            return false;
    }
    
    /**
     *
     * @return true if the pass is associated to an administrator's account
     */

    public boolean isAdministrator(){
        if(role_id==I_ROLE_ADMINISTRATOR)
            return true;
        else
            return false;
    }


    /**
     *
     * @return true if authentified, false is not
     */

    public boolean getIsAuthentified(){
        return isAuthentified;
    }
}
