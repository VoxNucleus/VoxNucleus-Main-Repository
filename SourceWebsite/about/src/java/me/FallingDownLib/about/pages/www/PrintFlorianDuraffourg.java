package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintFlorianDuraffourg implements ToCodeConverter {

    private StringBuilder flo_builder;
    protected PrintFlorianDuraffourg(){
        flo_builder = new StringBuilder();
    }

    public static PrintFlorianDuraffourg getInstance(){
        return new PrintFlorianDuraffourg();
    }

    public String getHTMLCode() {
        buildPage();
        return flo_builder.toString();
    }

    private void buildPage() {
        flo_builder.append("<h1>Description</h1>");
        flo_builder.append("Diplômé de Telecom SudParis Florian administre les "
                + "serveurs du site. Il travaille actuellement en tant qu'"
                + "ingénieur réseau/ sécurité chez l'opérateur mobile Orange.");
        flo_builder.append("<h3>Contact</h3>");
        flo_builder.append("Pour le contacter envoyer un mail à "
                + "<a href=\"mailto:florian@voxnucleus.fr\">florian@voxnucleus.fr</a>");
    }
}
