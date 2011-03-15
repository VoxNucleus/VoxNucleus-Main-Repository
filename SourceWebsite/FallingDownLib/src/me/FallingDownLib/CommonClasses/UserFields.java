/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.FallingDownLib.CommonClasses;

/**
 *
 * @author victork
 */
public class UserFields {

    /**
     * Cassandra Database : REQUIRED FIELDS
     */
    public static final String INDEX_DOCTYPE="user";
    public static final String DB_USERNAME="username";
    public static final String DB_PASSWORD="password";
    public static final String DB_PASSWORD_RESET="password_reset";
    public static final String DB_DATE_BIRTH="dateBirth";
    public static final String DB_EMAIL="email";
    public static final String DB_NAME="lastname";
    public static final String DB_FIRSTNAME="firstname";
    public static final String DB_LANGUAGE="language";
    public static final String DB_NBVOTES="nbVotes";
    public static final String DB_NBPOSTS="nbPosts";
    public static final String DB_NBCOMMENTS="nbComments";
    public static final String DB_SUBSCRIBE_TIMESTAMP="subscribetimestamp";
    public static final String DB_SUSPENDED="suspended";
    public static final String DB_UUID="TimeUUID";
    public static final String DB_ROLE="role";
    public static final String DB_FILENAME="filename";
    public static final String DB_REPUTATION="reputation";
    

    /**
     * Cassandra Database : REQUIRED FIELDS
     */

    public static final String DB_SITE_WEB="website";
    public static final String DB_DESCRIPTION="description_user";
    public static final String DB_CENTER_INTERESTS="center_interests";

    /**
     * SolR
     */
    public static final String INDEX_USERNAME = "u_username";
    public static final String INDEX_PASSWORD = "password";
    public static final String INDEX_DATE_BIRTH = "u_dateBirth";
    public static final String INDEX_EMAIL = "u_email";
    public static final String INDEX_NAME = "u_lastname";
    public static final String INDEX_FIRSTNAME = "u_firstname";
    public static final String INDEX_LANGUAGE = "u_language";
    public static final String INDEX_NBVOTES = "nbVotes";
    public static final String INDEX_NBPOSTS = "nbPosts";
    public static final String INDEX_NBCOMMENTS = "nbComments";
    public static final String INDEX_SUBSCRIBE_TIMESTAMP = "u_subscribetimestamp";
    public static final String INDEX_SUSPENDED = "u_suspended";


    /**
     * HTTP fields
     */

    public static final String HTTP_USERNAME="username";
    public static final String HTTP_LOGIN_USERNAME="l_username";
    public static final String HTTP_FIRSTNAME="firstname";
    public static final String HTTP_LASTNAME="lastname";
    public static final String HTTP_OLD_PASSWORD="old_password";
    public static final String HTTP_PASSWORD="password";
    public static final String HTTP_PASSWORD_CONFIRM="confirm_password";
    public static final String HTTP_PASSWORD_RESET = "password_reset";
    public static final String HTTP_EMAIL="email";
    public static final String HTTP_EMAIL_CONFIRM="confirm_email";
    public static final String HTTP_LANGUAGE="language";
    public static final String HTTP_BIRTHDATE = "birthdate";
    public static final String HTTP_CAPTCHA_TEXT="j_captcha_response";


    /**
     * HTTP fields - additional informations
     */

    public static final String HTTP_SITE_WEB = "website";
    public static final String HTTP_DESCRIPTION="description_user";
    public static final String HTTP_CENTER_INTERESTS="center_interests";
    public static final String HTTP_AVATAR="avatar";

    public static final String HTTP_TEXTAREA_DISCLAIMER="";

    /**
     * Disclaimer.
     */
    public static final String HTTP_DISCLAIMER_TEXT_FR = "Conditions d'utilisation de VoxNucleus \n\n\n"
            + "Bienvenue sur VoxNucleus !\n\n"
            + "1. Relation avec VoxNucleus\n"
            + "1.1 L'utilisation des logiciels, services et sites web de VoxNucleus est "
            + "régie par les termes d'un contrat conclu entre VoxNucleus et vous. Le "
            + "présent document décrit la teneur du contrat et définit certains termes "
            + "de ce contrat.\n"
            + "2. Acceptation des conditions\n"
            + "2.1 Vous devez accepter les conditions avant de pouvoir utiliser "
            + "nos services. Vous ne serez pas en droit d'utiliser les "
            + "services sans avoir accepté ces conditions.\n"
            + "2.2 Pour que l'acceptation des conditions se fasse vous pouvez au choix :\n"
            + "a. Cliquer sur le bouton en bas de cette page"
            + "b. Utiliser les services fournis par VoxNucleus."
            + "3. Inscription sur le site\n"
            + "3.1 Lors de l'inscription vous vous engagez à fournir des informations "
            + "correctes sur votre personne" // A faire
            + "4. Confidentialité\n"
            + "4.1 VoxNucleus s'engage à fournir tous les efforts possible afin que "
            + "la confidentialité des informations que vous ayez fourni lors "
            + "de votre enregistrement et par la suite soit protégées. \n"
            + "4.2 Les garanties de confidentialité situées au dessus ne "
            + "s'appliquent pas dans les cas suivants :\n"
            + "a. En cas de pertes de données dûes à des attaques dirigées vers nos serveurs\n"
            + "b. Lors de votre visite sur les services VoxNucleus des parties "
            + "tierces du site\n"
            + "c. En cas de mauvaise manipulation de votre part. Si vous fournissez "
            + "des informations personnelles par l'intermédiaire de nos services vous "
            + "serez considéré comme intégralement responsable.\n"
            + "5. Droits de propriété\n"
            + "5.1 Vous conservez les droits d'auteur et tous les droits vis-à-vis du "
            + "contenu que vous fournissez. Cependant vous cédez le droit permanent, "
            + "irrévocable, mondial gratuit et non exclusif de reproduire, adapter, modifier,"
            + " tout contenu que vous avez founri.\n"
            + "5.2 De plus vous vous engagez à ce que les images que vous postez "
            + "ne viole pas les droits d'auteur. Dans le cas où ce que vous fournissez "
            + "viole les droits d'auteur vous en porterez l'entière responsabilité.\n"
            + "5.3 Toutes les contenus présents sur le site (exceptés ceux listés au point 5.4)"
            + " : texte, image, ... sont utilisables par toute personne et tout \n"
            + "5.4 Ne sont pas inclus dans le point (5.3) le nom VoxNucleus, "
            + " les logos et images associés au nom."
            + "6. Utilisation des services\n"
            + "6.1 Toute personne, site internet ou organisation est libre d'utiliser "
            + "les textes et images postés par les utilisateurs du site.\n"
            + "6.2 Il est par contre interdit par une tierce organisation ou personne"
            + " de gagner de l'argent à partir des informations, textes ou images "
            + "trouvés sur ce site."
            + "7. Modification des conditions\n"
            + "VoxNucleus peut modifier à tout moment les conditions listées plus haut ou "
            + "ajouter des conditions supplémentaires. Lorsque la modification sera effectuée "
            + "vous serez contacté et vous aurez le choix entre supprimer votre compte "
            + "et accepter les nouvelles conditions.\n \n"
            + "Le 25 octobre 2010\n";

    public static final String UUID = "uuid";

}
