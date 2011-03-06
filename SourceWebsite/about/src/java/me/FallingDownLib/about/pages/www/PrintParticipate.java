package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintParticipate implements ToCodeConverter {

    private StringBuilder participate_page;

    protected PrintParticipate(){
        participate_page = new StringBuilder();
    }

    public static PrintParticipate getInstance(){
        return new PrintParticipate();
    }

    public String getHTMLCode() {
        buildPage();
        return participate_page.toString();
    }

    private void buildPage() {
        participate_page.append("<h1>Comment participer ?</h1>");
        participate_page.append("C'est avec grand plaisir que nous accueillons "
                + "toute proposition d'aide. La première façon de nous "
                + "apporter votre aide gratuite et ne demande pratiquement rien en "
                + "terme de temps : <em> Inscrivez-vous, votez et proposez des "
                + "liens </em>. C'est avant tout sur la participation de chacun "
                + "que repose ce site et c'est en étant sur le site qu'il vivra."
                + " Si le site et sa philosophie vous plaisent alors "
                + "n'hésitez pas à le faire partager à vos amis et faites en sorte "
                + "qu'ils nous rejoignent !<br>");
        participate_page.append("");
        participate_page.append("<h1 id=\"join_dev\">S'investir dans le développement</h1>");
        participate_page.append("<h2>Avertissement avant de débuter</h2>");
        participate_page.append("Avant de vous déclarer enthousiastes et prêt à "
                + "travailler avec notre équipe je vous incite fortement à lire "
                + "ce qui va suivre. En premier lieu votre participation, si vous "
                + "y consentez se fera sur une base de volontariat : nous ne "
                + "sommes pas actuellement en mesure de fournir une rémunération."
                + "Deuxièmement cette participation doit se faire de façon "
                + "entretenue. A de trop nombreuses reprises a-t-on dit \"oui\" "
                + "mais quand commence le travail sérieux on se se désiste. "
                + "Sachez que pour espérer continuer vous devez donc être "
                + "disponible plus de trois soirs par semaine "
                + "et ce pour plus de deux heures par soir.");
        participate_page.append("<h2>Nous contacter</h2>");
        participate_page.append("Si vos compétences se retrouvent dans une des "
                + "catégories citées plus bas et que votre motivation correspond "
                + "à la description plus haut, envoyez un mail à "
                + "<a href=\"mailto:offres@voxnucleus.fr\">offres@voxnucleus.fr</a>");
        participate_page.append("<h2>Les profils dont nous avons besoin</h2>");
        participate_page.append("<h3>Développement JAVA</h3>");
        participate_page.append("Nous sommes à la recherche de personnes intéressées "
                + "et versées dans le développement JAVA. Des connaissances en "
                + "J2EE sont requises.");
        participate_page.append("<h3>Base de données</h3>");
        participate_page.append("Nous recherchons des gens expérimentés dans les "
                + "bases de données. Si vous n'avez jamais travaillé sur les bases "
                + "des données il est peu probable que vous soyez utiles. Nous "
                + "avons besoin de personnes capable de gérer des bases de données "
                + "non relationnelles (NoSQL) ainsi qu'en base de données "
                + "relationnelle (PostGreSQL). S'il advenait que quelqu'un "
                + "connaissant Apache Pig ou autre logiciel de mise en relation "
                + "de données");
        participate_page.append("<h3>Développement Web</h3>");
        participate_page.append("Si vous êtes compétents en javascript/JQuery"
                + ", que vous maîtrisez la syntaxe HTML4/5 et CSS vous serez "
                + "bienvenue dans notre équipe.");
        participate_page.append("<h2>Si vous n'entrez dans aucune catégorie</h2>");
        participate_page.append("Vous êtes toujours motivés ? Vous avez lu "
                + "jusqu'ici ? Bien, c'est peut-être que vous avez quelque chose "
                + "à proposer ! N'hésitez pas à nous faire part de vos idées en "
                + "envoyant un mail à <a href=\"mailto:offres@voxnucleus.fr\">"
                + "offres@voxnucleus.fr</a>");
    }

}
