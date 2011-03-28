package FallingDown.user;

import me.FallingDownLib.CassandraConnection.connectors.UserConnector;

/**
 *
 * @author victork
 */
public class UsernameVerificator {

    /**
     *
     * @param username
     * @return true if username is available false otherwise
     */
    public static boolean verifyUsername(String username){
        boolean isUsernameAvailable=true;
        UserConnector uConnector = UserConnector.getInstance(username);
        try {
            isUsernameAvailable=!uConnector.isUserPresent();
        } catch (IllegalArgumentException ex) {
            
        } catch (Exception ex) {
            
        }
        return isUsernameAvailable;
    }

}
