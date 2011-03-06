package me.FallingDownLib.about;

import me.FallingDownLib.CommonClasses.www.SiteElements;

/**
 *
 * @author victork
 */
public class AboutElements {

    /**
     * 
     * @return
     */
    public static String getAboutCSS(){
        StringBuilder css_builder = new StringBuilder();
        css_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/about/general_about.css\" >");
        return css_builder.toString();
    }
    /**
     * 
     * @return
     */
    public static String getAboutScript(){
        StringBuilder script_builder = new StringBuilder();
        script_builder.append(SiteElements.getJqueryScript());
        script_builder.append(SiteElements.getJQueryTool());
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/about/jaboutscript.js\"></script>");
        return script_builder.toString();
    }
    /**
     *
     * @return
     */

    public static String getAboutBasicElements(){
        StringBuilder basic_builder = new StringBuilder();
        basic_builder.append(getAboutCSS());
        basic_builder.append(getAboutScript());
        return basic_builder.toString();
    }
}
