package FallingDown.user.login;

import java.util.Arrays;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author victork
 */
public class Identification {

    public boolean valid;
    byte[] hashedPassword;

    public Identification(String login, String password) {
        valid=false;
        FallingDownConnector connector=null;
        try {
            MessageDigest mess = MessageDigest.getInstance("SHA-1");
            mess.update(password.getBytes("UTF-8"));
            hashedPassword = mess.digest();
            connector = new FallingDownConnector();
            byte[] hashedPasswordInDB=connector.getInfoColumnWithkey(login,
                    "password", FallingDownConnector.DB_USERS);
            if(Arrays.equals(hashedPassword, hashedPasswordInDB))
                valid=true;
        } catch (NoSuchAlgorithmException e) {
            //Should never happen
        } catch (UnsupportedEncodingException e) {
            // Should not happen
        } catch(Exception e){
        }finally{
            if(connector!=null)
                connector.release();
        }
    }


    public boolean isValid(){
        return valid;
    }
}
