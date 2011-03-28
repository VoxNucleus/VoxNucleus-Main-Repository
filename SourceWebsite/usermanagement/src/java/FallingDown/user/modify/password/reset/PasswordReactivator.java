package FallingDown.user.modify.password.reset;

import javax.mail.MessagingException;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.identification.PasswordChanger;
import me.FallingDownLib.StringUtil.random.WeakRandomSequenceGenerator;
import me.FallingDownLib.mail.MailSender;
import me.FallingDownLib.mail.standardmail.user.NewPasswordMail;
import me.prettyprint.cassandra.utils.StringUtils;

/**
 *
 * @author victork
 */
public class PasswordReactivator {

    private String randomSequence;
    private String randomSequenceInDB;
    private String userId;
    private String newpassword;
    private String email;
    private boolean hasBeenReactivated;

    /**
     * 
     * @param seq
     * @param uId
     */
    protected PasswordReactivator(String seq, String uId){
        randomSequence = seq;
        userId=uId;
        hasBeenReactivated=false;
    }

    /**
     *
     * @param seq
     * @param uId
     * @return an instance
     */

    public static PasswordReactivator getPassReactivator(String seq, String uId){
        return new PasswordReactivator(seq,uId);
    }

    /**
     * 
     * @throws IncorrectUserInfo
     * @throws Exception
     */

    public void doReactivate() throws IncorrectUserInfo, Exception{
        verifyInfoFromDB();
        cleanDB();
        setNewPassword();
        sendMail();
    }

    /**
     * 
     * @throws IncorrectUserInfo
     * @throws Exception
     */
    private void verifyInfoFromDB() throws IncorrectUserInfo, Exception {
        UserConnector uConnector = UserConnector.getInstance(userId);
        if(!uConnector.isUserPresent())
            throw new IncorrectUserInfo("L'utilisateur n'existe pas");
        else{
            randomSequenceInDB=StringUtils.string(
                    uConnector.getField(StringUtils.bytes(UserFields.DB_PASSWORD_RESET)));
            if(!randomSequenceInDB.equalsIgnoreCase(randomSequence)){
                throw new IncorrectUserInfo("La vérification ne correspond pas à celle "
                        + "qui est présente dans nos bases de données");
            }
        }
    }

    /**
     * Generate a new 7 letters password
     * @throws Exception
     */

    private void setNewPassword() throws Exception {
        UserConnector uConnector = UserConnector.getInstance(userId);
        email=StringUtils.string(uConnector.getField(StringUtils.bytes(UserFields.DB_EMAIL)));
        WeakRandomSequenceGenerator w_seq_generator=WeakRandomSequenceGenerator.getGenerator(7);
        newpassword =w_seq_generator.getSequence();
        PasswordChanger pass_changer = PasswordChanger.getChanger(userId, newpassword);
        pass_changer.doChange();
    }

    /**
     * TODO : Does not work
     * Remove the information for the field
     * @throws Exception
     */
    private void cleanDB() throws Exception {
        UserConnector uConnector = UserConnector.getInstance(userId);
        uConnector.removeField(StringUtils.bytes(UserFields.DB_PASSWORD_RESET));
    }

    private void sendMail() throws MessagingException {
        MailSender mail_sender = MailSender.getMailSender();
        NewPasswordMail newpass_mail = NewPasswordMail.getNewPassMail(mail_sender.getSession());
        newpass_mail.setUsername(userId);
        newpass_mail.setPassword(email);
        newpass_mail.setPassword(newpassword);
        mail_sender.setMessageToSend(newpass_mail);
        mail_sender.sendMessage();
    }

}
