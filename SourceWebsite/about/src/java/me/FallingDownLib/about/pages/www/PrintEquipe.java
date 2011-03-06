package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * Show general team informations
 * @author victork
 */
public class PrintEquipe implements ToCodeConverter {

    private StringBuilder equipe_builder;

    /**
     * Constructor
     */
    protected PrintEquipe(){
        equipe_builder =new StringBuilder();
    }


    /**
     *
     * @return
     */
    public static PrintEquipe getInstance(){
        return new PrintEquipe();
    }

    /**
     *
     * @return html code
     */
    public String getHTMLCode() {
        buildPage();
        return equipe_builder.toString();
    }

    /**
     * 
     */
    private void buildPage() {
        equipe_builder.append("L'équipe est actuellement constituée de trois personnes "
                + ", jeunes étudiants ou travaillant déjà.");
        equipe_builder.append("<h2>Administration Système</h2>");
        equipe_builder.append("Le serveur du site doit être surveillé de près, "
                + "tout d'abord parce que sa puissance est... disons relativement "
                + "faible. De plus sur internet un serveur exposé au public est une "
                + "cible constante pour les internautes mal-"
                + "intentionnés.<br>");
        equipe_builder.append("<a href=\"/about/team/florianduraffourgpage\"> "
                + "Florian Duraffourg </a>"
                + " est en charge de faire survivre le serveur.");
        equipe_builder.append("<h2>Développement </h2>");
        equipe_builder.append("Tout le développement du site a été fait par une "
                + "seule personne. La configuration des bases de données, la création "
                + "des pages a été grâce aux outils à sa disposition (retrouvez-les "
                + "dans la section \"Les technologies\")<br>");
        equipe_builder.append("Pour le pire et le meilleur, celui qui a développé tout "
                + "ce site est <a "
                + "href=\"/about/team/victorkabdebonpage\"> Victor Kabdebon </a>");
        equipe_builder.append("<h2>WebDesign </h2>");
        equipe_builder.append("Le design du site est un travail important qui vous "
                + "permet de profiter du site");
        equipe_builder.append("Le chirurgien esthétique qui a pu opérer le site est <a "
                + "href=\"/about/team/hugoleygnacpage\"> Hugo Leygnac </a>");
        
        equipe_builder.append("<h2>Nous rejoindre vous intéresse ?</h2>");
        equipe_builder.append("<a href=\"/about/participatepage#join_dev\">"
                + "Visitez cette page pour plus d'informations."
                + "</a>");

    }

}
