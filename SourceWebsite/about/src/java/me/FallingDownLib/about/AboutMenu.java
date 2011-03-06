package me.FallingDownLib.about;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class AboutMenu implements ToCodeConverter {

    public static int MENU_INDEX;
    public static int MENU_TEAM;
    public static int MENU_T_VICTOR_KABDEBON;
    public static int MENU_T_FLORIAN_DURAFFOURG;
    public static int MENU_T_HUGO_LEYGNAC;
    public static int MENU_PARTICIPATE;
    public static int MENU_TECHNOLOGIES;
    public static int MENU_AD;
    public static int MENU_WEBMASTER;
    public static int MENU_FAQ;

    private boolean isPageTeam=false;
    private StringBuilder about_menu_builder;

    protected AboutMenu(){
        about_menu_builder = new StringBuilder();
    }

    public void pageTeam(boolean bool){
        isPageTeam=bool;
    }

    public static AboutMenu getMenu(){
        return new AboutMenu();
    }

    public String getHTMLCode() {
        buildMenu();
        return about_menu_builder.toString();
    }



    private void buildMenu() {
        about_menu_builder.append("<ul>");
        about_menu_builder.append("<li>").append("<a href=\"/about/index\">Présentation </a>").append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/equipepage\">L'Equipe</a>");
        if (isPageTeam) {
            about_menu_builder.append("<ul>");
            about_menu_builder.append("<li>").append("<a href=\"/about/team/victorkabdebonpage\">Victor Kabdebon</a>").append("</li>");
            about_menu_builder.append("<li>").append("<a href=\"/about/team/florianduraffourgpage\">Florian Duraffourg").append("</li>");
            about_menu_builder.append("<li>").append("<a href=\"/about/team/hugoleygnacpage\">Hugo Leygnac</a>").append("</li>");
            about_menu_builder.append("</ul>");
        }
        about_menu_builder.append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/participatepage\">Participer</a>").append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/technologypage\">Les Technologies </a>").append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/adpage\">Publicité</a>").append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/webmasterpage\">Intégrer VoxNucleus</a>").append("</li>");
        about_menu_builder.append("<li>").append("<a href=\"/about/faqpage\">FAQ</a>").append("</li>");

        about_menu_builder.append("</ul>");
    }

}
