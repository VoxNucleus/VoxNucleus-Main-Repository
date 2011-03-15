package me.FallingDownLib.CommonClasses.www;

/**
 *
 * @author victork
 */
public class SiteDecorations {

    /**
     *
     * @return code to set the favicon across pages
     */

    public static String setFavIcon(){
        StringBuilder builder= new StringBuilder("<link rel=\"icon\" href=\"/static/VoxNucleus.ico\" type=\"image/x-icon\">");
        builder.append("<link rel=\"shortcut icon\" href=\"/static/VoxNucleus.ico\" type=\"image/x-icon\">");
        return builder.toString();
    }

    /**
     * When page is too short it is not very beautiful, this things helps building something nicer.
     * @return a div with id space
     */
    static public String getBlankSpace() {
        return "<div id=\"space\" />";
    }

    /**
     *
     * @return img tag with the logo
     */
    public static String getBigLogo(){
        return "<img id=\"main_logo\" src=\"/image/website/logo/NucleusLogoAndName.png\" alt=\"logo\">";
    }

    public static String getRankZoneStyle(){
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/rank_style.css\" >";
    }

    public static String getExplanationBoxStyle(){
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/boxes/explanation_boxes.css\" >";
    }
    /**
     *
     * @return css code
     */

    public static String getOverlayStyle(){
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/overlay/logsub.css\">";
    }

}
