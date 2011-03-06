package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintHugoLeygnac implements ToCodeConverter {

    private StringBuilder hugo_builder;

    protected PrintHugoLeygnac(){
        hugo_builder = new StringBuilder();
    }

    public static PrintHugoLeygnac getInstance(){
        return new PrintHugoLeygnac();
    }

    public String getHTMLCode() {
        buildPage();
        return hugo_builder.toString();
    }

    private void buildPage() {
        hugo_builder.append("<h1>Description</h1>");
        hugo_builder.append("Etudiant à Supinfocom Arles, passionné par le "
                + "cinéma d'animation et le graphisme, c'est avec plaisir qu'il "
                + "a accepté de rejoindre les rangs du staff pour travailler le "
                + "design de VoxNucleus.");
        hugo_builder.append("<h3>Contact</h3>");
        hugo_builder.append("Pour le contacter envoyer un mail à "
                + "<a href=\"mailto:hugo@voxnucleus.fr\">hugo@voxnucleus.fr</a>");
    }

}
