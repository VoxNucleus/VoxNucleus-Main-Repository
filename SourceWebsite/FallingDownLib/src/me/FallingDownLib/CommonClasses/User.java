/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.FallingDownLib.CommonClasses;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

//For security
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.Exceptions.UserDoesNotExist;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.util.FieldVerificator;
import me.prettyprint.cassandra.utils.StringUtils;

/**
 *
 * @author victork
 */
public class User {

    public String username, name, firstName, birth, language;
    public String email;
    public String email_verif;
    private String password;
    private String password_verification;
    public byte[] hashPassword;
    public boolean userValid = true;
    public int nbPosts;
    public int nbComments;


    private boolean isQuickSubscription=false;

    /**
     * Get the Post parameters and converts its informations for the creation of a User
     * @param requestMap
     */
    public User(Map<String, String[]> requestMap, boolean isQuick) throws IncorrectUserInfo, InvalidRequestException, TException, PoolExhaustedException, UnavailableException, TimedOutException, Exception {
        username = requestMap.get(UserFields.HTTP_USERNAME)[0];
        password = requestMap.get(UserFields.HTTP_PASSWORD)[0];
        email = requestMap.get(UserFields.HTTP_EMAIL)[0];
        nbPosts = 0;
        nbComments = 0;
        isQuickSubscription = isQuick;
        if (isQuickSubscription) {
            quickValidateUser();
        } else {
            try {
                this.language = new String(requestMap.get(UserFields.HTTP_LANGUAGE)[0].getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                //Error should never happen
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.email_verif = requestMap.get(UserFields.HTTP_EMAIL_CONFIRM)[0];
            this.password_verification = requestMap.get(UserFields.HTTP_PASSWORD_CONFIRM)[0];
            validateUser();
        }

    }

    /**
     * This method makes all the verification necessary
     * @throws IncorrectUserInfo
     */
    private void validateUser() throws IncorrectUserInfo {
        validate_username();
        FieldVerificator.user_verify_username(username);
        FieldVerificator.user_verify_email(email, email);
        FieldVerificator.user_verify_language(language);
        FieldVerificator.user_verify_password(password, password_verification);
        hashPassword=hashPassword(password);
    }

    private void quickValidateUser() throws IncorrectUserInfo{
        validate_username();
        FieldVerificator.user_verify_username(username);
        FieldVerificator.user_verify_one_email(email);
        language="fr";
        FieldVerificator.user_verify_one_password(password);
        hashPassword=hashPassword(password);

    }

    private void validate_username() throws IncorrectUserInfo {
        UserConnector uConnector = UserConnector.getInstance(username);
        if (uConnector.isUserPresent()) {
            throw new IncorrectUserInfo("Erreur, nom d'utilisateur déjà enregistré");
        }

    }

    private boolean isLanguageCorrect() {
        SupportedLanguages.getInstance();
        HashMap<String, String> supportedLanguages = SupportedLanguages.getHashLanguages();
        if (supportedLanguages.containsKey(this.language)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that checks if the address passed is a good one.
     * @return
     */
    private boolean isEmailCorrect() {
        String[] tokens = this.email.split("@");
        if (tokens.length == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get directly all informations on a User. WARNING even password
     * is retrieved, must NOT been displayed
     * @param userId
     * @return
     * @throws UserDoesNotExist
     * @throws NotFoundException
     * @throws Exception
     */
    static public HashMap<String, String> getUserFromDatabase(String userId)
            throws UserDoesNotExist, NotFoundException,
            Exception {
        HashMap<String, String> user = new HashMap<String, String>();
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            List<Column> list = connector.getSlice(FallingDownConnector.DB_USERS, userId);
            if (list.size() > 0) {
                for (int index = 0; index < list.size(); index++) {
                    if (!StringUtils.string(list.get(index).getName()).equals(UserFields.UUID)) {
                        user.put(StringUtils.string(list.get(index).getName()),
                                StringUtils.string(list.get(index).getValue()));
                    } else {
                        user.put(UserFields.UUID, (EasyUUIDget.toUUID(list.get(index).getValue())).toString());
                    }
                }
                connector.release();

            } else {
                connector.release();
                throw new UserDoesNotExist("L'utilisateur n'exist pas dans les bases de données");
            }
        } catch (InvalidRequestException ex) {
        } catch (UnavailableException ex) {
        } catch (TimedOutException ex) {
        } catch (TException ex) {
        } catch (PoolExhaustedException ex) {
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
        return user;
    }

    /**
     *
     * @param password
     * @return the hashed password
     */
    public static byte[] hashPassword(String password) {
        byte[] hashedPassword = null;
        try {
            MessageDigest mess = MessageDigest.getInstance("SHA-1");
            mess.update(password.getBytes("UTF-8"));
            hashedPassword = mess.digest();
        } catch (NoSuchAlgorithmException e) {
            //N'arrivera jamais normmalement
        } catch (UnsupportedEncodingException e) {
            //N'arrivera pas non plus
        }
        return hashedPassword;
    }

    /**
     * 
     * @return HashMap containing all informations
     */
    public HashMap<String, byte[]> toHashMapDB() {
        HashMap<String, byte[]> result = new HashMap<String, byte[]>();
        Calendar cal = Calendar.getInstance();
        result.put(UserFields.DB_USERNAME, StringUtils.bytes(username));
        result.put(UserFields.DB_PASSWORD, hashPassword);
        result.put(UserFields.DB_EMAIL, StringUtils.bytes(email));
        result.put(UserFields.DB_LANGUAGE, StringUtils.bytes(language));
        result.put(UserFields.DB_NBCOMMENTS, StringUtils.bytes("0"));
        result.put(UserFields.DB_NBPOSTS, StringUtils.bytes("0"));
        result.put(UserFields.DB_REPUTATION, StringUtils.bytes("0"));
        result.put(UserFields.DB_NBVOTES, StringUtils.bytes("0"));
        result.put(UserFields.DB_SUSPENDED, StringUtils.bytes("false"));
        result.put(UserFields.DB_SUBSCRIBE_TIMESTAMP, StringUtils.bytes(
                Long.toString(cal.getTimeInMillis())));
        result.put(UserFields.DB_ROLE, StringUtils.bytes(Pass.S_ROLE_USER));
        return result;
    }
}
