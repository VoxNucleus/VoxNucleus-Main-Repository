package FallingDown.user.modify.password.reset;

import javax.mail.MessagingException;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.StringUtil.random.StrongRandomSequenceGenerator;
import me.FallingDownLib.mail.MailSender;
import me.FallingDownLib.mail.standardmail.user.PasswordResetMail;
import me.prettyprint.cassandra.utils.StringUtils;

/**
 *
 * @author victork
 */
public class PasswordReseter {

    private String userId;
    private String email;

    private String emailInDB;
    private String random_sequence;

    private String pathToReset="";

    protected PasswordReseter(String uId,String mail){
        email = mail;
        userId=uId;
    }

    public static PasswordReseter getReseter(String uId,String mail){
        return new PasswordReseter(uId,mail);
    }

    public void reset() throws IncorrectUserInfo, MessagingException, Exception{

        checkInDatabase();
        InsertInDatabase();
        buildPathToReset();
        SendMail();
    }

    /**
     * 
     * @throws IncorrectUserInfo
     * @throws Exception
     */

    private boolean checkInDatabase() throws IncorrectUserInfo, Exception {
        UserConnector uConnector = UserConnector.getInstance(userId);
        if (uConnector.isUserPresent()) {
            emailInDB = StringUtils.string(uConnector.getField(StringUtils.bytes(UserFields.DB_EMAIL)));
            if (email.equalsIgnoreCase(emailInDB)) {
                return true;
            } else {
                throw new IncorrectUserInfo("L'email que vous avez entré ne correspond"
                        + " pas à ce qui est dans les bases de données.");
            }
        } else {
            throw new IncorrectUserInfo("L'utilisateur n'est pas dans la base"
                    + " de données.");
        }
    }

    /**
     * 
     * @throws Exception
     */
    private void InsertInDatabase() throws Exception {
        StrongRandomSequenceGenerator seq_generator = StrongRandomSequenceGenerator.getGenerator(20);
        UserConnector uConnector = UserConnector.getInstance(userId);
        random_sequence=seq_generator.getSequence();

        uConnector.setField(StringUtils.bytes(UserFields.DB_PASSWORD_RESET),
                StringUtils.bytes(random_sequence));
    }

    private void SendMail() throws MessagingException {
        MailSender mail_sender = MailSender.getMailSender();
        PasswordResetMail reset_mail = PasswordResetMail.gePasswordResetMail(mail_sender.getSession());
        reset_mail.setEmail(email);
        reset_mail.setRandomSequence(random_sequence);
        reset_mail.setUsername(userId);
        mail_sender.setMessageToSend(reset_mail);
        mail_sender.sendMessage();
    }

    private void buildPathToReset() {

    }


}
