package me.FallingDownLib.about;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class AboutFooter implements ToCodeConverter {

    private StringBuilder about_footer_builder;

    /**
     * Builder
     */
    protected AboutFooter(){
        about_footer_builder= new StringBuilder();
    }

    /**
     *
     * @return
     */

    public static AboutFooter getInstance(){
        return new AboutFooter();
    }

    /**
     * 
     * @return
     */

    public String getHTMLCode() {
        buildFooter();
        return about_footer_builder.toString();
    }

    /*
     *
     */

    private void buildFooter() {

        about_footer_builder.append("<ul id=\"footer_list\">");
                about_footer_builder.append("<li class=\"lileft\">")
                        .append("<a href=\"/\">Revenir au site</a>").append("</li>");

        about_footer_builder.append("<li>")
                .append("<a href=\"/about/\">A propos de VoxNucleus</a>").append("</li>");
        about_footer_builder.append("<li>")
                .append("<a href=\"/about/participatepage\">Participer</a>").append("</li>");
        about_footer_builder.append("<li class=\"liright\">")
                .append("<a target=\"_blank\" href=\"http://voxnucleus.blogspot.com\">Blog</a>").append("</li>");
        about_footer_builder.append("<li class=\"liright\">")
                .append("<a href=\"mailto:equipe@voxnucleus.fr\">Contacter l'équipe</a>").append("</li>");
        about_footer_builder.append("</ul>");
    }

}
