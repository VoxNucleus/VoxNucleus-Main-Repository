package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintTechnology implements ToCodeConverter {

    private StringBuilder tech_builder;

    protected PrintTechnology(){
        tech_builder = new StringBuilder();
    }

    public static PrintTechnology getInstance(){
        return new PrintTechnology();
    }

    /**
     * 
     * @return
     */
    public String getHTMLCode() {
        buildPage();
        return tech_builder.toString();
    }

    private void buildPage() {
        tech_builder.append("Toute l'équipe tient d'abord à saluer le magnifique "
                + "travail effectué par le monde de l'open source. Certaines "
                + "organisations telles que la Apache Foundation mettent un point "
                + "d'honneur à fournir d'excellents outils et ce gratuitement."
                + " On ne peut qu'être admiratif devant les possibilités offertes "
                + "à tous de travailler avec d'aussi excellents outils.");
        tech_builder.append("<h2>Base de données</h2>");
        tech_builder.append("La base de données utilisée par VoxNucleus s'appelle "
                + "<em>Cassandra</em>. Cassandra n'est pas une alternative à MySQL "
                + "mais au contraire est en opposition avec le modèle SQL. Cassandra "
                + "appartient au mouvement de bases NoSQL, créé pour pallier les "
                + "défauts de SQL."
                + "Plus d'information sur ce nouveau modèle de base de données "
                + "<a target_blank=\"_blank\" href=\"http://cassandra.apache.org\">"
                + "sur le site web de Cassandra</a>");
        tech_builder.append("<h2>Site Web</h2>");
        tech_builder.append("Pour générer les pages vues des serveurs Web Tomcat "
                + "sont installés. Ils s'appuient sur des Servlet (et quelques "
                + "JSP) codés en JAVA. Tomcat est développé par la fondation Appache "
                + "et est actuellement dans sa version 7. Plus d'information sur "
                + "<a target=\"_blank\" href=\"http://tomcat.apache.org/\">le "
                + "site web dédié à Tomcat</a>.");
        tech_builder.append("<h2>Pages Webs</h2>");
        tech_builder.append("Dans la construction des pages Webs en elle-même nous "
                + "nous sommes beaucoup appuyés sur les technologies libres. Un "
                + "outil précieux pour tous les développeurs web est <a target="
                + "\"_blank\" href=\"http://jquery.com/\">JQuery</a>. Simple d'"
                + "utilisation, il vous sera utile dans bien des circonstances. "
                + "Construit à partir de Jquery la librairie <a href=\"http://flowplayer.org/tools/\" "
                + "target=\"_blank\"> Jquery Tools </a>");
    }

}
