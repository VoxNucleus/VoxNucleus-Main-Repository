package me.FallingDownLib.CommonClasses.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class LogoMenu implements ToCodeConverter{
    
    private StringBuilder logomenu_code;

    /**
     * Constructor
     */

    protected LogoMenu(){
        logomenu_code =new StringBuilder();
    }

    /**
     * 
     * @return instance of a LogoMenu
     */

    public static LogoMenu getInstance(){
        return new LogoMenu();
    }


    /**
     * Build the div around the menu
     */
    private void buildLogoMenu(){

        logomenu_code.append("<div id=\"logo_menu\">");
        insertLogo();
        insertSearch();
        logomenu_code.append("</div>");

    }

    /**
     * Insert a logo
     */
    private void insertLogo(){
        logomenu_code.append("<a href=\"/\"><img border=\"0\" id=\"main_logo\" "
                + "src=\"/image/website/logo/NucleusLogoAndName.png\" alt=\"MainLogo\"></a>");
    }

    /**
     * Insert search container
     */

    private void insertSearch(){
        logomenu_code.append("<div id='search_container'>");
        logomenu_code.append("<form id=\"searchfield\" action=\"/search/search\" method=\"get\" >");
        logomenu_code.append("<input type=\"text\" name=\"q\" class=\"text_box\" value=\"Rechercher...\"/>");
        logomenu_code.append("<input type=\"submit\" id=\"submit\" src=\"/image/website/common/search.png\" value=\"Chercher\" alt=\"Chercher\"/>");
        logomenu_code.append("</form>");
        logomenu_code.append("</div>");
    }

    /**
     *
     * @return HTML code
     */

    public String getHTMLCode() {
        buildLogoMenu();
        return logomenu_code.toString();
    }


}
