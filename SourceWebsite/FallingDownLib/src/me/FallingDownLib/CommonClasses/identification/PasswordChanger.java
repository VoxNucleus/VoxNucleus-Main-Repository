package me.FallingDownLib.CommonClasses.identification;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserFields;
import me.prettyprint.cassandra.utils.StringUtils;

/**
 * Class to change the password of an user. It is an unsafe method and must be
 * used AFTER verification
 * @author victork
 */
public class PasswordChanger {

    private String password;
    private String userId;
    private boolean changeSuccessful;


    /**
     * 
     * @param uId
     * @param newPassword
     */
    protected PasswordChanger(String uId, String newPassword){
        password= newPassword;
        userId=uId;
        changeSuccessful=false;
    }

    /**
     * warning, everything must be verified before
     * @param uId
     * @param newPassword
     * @return an instance of Password changer
     */
    public static PasswordChanger getChanger(String uId, String newPassword) {
        return new PasswordChanger(uId, newPassword);
    }

    /**
     * Do the change of password
     */
    public void doChange() {
        UserConnector uConnector = UserConnector.getInstance(userId);
        byte[] hashedPass = User.hashPassword(password);
        try {
            uConnector.setField(StringUtils.bytes(UserFields.DB_PASSWORD), hashedPass);
            changeSuccessful=true;
        } catch (Exception ex) {
            Logger.getLogger(PasswordChanger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return true if the change has been successful
     */
    public boolean getIsChangeSuccessful(){
        return changeSuccessful;
    }

}
