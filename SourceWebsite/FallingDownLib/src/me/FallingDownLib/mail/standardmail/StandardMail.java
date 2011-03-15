package me.FallingDownLib.mail.standardmail;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import me.FallingDownLib.mail.interfaces.ToMessage;

/**
 * Standard form of an HTML message
 * @author victork
 */
public class StandardMail implements ToMessage {

    //Default value
    protected String message_from="admin@voxnucleus.fr";
    protected String message_to="";
    protected String message_title="";
    protected String message_corpse="";

    protected MimeMessage message;

    protected StandardMail(Session session){
        message = new MimeMessage(session);
    }

    protected void setCorpse(String corpse){
        message_corpse = corpse;
    }

    protected void setTitle(String title){
        message_title= title;
    }

    protected void setFrom(String from){
        message_from = from;
    }

    protected void setTo(String to){
        message_to=to;
    }

    /**
     *
     * @return message
     */
    public MimeMessage getMessage() {
        try {
            message.setFrom(new InternetAddress(message_from));
            message.setSubject(message_title);
            message.setContent(message_corpse, "text/html");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(message_to));
        } catch (MessagingException ex) {
            Logger.getLogger(StandardMail.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

}
