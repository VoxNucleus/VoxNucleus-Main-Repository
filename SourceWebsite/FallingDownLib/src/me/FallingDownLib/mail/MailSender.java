package me.FallingDownLib.mail;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import me.FallingDownLib.mail.interfaces.ToMessage;


/**
 *
 * @author victork
 */
public class MailSender {

    private static String host="localhost";
    Properties properties;
    private Session session;
    private MimeMessage messageToSend;

    /**
     * Default constructor
     */
    protected MailSender(){
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        session = Session.getDefaultInstance(properties);
    }

    /**
     * 
     * @return an instance of a mail Sender
     */

    public static MailSender getMailSender(){
        return new MailSender();
    }

    public Session getSession(){
        return session;
    }

    public void setMessageToSend(ToMessage toMess){
        messageToSend=toMess.getMessage();
    }

    public void sendMessage() throws MessagingException{
        Transport.send(messageToSend);
    }

}
