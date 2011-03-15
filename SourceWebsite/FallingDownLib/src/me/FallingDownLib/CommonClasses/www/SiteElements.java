package me.FallingDownLib.CommonClasses.www;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.footer.Footer;
import me.FallingDownLib.CommonClasses.www.post.PrintRegularListPost;
import me.voxnucleus.www.helpers.CommentCreationHelper;

/**
 * This class contains only static elements.
 * It's made to easily print elements accross the website with a common layout
 * @author victork
 */
public class SiteElements {

    /**
     * Print a commenting area for the Post with id : idPost
     * @param idPost
     * @return
     */
    static public String printCommentingArea(String idPost) {

        return ("<div class=\"commentarea\" >"
                + "<b>Poster un commentaire :</b>"
                + "<form method=\"POST\" id=\"comment_area_form\" action=\"/postmanagement/validateComment\">"
                + "<table>"
                + "<tr>"
                + "<td> Titre : </td>"
                + "<td><INPUT pattern=\".{0,60}\"  type=\"TEXT\" NAME=\"title\"></td>"
                + "</tr>"
                + "<tr>"
                + "<td> Texte : </td>"
                + "<td><textarea name=\"text\" pattern=\".+\" rows=5 cols=70></textarea></td>"
                + "</tr>"
                + "</table>"
                + "<input type=\"hidden\"  name=\"post_id\" value=\"" + idPost + "\" />"
                + "<button type=\"SUBMIT\"> Commenter </button> </form>" + "</div>");
    }

    /**
     * Print a commenting area for the Post with id : idPost.
     * Takes into account connected / not connected.
     * @param idPost
     * @param pass
     * @return html code for commenting area
     */
    static public String printCommentingArea(String idPost, Pass pass) {
        CommentCreationHelper comm_helper=CommentCreationHelper.getHelper();
        StringBuilder comment_area_builder = new StringBuilder();
        if (pass.getIsAuthentified()) {
            comment_area_builder.append("<div class=\"commentarea\" >");
            comment_area_builder.append("<b>Poster un commentaire :</b>");
            comment_area_builder.append("<form method=\"POST\" id=\"comment_area_form\" action=\"/postmanagement/validateComment\">");
            comment_area_builder.append("<table> <tr> <td style=\"width:70px;\"> Titre").append(comm_helper.getHelpTooltip(CommentFields.HTTP_COMMENT_TITLE)).append("</td>");
            comment_area_builder.append("<td><input pattern=\".{0,60}\"  type=\"TEXT\" NAME=\"");
            comment_area_builder.append(CommentFields.HTTP_COMMENT_TITLE).append("\">");
            comment_area_builder.append("</td>");
            comment_area_builder.append("</tr> <tr> <td> Texte").append(comm_helper.getHelpTooltip(CommentFields.HTTP_COMMENT_TEXT)).append("</td>");
            comment_area_builder.append("<td><textarea name=\"").append(CommentFields.HTTP_COMMENT_TEXT);
            
            comment_area_builder.append("\" required=\"required\" rows=5 cols=70></textarea></td>");
            comment_area_builder.append("</tr> </table>");
            comment_area_builder.append("<input type=\"hidden\"  name=\"post_id\" value=\"").append(idPost).append("\" />");
            comment_area_builder.append("<button type=\"SUBMIT\"> Commenter </button> </form>" + "</div>");
        } else {
            comment_area_builder.append("<div class=\"commentarea\"> <em> Vous ne semblez pas être ");
            comment_area_builder.append("connecté au site. Vous pouvez essayer de vous connecter ");
            comment_area_builder.append("en <a quicklogin=\"/usermanagement/quicklogin\" ");
            comment_area_builder.append("rel=\"#logsub\" onClick=\"load_overlay(this);\" style=\"cursor:pointer;\">ouvrant une ");
            comment_area_builder.append("zone de connection</a> ou alors <a href=\"/usermanagement/subscribe.jsp\">");
            comment_area_builder.append("vous pouvez vous enregistrer en cliquant ici</a>. </em> </div>");

        }
        return comment_area_builder.toString();
    }

    static public String displayBasicMenu(Pass pass) {
        CategoriesMenu3 menu = new CategoriesMenu3();
        LogoMenu logoMenu= LogoMenu.getInstance();
        StringBuilder buff = new StringBuilder();
        buff.append("<div id=\"up_box\">");
        buff.append(UserMenu.getUserMenu(pass.getIsAuthentified()));
        buff.append(logoMenu.getHTMLCode());
        buff.append(menu.getHTMLCode());
        buff.append("</div>");
        return buff.toString();
    }




    static public String displayMenu(String category, String sub_category, String filter) {
        CategoriesMenu3 menu = new CategoriesMenu3(category, sub_category, filter);
                LogoMenu logoMenu= LogoMenu.getInstance();

        StringBuilder buff = new StringBuilder();
        buff.append("<div id=\"up_box\">");
        buff.append(UserMenu.getUserMenu(false));
        buff.append(logoMenu.getHTMLCode());
        buff.append(menu.getHTMLCode());
        buff.append("</div>");
        return buff.toString();
    }

    /**
     * 
     * @param category
     * @param sub_category
     * @param filter
     * @return
     */
    static public String displayMenu(String category, String sub_category,
            String filter, Pass pass) {
        CategoriesMenu3 menu = new CategoriesMenu3(category, sub_category, filter);
                LogoMenu logoMenu= LogoMenu.getInstance();
        StringBuilder buff = new StringBuilder();
        buff.append("<div id=\"up_box\">");
        buff.append(UserMenu.getUserMenu(pass.getIsAuthentified()));
        buff.append(logoMenu.getHTMLCode());
        buff.append(menu.getHTMLCode());
        buff.append("</div>");
        return buff.toString();
    }



    /**
     * Generate code to insert an image with alt
     * WARNING the path must be inserted !
     * @param location
     * @param alt
     * @return
     */
    static public String insertImage(String location, String alt) {
        return ("<img class=\"post_image\" src=\"/image/" + location + "\" alt=\"" + alt + "\">");
    }

    /**
     * Display a footer
     * @return the code for a footer
     */
    static public String displayFooter() {
        StringBuilder buff = new StringBuilder();
        buff.append("<div id=\"footer\" >");
        buff.append("<a href=\"/\" > Page Principale </a> | <a href=\"/usermanagement/login.jsp\" > Se connecter </a>");
        buff.append("</div>");
        return buff.toString();
    }

    /**
     * This method display the footer of the website.
     * If identification succeeds then it prints a special footer,
     * otherwise it prints a basic footer
     * @param pass
     * @return code for the footer
     */

    static public String displayFooter(HttpServletRequest request){
        return "";
    }


    /**
     * This method display the footer of the website.
     * If identification succeeds then it prints a special footer,
     * otherwise it prints a basic footer
     * @param pass
     * @return code for the footer
     */

    static public String displayFooter(Pass pass){
        Footer footer = Footer.getInstance(pass);
        return footer.getHTMLCode();
    }

    static public StringBuffer getSearchBox() {
        StringBuffer buff = new StringBuffer();
        buff.append("<div id='search_container'>");
        buff.append("<form id=\"searchfield\" action=\"/search/search\" method=\"get\" >");
        buff.append("<input type=\"text\" name=\"q\" class=\"text_box\" value=\"Rechercher...\">");
        buff.append("<input type=\"submit\" id=\"submit\" src=\"/image/website/common/search.png\" value=\"Chercher\" alt=\"Chercher\">");
        buff.append("</form>");
        buff.append("</div>");
        return buff;
    }

    /**
     *
     * @return Code for an advanced Searchbox
     */
    static private StringBuffer getAdvancedSearchBox() {
        StringBuffer buff = new StringBuffer();
        buff.append("<div id='search_container'>");
        buff.append("<form id=\"searchfield\" action=\"/search/search\" method=\"get\" >");
        buff.append("<input type=\"text\" class=\"text_box\" value=\"Rechercher...\">");
        //To modify
        buff.append("<input type=\"submit\" id=\"submit\" src=\"/image/website/common/search.png\" value=\"Chercher\" alt=\"Chercher\">");
        buff.append("</form>");
        buff.append("</div>");
        return buff;
    }

    /**
     *
     * @return the right menu
     */
    static public String displayRightMenu() {
        StringBuilder buff = new StringBuilder("<div id=\"right_menu\">");
        buff.append("<ul>");
        buff.append("<li> test </li>");

        buff.append("</ul>");
        return buff.toString();
    }

    /**
     * Insert CSS styles easily.
     * @return HTML code to insert CSS
     */
    static public String getCommonCSSStyle() {
        StringBuilder buff = new StringBuilder("");
        buff.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/menu.css\" >");
        buff.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/general_layout.css\" >");
        buff.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/footer.css\" >");
        return buff.toString();
    }

    /**
     *
     * @return CSS code for one column layout
     */
    static public String getOneColumnStyle() {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/layout_one_column.css\" >";
    }

    /**
     *
     * @return CSS code for two column layout
     */
    static public String getTwoColumnStyle() {
        return "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/layout_two_column.css\" >";
    }

    /**
     *
     * @return code for JQuery only. NOT MINIFIED
     */

    public static String getJqueryScript(){
        return "<script type=\"text/javascript\" src=\"/jsp/jquery-1.5.min.js\"></script>";
    }

    /**
     * Insert important scripts
     * @return HTML code for scripts
     */
    static public String getCommonScripts() {
        StringBuilder script_builder = new StringBuilder();
        script_builder.append(getJqueryScript());
        script_builder.append(getJQueryTool());
        script_builder.append(getJQueryUI());
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/jquery.cookies.2.2.0.js\"></script>");
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/jmenu.js\"></script>");
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/general/jgeneralvoxnucleus.js\"></script>");
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/jVote.js\"></script>");
        script_builder.append("<script type=\"text/javascript\" src=\"/jsp/footer/jFooter.js\"></script>");
        return script_builder.toString();
    }

    public static String getJQueryUI(){
        return "<script type=\"text/javascript\" src=\"/jsp/plugins/jquery-ui-1.8.8.custom.min.js\"></script>";
    }

    /**
     *
     * @return Jquery util link
     */
    public static String getJQueryTool(){
        return "<script type=\"text/javascript\" src=\"/jsp/plugins/jquery.tools.min.js\"></script>";
    }

    /**
     * 
     * @return
     */

    public static String getsublogOverlay(){
        StringBuilder overlay_builder = new StringBuilder();
        overlay_builder.append("<div class=\"logsuboverlay\" id=\"logsub\" style=\"display:none\">");
        overlay_builder.append("<div class=\"contentWrap\"></div>");
        overlay_builder.append("<div class=\"close\"></div></div>");
        return overlay_builder.toString();
    }

    /**
     *
     * @return code for explanation box
     */
    public static String getExplanationBoxScript(){
        return "<script type=\"text/javascript\" src=\"/jsp/box/jBox.js\"></script>";
    }

    public static String getOverlayScript(){
        return "<script type=\"text/javascript\" src=\"/jsp/overlay/logsub.js\"></script>";
    }

    /**
     * It also add a div that gives an indication for JQuery to launch requests
     * @return list of pages
     */
    static public String getPages(String path, int index) {
        StringBuilder buff = new StringBuilder("<div id=\"list_pages\">");
        if (index >= 3) {
            buff.append("<ul class=\"list\">");
            buff.append("<li><a href=\"").append(path).append("/").append(index - 2).append("\">").append(index - 1).append("</a></li>");
            buff.append("<li><a href=\"").append(path).append("/").append(index - 1).append("\">").append(index).append("</a></li>");
            buff.append("<li ").append("class=\"selected\"").append("><a href=\"").append(path).append("/").append(index).append("\">").append(index+1).append("</a></li>");
            buff.append("<li><a href=\"").append(path).append("/").append(index+1).append("\">").append(index + 2).append("</a></li>");
            buff.append("<li><a href=\"").append(path).append("/").append(index + 2).append("\">").append(index + 3).append("</a></li>");
        } else {
            buff.append("<ul class=\"list\">");
            buff.append("<li").append(getSelectedPage(0,index)).append("><a href=\"").append(path).append("/").append(0).append("\">").append(1).append("</a></li>");
            buff.append("<li").append(getSelectedPage(1,index)).append("><a href=\"").append(path).append("/").append(1).append("\">").append(2).append("</a></li>");
            buff.append("<li").append(getSelectedPage(2,index)).append("><a href=\"").append(path).append("/").append(2).append("\">").append(3).append("</a></li>");
            buff.append("<li").append(getSelectedPage(3,index)).append("><a href=\"").append(path).append("/").append(3).append("\">").append(4).append("</a></li>");
            buff.append("<li").append(getSelectedPage(4,index)).append("><a href=\"").append(path).append("/").append(4).append("\">").append(5).append("</a></li>");
        }
        buff.append("</ul></div>");
        return buff.toString();
    }

    static private String getSelectedPage(int pageNumber, int requested_Page){
        if(pageNumber==requested_Page)
            return " class=\"selected\" ";
        else
            return "";
    }

    /**
     * Print zone which displays a number of votes plus a voting area if printVoteArea is set to true
     * @param nbVote
     * @param key
     * @param printVoteArea
     * @return
     */
    static public String getVoteZone(int nbVote, String key, boolean printVoteArea) {
        StringBuilder buff = new StringBuilder("<div class=\"rank\">");
        buff.append("<div id=\"nbVotes\">").append(nbVote).append("</div>");
        if (printVoteArea) {
            buff.append("<div id=\"vote_zone\">");
            buff.append("<a onClick=\"doVote(\'").append(key).append(" \',this)\"> Votez </a>");
            buff.append("</div>");
        }
        return buff.append("</div>").toString();
    }

    /**
     *
     * @param text
     * @return
     */
    static public String getPageSubtitle(String text) {
        StringBuilder builder = new StringBuilder();
        builder.append("<div id=\"subtitle\">").append(text).append("</div>");
        return builder.toString();
    }

    /**
     *
     * @param scriptName
     * @param category
     * @param sub_category
     * @param time_filter
     * @return code to insert in \<head\>
     */
    static public String getJSReloaderCode(String scriptName, String category,
            String sub_category, String time_filter) {
        StringBuilder builder = new StringBuilder();
        builder.append("<script type=\"text/javascript\">");
        builder.append("$(document).ready(function(){");
        builder.append(scriptName).append("()");
        if (time_filter != null) {
            builder.append("\",\"").append(time_filter);
        }
        builder.append("});");
        builder.append("</script>");
        return builder.toString();
    }

    /**
     * 
     * @param args
     * @param values
     * @return area that MUST be with display none. Contains important informations
     */
    static public String getHiddenZone(String[] args, String[] values) {
        StringBuilder hidden_builder = new StringBuilder();
        hidden_builder.append("<span class=\"hidden\">");
        for (int index = 0; index < args.length; index++) {
            hidden_builder.append("<span class=\"").append(args[index]).append("\">").
                    append(values[index]).append("</span>");
        }
        hidden_builder.append("</span>");
        return hidden_builder.toString();
    }

    /**
     *
     */

    public static String  insertListPosts(ArrayList<String> postList) {
        if (postList.size() > 0) {
            PrintRegularListPost list = new PrintRegularListPost(postList);
            return list.getHTMLCode();
        } else {
            return "<b>Oups... Il semble ne pas y avoir de posts dans cette section."
                    + " Essayez de regarder d'autres sections ou simplement "
                    + "<a href=\"/postmanagement/newpost\">postez en suivant ce lien...</a></b>";
        }
    }

    /**
     *
     * @return an HTML doctype
     */

    static public String getDoctypeHTML(){
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"  \"http://www.w3.org/TR/html4/loose.dtd\">";
    }

    /**
     * insert User image
     * @param uHash
     * @return
     */
    static public String insertUserAvatar(UserHash uHash) {
        if (uHash.getAvatar()!=null && !uHash.getAvatar().isEmpty()) {
            return ("<img class=\"post_image\" src=\"/image/user/" + uHash.getUsername()
                    + "/" + uHash.getAvatar() + "\" alt=\"" + uHash.getUsername() + "\">");
        } else {
            return "";
        }

    }

    /**
     *
     * @param filter
     * @param category
     * @param sub_category
     * @return the title of the page
     */
    static public String getFilterTitle(String filter_text, String category, String sub_category){
        StringBuilder title_builder = new StringBuilder();
        title_builder.append("VoxNucleus : ");
        if (!category.equals(Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL])) {
            title_builder.append(category);
            int category_index = Categories.getCategory(category);
            if (!sub_category.equals(SubCategories.SUB_CATEGORIES[category_index][0])) {
                title_builder.append(" - ");
                title_builder.append(sub_category);
            }
        }

        title_builder.append(filter_text);
        return title_builder.toString();
    }
}
