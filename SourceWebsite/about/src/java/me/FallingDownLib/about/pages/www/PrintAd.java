package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintAd implements ToCodeConverter{

    private StringBuilder ad_builder;

    protected PrintAd(){
        ad_builder = new StringBuilder();
    }
    /**
     *
     * @return instance
     */

    public static PrintAd getInstance(){
        return new PrintAd();
    }

    public String getHTMLCode() {
        buildPage();
        return ad_builder.toString();
    }

    private void buildPage() {
        ad_builder.append("<h1> Vous êtes un particulier</h1>");
        ad_builder.append("<h2> Pourquoi désactiver Adblock et autres antipubs ?</h2>");
        ad_builder.append("Si vous aimez le site tel qu'il est actuellement, "
                + "nous aimerions faire appel à vous pour nous aider à le maintenir "
                + "dans cet état. S'il vous est "
                + "possible de couper les filtres anti-publicités (comme AdBlock),"
                + "cela nous permet de maintenir les serveurs en état. "
                + "Contrairement à beaucoup d'autres sites nous essayons de garder "
                + "discrètes les publicités que vous pourrez trouver. Si vous n'activez pas "
                + "un filtre à publicité votre visite n'en sera pas impactée.<br> "
                + "Si un jour vous observez une publicité qui gêne la navigation "
                + "sur le site nous vous encourageons à écrire un mail à notre "
                + "équipe.");
        ad_builder.append("<h1> Vous êtes une entreprise</h1>");
        ad_builder.append("<h2> Quel type de publicité est susceptible d'être acceptée ?</h2>");
        ad_builder.append("Nous accueillerons avec attention toute offre de publicité "
                + "qui respecte les règles suivantes :");
        ad_builder.append("<ul>");
        ad_builder.append("<li>Elle ne déforme pas l'aspect global des pages du site</li>");
        ad_builder.append("<li>Elle respecte nos règles en matière de respect de confidentialité "
                + "des internautes</li>");
        ad_builder.append("<li>Elle n'est pas à caractère pornographique</li>");
        ad_builder.append("<li>Elle ne nous oblige en rien à infléchir notre "
                + "politique de neutralité envers ce qui est publié sur ce site.</li>");
        ad_builder.append("</ul>");
        ad_builder.append("<h3> Nous contacter</h3>");
        ad_builder.append("<div class=\"ad_contact\">");
        ad_builder.append("Si vous avez une offre ou tout autre proposition à "
                + "nous soumettre n'hésitez pas à nous contacter à l'adresse ci-dessous :");
        ad_builder.append("</div>");
        ad_builder.append("<a href=\"mailto:pub@voxnucleus.fr\">pub@voxnucleus.fr</a>");
    }
}
