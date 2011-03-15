package me.FallingDownLib.mail.standardmail.user;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import me.FallingDownLib.mail.standardmail.StandardMail;

/**
 *
 * @author victork
 */
public class SubscribeMail extends StandardMail {

    String username;
    String email;


    protected SubscribeMail(Session session){
        super(session);
    }

    public static SubscribeMail getInstance(Session session){
        return new SubscribeMail(session);
    }

    public void setUsername(String uId){
        username=uId;
    }

    public void setEmail(String uIdEmail){
        email = uIdEmail;
    }

    @Override
    public MimeMessage getMessage() {
        super.setCorpse(buildCorpse());
        super.setFrom("inscription@voxnucleus.fr");
        super.setTitle("Bienvenue sur VoxNucleus !");
        super.setTo(email);
        return super.getMessage();
    }

    private String buildCorpse() {
        StringBuilder builder = new StringBuilder();
        builder.append("<h3>Bienvenue sur VoxNucleus, ").append(username).append(" </h3><br>");
        builder.append("Nous sommes toujours heureux d'accueillir de nouvelles personnes.<br>"
                + "Notez que nous ne vous harcèleront pas de mail, les seuls mails que vous "
                + "recevrez de notre part seront les annonces importantes ainsi que les demandes "
                + "relatives à votre compte (mise à jour de votre mot de passe, des conditions,"
                + " etc...)<br>");
        builder.append("Il est inutile d'essayer de répondre à cette adresse, "
                + "personne ne la regarde... Si vous souhaitez nous contacter écrivez à "
                + "admin@voxnucleus.fr<br>");
        builder.append("A bientôt sur VoxNucleus !<br>");
        builder.append("Victor et toute l'équipe de VoxNucleus");
        return builder.toString();
    }

    





}
