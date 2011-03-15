package me.FallingDownLib.mail.standardmail.user;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import me.FallingDownLib.mail.standardmail.StandardMail;

/**
 *
 * @author victork
 */
public class PasswordResetMail extends StandardMail {

    private String username;
    private String random_Sequence;
    private String user_email;

    protected PasswordResetMail(Session session){
        super(session);
    }

    public static PasswordResetMail gePasswordResetMail(Session session){
        return new PasswordResetMail(session);
    }

    public void setEmail(String email){
        user_email=email;
    }

    public void setUsername(String uId){
        username=uId;
    }

    public void setRandomSequence(String rand_seq){
        random_Sequence=rand_seq;
    }

    @Override
    public MimeMessage getMessage() {
        super.setCorpse(buildMessage());
        super.setTo(user_email);
        super.setTitle("VoxNucleus : Demande de réinitialisation du mot de passe");
        super.setFrom("admin@voxnucleus.fr");
        return super.getMessage();
    }

    private String buildMessage() {
        String pathToReinitilizePassword="http://www.voxnucleus.fr/usermanagement/"
                + "modify/passwordreset?username="+username+"&"+
                "password_reset="+random_Sequence;

        StringBuilder corpse_builder = new StringBuilder();
        corpse_builder.append("<h3>Instructions pour réinitialiser votre mot de passe.</h3>");

        corpse_builder.append("Il semble que vous ayez demandé à réinitialiser "
                + "votre mot de passe sur VoxNucleus.<br>"
                + ""
                + "Votre mot de passe n'est pas encore réinitialiser il faut que vous "
                + "cliquiez sur ce lien : <br>");

        corpse_builder.append("<a href=\"").append(pathToReinitilizePassword).append("\">"
                + "Lien pour réactiver votre mot de passe</a> <br>");
        corpse_builder.append("Si vous n'arrivez pas à cliquer sur le lien, copier simplement "
                + "l'adresse suivante et coller dans la barre d'adresse : ");
        corpse_builder.append(pathToReinitilizePassword).append(" .");
        corpse_builder.append("<br>Si le mot de passe ne parvient toujours pas à être "
                + "réinitialisé n'hésitez pas à contacter un membre de l'équipe à "
                + "cette adresse :"
                + " equipe@voxnucleus.fr. Nous serons heureux de vous aider !");
        return corpse_builder.toString();
    }
}
