package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintIndex implements ToCodeConverter {

    private StringBuilder index_builder;

    protected PrintIndex(){
        index_builder = new StringBuilder();
    }

    public static PrintIndex getInstance(){
        return new PrintIndex();
    }

    public String getHTMLCode() {
        buildPage();
        return index_builder.toString();
    }

    private void buildPage() {
        index_builder.append("<h1> VoxNucleus </h1>");
        index_builder.append("<p align=\"center\" style=\"font-style: italic;\">");
        index_builder.append("Tout d'abord un peu de culture : <br>");
        index_builder.append("Vox : (latin) la voix, la parole<br>");
        index_builder.append("Nucleus : (latin) le noyau <br>");
        index_builder.append("</p>");
        index_builder.append("<h2> Le but du site</h2>");
        index_builder.append("Aujourd'hui internet est un univers sans cesse en "
                + "mouvement et il devient difficile de sélectionner les "
                + "contenus intéressants."
                + "Ce site tente d'apporter une solution à ce problème. Sur "
                + "VoxNucleus chacun a le droit de poster des messages "
                + "(appelés \"noyaux\" ou \"nucleus\") "
                + "et ce sont les internautes qui en apportant leur voix (\"vox\") viennent "
                + "faire grandir ce noyau en votant "
                + "pour lui. Tout ce qui aura été choisi par la majorité sera "
                + "parmi les premiers. ");
        index_builder.append("<h2> Les engagements</h2>");
        index_builder.append("<h3> Confidentialité</h3>");
        index_builder.append("Avoir un site capable d'organiser l'information de façon "
                + "sociale est une bonne chose mais il ne faudrait pas négliger "
                + "des choses tout aussi importantes comme la vie privée. En des "
                + "temps où Facebook/ Twitter / Google stockent toutes vos données de"
                + " connexion il est bon d'avoir un espace qui s'affranchit de "
                + "ses normes de non-confidentialité. Personne ne peut négliger "
                + "l'importance de pouvoir naviguer en paix sans être constament épié.<br> "
                + "Nous nous engageons donc à établir des règles de respect de "
                + "la vie privée, à ne pas revendre les informations tirées de "
                + "nos visites ni aucune des informations que vous nous confierez "
                + "(dans la limite où ce sont des données confidentielles)<br>"
                + "Comme personne n'est parfait, pour des raisons de management de serveur "
                + "nous avons besoin du service \"Google Analytics\". L'objectif "
                + "dans le futur sera de  Cependant pour des besoins de management et de publicité "
                + "Google Analytics est activé sur toutes les pages. L'objectif "
                + "dans le futur sera de se passer de ce service.");
        index_builder.append("<h3> Logiciel libre</h3>");
        index_builder.append("Les développeurs s'engagent à terme (en cas de réussite "
                + "du site) à publier l'ensemble des sources de ce site. Cependant "
                + "dans un premier temps par souci d'organisation les sources ne "
                + "seront pas révélées mais elles s'ouvriront petit à petit ou totalement "
                + "dans les prochains mois.");
    }

}
