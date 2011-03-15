package me.voxnucleus.www.helpers;

import me.FallingDownLib.CommonClasses.UserFields;

/**
 * Help for user creation and modification
 * @author victork
 */
public class UserCreationHelper extends Helper {

    protected UserCreationHelper(){
        super();

        super.addHelp(UserFields.HTTP_AVATAR, "Cet avatar vous représentera partout "
                + "sur le site, choisissez-le avec attention.");
        super.addHelp(UserFields.DB_EMAIL, "Vote adresse email complète de "
                + "la forme : utilisateur@fournisseur.extension");
        super.addHelp(UserFields.DB_USERNAME, "Votre nom d'utilisateur qui vous "
                + "identifiera à travers le site. Il est inchangeable alors vérifiez "
                + "qu'il vous va.");
        super.addHelp(UserFields.DB_PASSWORD,"Votre mot de passe. Pour éviter "
                + "qu'il soit volé choisissez le assez complexe.");
        super.addHelp(UserFields.HTTP_CAPTCHA_TEXT, "On veut juste vérifier que "
                + "vous n'êtes pas une saleté de robot !");
    }

    public static UserCreationHelper getHelper(){
        return new UserCreationHelper();
    }


}
