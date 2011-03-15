/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.FallingDownLib.CommonClasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * This class extends HashMap to have direct and conveniant way to retrieve informations about an user.
 * 
 * @author victork
 */
public class UserHash extends HashMap<String,String> {

    private String formatNull(String in){
        if(in ==null)
            return "";
        else
            return in;
    }


    /**
     * Constructor. WARNING : it copies the HashMap entirely, so it might be slow.
     * @param map
     */

    public UserHash(HashMap<String, String> map){
        super.putAll(map);
    }

    public String getUsername(){
        return super.get(UserFields.DB_USERNAME);
    }

    /**
     *
     * @return User's firstname
     */
    public String getFirstname(){
        return formatNull(super.get(UserFields.DB_FIRSTNAME));
    }

    /**
     *
     * @return User's lastname
     */

    public String getLastname(){
        return formatNull(super.get(UserFields.DB_NAME));
    }

    /**
     * @return the subscription date
     */

    public String getSubscribeDate() {
        Date d = new Date(Long.parseLong(getTimestamp()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(d);
    }

    /**
     *
     * @return String representation of the long number timestamp
     */

    public String getTimestamp(){
        return super.get(UserFields.DB_SUBSCRIBE_TIMESTAMP);
    }

    public String getLanguage(){
        return super.get(UserFields.DB_LANGUAGE);
    }

    public String getEmail(){
        return super.get(UserFields.DB_EMAIL);
    }

    public int getNbVotes(){
        return Integer.parseInt(super.get(UserFields.DB_NBVOTES));
    }

    public int getNbComments(){
        return Integer.parseInt(super.get(UserFields.DB_NBCOMMENTS));
    }

    public int getNbPosts(){
        return Integer.parseInt(super.get(UserFields.DB_NBPOSTS));
    }

    public String getUUID(){
        return super.get(UserFields.UUID);
    }

    public String getBirthDate(){
        return formatNull(super.get(UserFields.DB_DATE_BIRTH));
    }

    public String getWebsite(){
        return formatNull(super.get(UserFields.DB_SITE_WEB));
    }

    public String getCenterOfInterest(){
        return formatNull(super.get(UserFields.DB_CENTER_INTERESTS));
    }

    public String getDescription(){
        return formatNull(super.get(UserFields.DB_DESCRIPTION));
    }

    public String getAvatar(){
        return formatNull(super.get(UserFields.DB_FILENAME));
    }


    /**
     *
     * @return true if the user is suspended
     */

    public boolean isSuspended(){
        String answer = super.get(UserFields.DB_SUSPENDED);
        if(answer.equals("yes")){
            return true;
        }else{
            return false;
        }
    }

    
}
