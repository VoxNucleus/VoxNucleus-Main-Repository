package me.FallingDownLib.CommonClasses.util;

import javax.servlet.http.Cookie;

/**
 *
 * @author victork
 */
public class CookiesUtil {

    public static String COOKIE_STORED_SESSION="storedSession";
    public static String COOKIE_USERNAME="username";
    public static String COOKIE_JSESSION="JSESSIONID";

       /** Find the username from a list of cookies.
     * Since it's after the authentification this field should never return empty
     * @param listCookie
     * @return
     */
    static public  String findUsernameFromCookie(Cookie[] listCookie) {
        String username = "";
        for (int index = 0; index < listCookie.length; index++) {
            if (listCookie[index].getName().equals("username")) {
                username = listCookie[index].getValue();
            }
        }
        return username;
    }


    /**
     *
     * @param listCookie
     * @return an empty String
     */

    static public String findStoredSessionFromCookie(Cookie[] listCookie) {
        String storedSession = "";
        for (int index = 0; index < listCookie.length; index++) {
            if (listCookie[index].getName().equals("storedSession")) {
                storedSession = listCookie[index].getValue();
            }
        }
        return storedSession;
    }

}
