package me.FallingDownLib.CommonClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CassandraConnection.connectors.SessionConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;


/**
 * This class is a way to identify yourself across the whole website
 * @author victork
 */
public class FallingDownSession {

    public String storedSession;
    public String login;
    private String IP;
    private long cookieDeathDate;
    public boolean authentified = false;

    /**
     * v2 : Get login from cookie
     * @param request
     */

    public FallingDownSession(HttpServletRequest request) {
        Cookie[] listCookies = request.getCookies();
        for (int i = 0; i < listCookies.length; i++) {
            if (listCookies[i].getName().equals("storedSession")) {
                storedSession = listCookies[i].getValue();
            }
            if(listCookies[i].getName().equals("login")){
                login = listCookies[i].getValue();
            }
        }
    }

    public FallingDownSession(String sessionId, String login, long expirationDate, String IP){
        storedSession = sessionId;
        this.login=login;
        this.cookieDeathDate=expirationDate;
        this.IP=IP;
    }

    /**
     * Save session informations into Cassandra.
     * @throws InvalidRequestException
     * @throws PoolExhaustedException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */

    public void saveSessionToDabase() throws InvalidRequestException,
            PoolExhaustedException, NotFoundException, UnavailableException, Exception {
        FallingDownConnector connector = new FallingDownConnector();
        try {

            connector.insertInfoColumnWithkey(storedSession, FallingDownSessionFields.DB_SESSION_ID,
                    storedSession,
                    FallingDownConnector.DB_SESSIONS);
            connector.insertInfoColumnWithkey(storedSession, FallingDownSessionFields.DB_SAVED_IP,
                    IP,
                    FallingDownConnector.DB_SESSIONS);
            connector.insertInfoColumnWithkey(storedSession, FallingDownSessionFields.DB_LOGIN,
                    login,
                    FallingDownConnector.DB_SESSIONS);
            connector.insertInfoColumnWithkey(storedSession, 
                    FallingDownSessionFields.DB_EXPIRATION_DATE,
                    Long.toString(cookieDeathDate),
                    FallingDownConnector.DB_SESSIONS);

        } finally {
            connector.release();
        }

    }

    /**
     * depreciated
     */


    public void authentifiate() {
        FallingDownConnector connector=null;
        try {
            connector = new FallingDownConnector();
            //String sessionInDabase = StringUtils.string(connector.getInfoColumnWithkey(storedSession, "sessionId", FallingDownConnector.DB_SESSIONS));
            String loginInDB = StringUtils.string(connector.getInfoColumnWithkey(storedSession,
                    "login", FallingDownConnector.DB_SESSIONS));
            authentified = true;
            connector.release();
        } catch (NotFoundException e) {
            authentified = false;
        } catch (Exception e) {
            //TODO
        }finally{
            if(connector!=null)
                connector.release();
        }
    }



    /**
     *
     * @param request
     * @return true if person is authentified; false if not
     */
    public static boolean authentifiateFromData(HttpServletRequest request) {
        boolean isAuthentified = false;
        
        if ( request == null || request.getCookies() == null) {
            return false;
        }
        Cookie[] listCookie = request.getCookies();
        String ID = "";
        String username = "";
        for (int index = 0; index < listCookie.length; index++) {
            if (listCookie[index].getName().equals("username")) {
                username = listCookie[index].getValue();
            }
            if (listCookie[index].getName().equals("storedSession")) {
                ID = listCookie[index].getValue();
            }
        }
        if ((!ID.isEmpty()) && (!username.isEmpty())) {
            HashMap<String, String> infoAssociated = getSessionFromDatabase(ID);
            String loginInDb = infoAssociated.get(FallingDownSessionFields.DB_LOGIN);
            String IPInDb = infoAssociated.get(FallingDownSessionFields.DB_SAVED_IP);
            boolean isUsernameCorrect = false;
            boolean isRemoteAddrCorrect = false;
            boolean isValid=false;
            if (loginInDb != null && IPInDb != null) {
                isUsernameCorrect = loginInDb.equals(username);
                isRemoteAddrCorrect = IPInDb.equals(request.getRemoteAddr());
                isValid = isSessionStillValid(infoAssociated, ID);
            }
            
            if (isUsernameCorrect && isRemoteAddrCorrect && isValid) {
                isAuthentified = true;
            }
        }
        return isAuthentified;
    }

    /**
     *
     * @param infoAssociated
     * @return true if the session is valid, false if not
     */

    private static boolean isSessionStillValid(HashMap<String,String> infoAssociated, String sessionID ){
        boolean isValid=false;
        long timeOfDeathTimestamp = Long.parseLong(infoAssociated.get(FallingDownSessionFields.DB_EXPIRATION_DATE));
        if (timeOfDeathTimestamp < (Calendar.getInstance()).getTimeInMillis()) {
            destroySession(sessionID);
        } else {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Provide an authentification method for the whole website, using cookie data
     * TODO : clear mess.
     * @param ID
     * @param username
     * @return
     * @throws PoolExhaustedException
     * @throws TException
     * @throws NotFoundException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws Exception
     */
    public static boolean authentifiateFromData(Cookie[] listCookie) {
        FallingDownConnector connector = null;
        if (listCookie == null) {
            return false;
        }
        String ID = null;
        String username = null;
        for (int index = 0; index < listCookie.length; index++) {
            if (listCookie[index].getName().equals("username")) {
                username = listCookie[index].getValue();
            }
            if (listCookie[index].getName().equals("storedSession")) {
                ID = listCookie[index].getValue();
            }
        }

        if (ID != null && username != null) {
            try {
                connector = new FallingDownConnector();
                String loginInDB = StringUtils.string(connector.getInfoColumnWithkey(ID, "login", FallingDownConnector.DB_SESSIONS));
                if (loginInDB.equals(username)) {
                    return true;
                } else {
                    return false;
                }
            } catch (InvalidRequestException ex) {
                Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (NotFoundException ex) {
                Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (UnavailableException ex) {
                Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (Exception ex) {
                Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } finally {
                if (connector != null) {
                    connector.release();
                }
            }
        } else {
            return false;
        }
    }


    /**
     * Provides authentification for the whole website, based on a username and Id
     * @param username
     * @param ID
     * @return
     * @throws PoolExhaustedException
     * @throws TException
     * @throws NotFoundException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws Exception
     */

    public static boolean authentifiateFromData(String username, String ID) throws PoolExhaustedException,
            TException, NotFoundException, InvalidRequestException, UnavailableException, Exception {
        FallingDownConnector connector = new FallingDownConnector();
        try {

            String loginInDB = StringUtils.string(connector.getInfoColumnWithkey(ID,
                    FallingDownSessionFields.DB_LOGIN,
                    FallingDownConnector.DB_SESSIONS));
            if (loginInDB.equals(username)) {
                return true;
            } else {
                return false;
            }
        } finally {
            if(connector!=null)
                connector.release();
        }       
    }

    /**
     *
     * @param sessionID
     * @return HashMap containg all informations on a session.
     */

    public static HashMap<String,String> getSessionFromDatabase(String sessionID) {
        HashMap<String, String> sessionInformation = new HashMap<String, String>();
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            List<Column> listCol = connector.getKeyColumn(FallingDownConnector.DB_SESSIONS, sessionID);
            sessionInformation = ColumnUtil.ArrayToHashString(listCol);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(FallingDownSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        }finally{
            if(connector!=null)
                connector.release();
        }
        return sessionInformation;
    }


    /**
     * Destroy a session if asked
     * @param sessionKey
     */
    
    public static void destroySession(String sessionKey){

        SessionConnector sConnector = SessionConnector.getConnector(sessionKey);
        sConnector.deleteSession();
    }
}
