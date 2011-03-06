package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintVictorKabdebon implements ToCodeConverter{

    private StringBuilder victor_k_builder;

    protected PrintVictorKabdebon(){
        victor_k_builder = new StringBuilder();
    }

    /**
     *
     * @return the instance
     */
    public static PrintVictorKabdebon getInstance(){
        return new PrintVictorKabdebon();
    }

    /**
     *
     * @return html code
     */

    public String getHTMLCode() {
        buildPage();
        return victor_k_builder.toString();
    }

    /**
     *
     */
    private void buildPage() {
        victor_k_builder.append("<h1>Description</h1>");
        victor_k_builder.append("Etudiant en double diplôme à Telecom SudParis "
                + "et à Georgia Tech, je suis passionné par les nouvelles "
                + "technologies et je suis l'actualité de très près. Cette "
                + "passion m'a amené à créer ce site. ");
        victor_k_builder.append("<h2>Compétences techniques</h2>");
        victor_k_builder.append("JAVA, C++, Ruby, HTML, Javascript, CSS et bien d'autres");
        victor_k_builder.append("<h3>Sites web</h3>");
        victor_k_builder.append("<a href=\"http://victor-kabdebon.blogspot.com/\">"
                + "Blog (Technique)</a><br>");
        victor_k_builder.append("<a href=\"http://fr.linkedin.com/pub/victor-kabdebon/"
                + "13/15/194\">Profil Linkedin</a>");
        victor_k_builder.append("<h3>Contact</h3>");
        victor_k_builder.append("Pour le contacter envoyer un mail à "
                + "<a href=\"mailto:victor@voxnucleus.fr\">victor@voxnucleus.fr</a> ");

    }
}
