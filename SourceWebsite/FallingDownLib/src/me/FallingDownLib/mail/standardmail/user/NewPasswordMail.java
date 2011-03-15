package me.FallingDownLib.mail.standardmail.user;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import me.FallingDownLib.mail.standardmail.StandardMail;

/**
 *
 * @author victork
 */
public class NewPasswordMail extends StandardMail{
    private String email;
    private String username;
    private String password;

    /**
     * Default constructor
     * @param session
     */
    protected NewPasswordMail(Session session){
        super(session);
    }

    /**
     *
     * @param session
     * @return an instance to construct a mail
     */
    public static NewPasswordMail getNewPassMail(Session session){
        return new NewPasswordMail(session);
    }

    public void setEmail(String u_email){
        email=u_email;
    }

    public void setPassword(String pass){
        password=pass;
    }

    public void setUsername(String uId){
        username=uId;
    }

    @Override
    public MimeMessage getMessage(){
        super.setCorpse(buildMessage());
        super.setFrom("admin@voxnucleus.fr");
        super.setTo(email);
        super.setTitle("VoxNucleus : Réinitialisation du mot de passe effectuée");
        return super.getMessage();
    }

    /**
     * 
     * @return corpse of the message
     */
    private String buildMessage() {
        StringBuilder newpassword_builder = new StringBuilder();
        newpassword_builder.append("<h3>Votre nouveau mot de passe est ici </h3>");
        newpassword_builder.append("Votre mot de passe vient d'être réinitalisé par "
                + "nos soins. Le nouveau mot de passe est le suivant (enlevez les guillements)"
                + " : \"").append(password).append("\" <br>");

        newpassword_builder.append("Ce mot de passe peut être utilisé dès maintenant "
                + "en se connectant à <a href=\"http://www.voxnucleus.fr/usermanagement/login.jsp\">"
                + " Voxnucleus.fr</a>.<br>");
        newpassword_builder.append("En terme de sécurité, comme votre mot de passe "
                + "a circulé par mail et si vous êtes connecté par Wifi il a probablement "
                + "circulé en clair il est plus que conseillé d'aller le changer dès "
                + "maintenant !<br><br><br>");
        newpassword_builder.append("<b> L'équipe VoxNucleus</b>");
        return newpassword_builder.toString();
    }

}
