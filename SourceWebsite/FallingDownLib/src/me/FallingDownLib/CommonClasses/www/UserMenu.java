package me.FallingDownLib.CommonClasses.www;

/**
 *
 * @author victork
 */
public class UserMenu {

     /**
     * Display a non custom menu
     * @return top menu
     */

    static public String getUserMenu(boolean isAuthentified){
        StringBuilder builder = new StringBuilder();
        builder.append("<ul class=\"topnav\">");
        builder.append("<li class=\"short\"> <a href=\"/\" > Page Principale </a> </li>");
        builder.append("<li class=\"short\"> <a href=\"/user\" > Mon espace </a></li>");
        builder.append("<li class=\"short\"> <a href=\"/postmanagement/newpost\"> Nouveau Noyau </a></li>");
        builder.append("<li class=\"short\"> <a href=\"/usermanagement/option\">Options </a></li>");
        builder.append("<li class=\"short\"> <a target=\"_blank\" href=\"/about/\" >A propos </a> </li>");
        if(!isAuthentified)
            builder.append(getSubscribe());
        builder.append("</ul>");
        return builder.toString();
    }

    private static String getSubscribe(){
        return "<li class=\"short\"> <a href=\"/usermanagement/subscribe.jsp\" > <i>S'inscrire</i> </a> </li>";
    }

}
