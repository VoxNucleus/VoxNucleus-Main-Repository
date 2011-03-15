package me.FallingDownLib.CommonClasses.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostInfo;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.CommonClasses.SupportedLanguages;

/**
 *
 * @author victork
 */
public class FieldVerificator {

    public static final int TITLE_MAX_LENGTH=20;
    public static final int MAX_NUMBER_LETTERS_DESCRIPTION_POST = 2000;
    public static final int MAX_NUMBER_LETTERS_TITLE_POST = 60;


    /**
     *
     * @param password
     * @param password_verif
     * @return
     */
    public static boolean user_verify_password(String password, String password_verif)
            throws IncorrectUserInfo {
        if (user_verify_one_password(password)) {
            if (!password.equals(password_verif)) {
                throw new IncorrectUserInfo("Les deux mots de passe "
                        + "ne sont pas identiques.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param password
     * @return
     * @throws IncorrectUserInfo
     */
    public static boolean user_verify_one_password(String password) 
            throws IncorrectUserInfo{
        if (password.length() < 5) {
            throw new IncorrectUserInfo("Le mot de passe est trop court (Il doit"
                    + " être supérieur à 5 caractères).");
        } else if(password.length()>12){
            throw new IncorrectUserInfo("Le mot de passe est trop long (Longueur "
                    + "maximale : 12 caractères)");
        }else{
            return true;
        }
        
    }

    /**
     *
     * @param email
     * @param email_verif
     * @return
     */
    public static boolean user_verify_email(String email, String email_verif)
            throws IncorrectUserInfo {
        if (user_verify_one_email(email)) {
            if (email.equals(email_verif)) {
                return true;
            } else {
                throw new IncorrectUserInfo("Les mails entrés ne sont pas identiques.");
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param email
     * @return
     * @throws IncorrectUserInfo
     */
    public static boolean user_verify_one_email(String email)
            throws IncorrectUserInfo {
        String[] tokens = email.split("@");
        if (!(tokens.length == 2)) {
            throw new IncorrectUserInfo("Le mail entré ne semble pas "
                    + "correspondre aux standard des mails.");
        }else{
            return true;
        }
    }

    /**
     *
     * @param username
     * @return
     */
    public static boolean user_verify_username(String username) throws IncorrectUserInfo {
        if (username.length() > 15) {
            throw new IncorrectUserInfo("Le nom d'utilisateur entré est trop long");
        } else if (username.length() < 2) {
            throw new IncorrectUserInfo("Le nom d'utilisateur entré est trop court");
        }
        Pattern notAlphaNumericalP = Pattern.compile(MyPattern.NOT_ALPHANUMERICAL);
        Matcher notAlphaNumMatcher = notAlphaNumericalP.matcher(username);
        if (notAlphaNumMatcher.find()) {
            throw new IncorrectUserInfo("Il existe dans le nom d'utilisateur "
                    + "entré des caractères non alphanumérique.");
        }
        return true;
    }

    /**
     *
     * @param language
     * @return
     * @throws IncorrectUserInfo
     */
    public static boolean user_verify_language(String language) throws IncorrectUserInfo {
        SupportedLanguages.getInstance();
        HashMap<String, String> supportedLanguages = SupportedLanguages.getHashLanguages();
        if (supportedLanguages.containsKey(language)) {
            return true;
        } else {
            throw new IncorrectUserInfo("La langue entrée n'est pas dans la liste des langages supportés par le site.");
        }
    }

 /**
  *
  * @param lastname
  * @return   true if the lastname is correct
  * @throws IncorrectUserInfo
  */
    public static boolean user_verify_lastname(String lastname) throws IncorrectUserInfo {
        if (lastname.length() > 15) {
            throw new IncorrectUserInfo("Le nom entré est trop long. "
                    + "La limite authorisée est de 15 caractères.");
        } else {
            return true;
        }

        }

    /**
     *
     * @param firstname
     * @return
     * @throws IncorrectUserInfo
     */
    public static boolean user_verify_firstname(String firstname) throws IncorrectUserInfo {
        if (firstname.length() > 15) {
            throw new IncorrectUserInfo("Le prénom entré est trop long. "
                    + "La limite authorisée est de 15 caractères.");
        } else {
            return true;
        }
    }

    /**
     * Verify the birthdate with
     * @param birthdate
     * @return
     * @throws IncorrectUserInfo
     */
    public static boolean user_verify_birthdate(String birthdate) throws IncorrectUserInfo {
        Pattern birthdatePattern = Pattern.compile(MyPattern.BIRTHDATE);
        Matcher birthdateMatcher = birthdatePattern.matcher(birthdate);
        if (!birthdateMatcher.find()) {
            throw new IncorrectUserInfo("La date d'anniversaire entrée ne correspond pas au prototype demandé : <br> jj/mm/aaaa");
        } else {
            return true;
        }
    }

    public static boolean user_verify_description(String description) throws IncorrectUserInfo{
        if(description.length()>1000)
            throw new IncorrectUserInfo("Votre description doit être limitée à"
                    + " 1000 caractères.");
        else
            return true;
    }

    public static boolean user_verify_center_of_interest(String center_of_interest)
            throws IncorrectUserInfo{
        if(center_of_interest.length()>70)
            throw new IncorrectUserInfo("Pour des raisons de performance la longueur "
                    + "maximale des centres d'intérêt est de 70 caractères.");
        else
            return true;
    }


    public static boolean user_verify_website(String link) throws IncorrectUserInfo {
        if(link.isEmpty())
            return true;
        Pattern http_linkPattern = Pattern.compile(MyPattern.HTTP_LINK_PATTERN);
        Matcher httpLinkMatcher = http_linkPattern.matcher(link);
        if (!httpLinkMatcher.find()) {
            throw new IncorrectUserInfo("Le lien que vous avez fourni ne semble pas être un lien internet.");
        } else {
            return true;
        }
    }


    /**
     * Verify given link
     * @param link
     * @return
     * @throws IncorrectPostInfo
     */
    public static boolean post_verify_http_link(String link) throws IncorrectPostInfo{
        Pattern http_linkPattern=Pattern.compile(MyPattern.HTTP_LINK_PATTERN);
        Matcher httpLinkMatcher= http_linkPattern.matcher(link);
        if(!httpLinkMatcher.find()){
            throw new IncorrectPostInfo("Le lien que vous avez fourni ne semble pas être un lien internet.");
        }else{
            return true;
        }
    }

    /**
     *
     * @param title
     * @return
     * @throws IncorrectPostInfo
     */
    public static boolean post_verify_title(String title) throws IncorrectPostInfo{
        if (title.isEmpty()) {
            throw new IncorrectPostInfo("Le titre du noyau est vide");
        } else if (title.length() > MAX_NUMBER_LETTERS_TITLE_POST) {
            throw new IncorrectPostInfo("Le titre du noyau est trop long...");
        }else
            return true;
    }

    /**
     *
     * @param description
     * @return
     * @throws IncorrectPostInfo
     */
    public static boolean post_verify_description(String description) throws IncorrectPostInfo {
        if (description.isEmpty()) {
            throw new IncorrectPostInfo("La description du post est vide...");
        } else if (description.length() > MAX_NUMBER_LETTERS_DESCRIPTION_POST) {
            throw new IncorrectPostInfo("La description du noyau est supérieure"
                    + " à 2000 caractères...");
        } else {
            return true;
        }
    }

    /**
     *
     * @param language
     * @return
     * @throws IncorrectPostInfo
     */
    public static boolean post_verify_language(String language) throws IncorrectPostInfo{
        if(!SupportedLanguages.isSupportedLanguage(language)){
            throw new IncorrectPostInfo("La langue indiquée n'existe pas...");
        }else
            return true;
    }


    /**
     *
     * @param category
     * @param subcategory
     * @return
     * @throws IncorrectPostInfo
     */
    public static boolean post_verify_category_subcategory(String category,
            String subcategory) throws IncorrectPostInfo{
        if(!SubCategories.isAValidPair(category, subcategory)){
            throw new IncorrectPostInfo("Vous ne semblez pas avoir entré une "
                    + "catégorie et une sous-catégorie correctes...");
        } else
            return true;
    }

    public static boolean post_verify_tags(String tags) throws IncorrectPostInfo {
        if(tags.isEmpty()){
            throw new IncorrectPostInfo("Votre liste de tags semble être vide");
        }else{
            return true;
        }

    }
}
