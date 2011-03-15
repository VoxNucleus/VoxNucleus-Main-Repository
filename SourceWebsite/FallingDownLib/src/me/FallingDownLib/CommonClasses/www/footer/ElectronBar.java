package me.FallingDownLib.CommonClasses.www.footer;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * This bar is the footer at the bottom of the screen.
 * Functionnalities will be increased with time
 * @author victork
 */
public class ElectronBar implements ToCodeConverter{

    private static final String TEXT_ELECTRON_BAR="Partager un lien rapidement...";

    private StringBuilder bar_builder;

    /**
     * 
     */

    private ElectronBar(){
        bar_builder = new StringBuilder();
    }

    /**
     *
     * @return
     */

    public static ElectronBar getInstance(){
        return (new ElectronBar());
    }



    private void buildBar(){
        bar_builder.append("<div id=\"electronbar\">");
        bar_builder.append("<form name=\"eb_form\" action=\"/postmanagement/newpost\" method=\"POST\">");
        bar_builder.append("<input name=\"link\" class=\"minized\" type=\"text\" value=\"").append(TEXT_ELECTRON_BAR).append("\">");
        bar_builder.append("<button>Poster ce lien</button>");
        bar_builder.append("</form>");
        bar_builder.append("</div>");

    }

    public String getHTMLCode() {
        buildBar();
        return bar_builder.toString();
    }
}
