package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author victork
 */
public class PrintWebMaster implements ToCodeConverter {

    StringBuilder webmast_builder;

    protected PrintWebMaster(){
        webmast_builder= new StringBuilder();
    }

    public static PrintWebMaster getInstance(){
        return new PrintWebMaster();
    }
    
    public String getHTMLCode() {
        buildPage();
        return webmast_builder.toString();
    }

    private void buildPage() {
        webmast_builder.append("<div id=\"presentation\">");
        webmast_builder.append("<h1>Présentation</h1>");
        webmast_builder.append("Sur VoxNucleus nous proposons aux webmaster d'incorporer "
                + "sans aucun frais nos outils. Ils permettent en un click de partager "
                + "des pages sur VoxNucleus, augmentant le traffic ainsi que le "
                + "positionnement dans les recherches Google. De plus, nous nous "
                + "engageons à ne pas distribuer les résultats de l'analyse des requêtes "
                + "qui nous parviennent. La confidentialité de vos visiteurs ainsi que celle "
                + "du site est respectée.");
        webmast_builder.append("</div>");
        webmast_builder.append("<div id=\"integration\">");
        webmast_builder.append("<h1>Intégration sur votre site</h1>");
        webmast_builder.append("L'intégration des outils de VoxNucleus sur un site "
                + "peut se faire de plusieurs façons : la façon la plus simple "
                + "est <a href=\"#automatic_integration\"> est automatique </a>"
                + "ou alors <a href=\"#automatic_integration\"> manuelle </a>.");
        webmast_builder.append("<h2>Intégration manuelle</h2>");
        webmast_builder.append("<div id=\"manual_integration\">");
        webmast_builder.append("La première façon d'intégrer VoxNucleus à votre site "
                + "peut se faire en fournissant un lien directement aux visiteurs.");
        webmast_builder.append("</div>");
        buildJavascriptEncoder();
        webmast_builder.append("<h2>Intégration automatique</h2>");
        webmast_builder.append("<div id=\"automatic_integration\">");
        webmast_builder.append("L'intégration automatique est très simple et se fait "
                + "en moins d'une minute. De plus ce que vous allez ajouter à votre "
                + "page ne sera pas bloquant au niveau du chargement de votre page "
                + "web. Comme toujours, nous nous engageons à ne pas revendre<br>"
                + "les informations tirées de l'utilisation de ce bouton à un tiers.");
        webmast_builder.append("<em> Avant de poursuivre sachez que comme le chargement "
                + "du bouton est asynchrone l'intégration de ce bouton ne sera en aucun"
                + "cas bloquant dans le chargement de la page. Si nos serveurs "
                + "venaient à subir une avarie le bouton et les informations resteront "
                + "invisibles. </em>");
        webmast_builder.append("</div>");
        webmast_builder.append("<h3>Générer le bouton</h3>");
        buildWidgetJavascriptInsert();
        webmast_builder.append("Une fois que le script a été inséré il faut déterminer"
                + " l'emplacement où le bouton apparaîtra, vous pouvez le générer avec "
                + "ce qui est dessous. Il vous suffit de placer le code à l'endroit "
                + "où vous souhaitez avoir le bouton.");
        buildWidgetGeneratorZone();
        webmast_builder.append("<h3>Type de boutons</h3>");
        webmast_builder.append("Pour l'instant il n'y a qu'un seul type de bouton "
                + "qui existe, mais revenez pour voir les mises à jour.<br>");
        webmast_builder.append("<b>Icône : </b> Petite icône qui peut se placer dans "
                + "n'importe quel page, de façon très simple."
                + "<br>");

        buildExplicationZone();
        webmast_builder.append("<h3>Compatibilité</h3>");
        webmast_builder.append("Ce script a été testé et fonctionne avec les "
                + "navigateurs suivants :");
        webmast_builder.append("<ul class=\"compatible_browser\">");
        webmast_builder.append("<li>");
        webmast_builder.append("Internet Explorer 6+ (oui c'était difficile)");
        webmast_builder.append("</li>");
        webmast_builder.append("<li>");
        webmast_builder.append("Firefox 2+");
        webmast_builder.append("</li>");
        webmast_builder.append("<li>");
        webmast_builder.append("Chrome 5+");
        webmast_builder.append("</li>");
        webmast_builder.append("<li>");
        webmast_builder.append("Opera 10+");
        webmast_builder.append("</li>");
        webmast_builder.append("<li>");
        webmast_builder.append("Safari 4+");
        webmast_builder.append("</li>");
        webmast_builder.append("</ul>");
        webmast_builder.append("Si vous remarquez des compatibilités ou incompatibilités "
                + "avec d'autres navigateurs vous pouvez contacter l'équipe pour "
                + "nous donner de plus amples informations.");
        webmast_builder.append("<h3>Avertissement</h3>");
        webmast_builder.append("<div id=\"automatic_integration_disclaimer\">");
        webmast_builder.append("<em>Bien que nous fournissons gratuitement ce bouton "
                + "à tout le monde nous ne disposons pas de serveurs très puissants "
                + "et il pourra y avoir des interruptions de service ainsi que des "
                + "changements dans le code. Ces changements n'entraîneront normalement "
                + "pas de nécessité de changer le code donné ici, mais il est préférable "
                + "que vous reveniez sur ce site de temps à autre pour vous tenir"
                + "informé des évolutions du site.<br>"
                + "Nous vous remercions pour votre compréhension.</em>");
        webmast_builder.append("</div>");

        webmast_builder.append("</div>");
    }

    private void buildJavascriptEncoder(){
        webmast_builder.append("<div id=\"manual_integration_javascript\">");
        webmast_builder.append("<table>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Titre ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_TITLE,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Adresse ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_LINK,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Description ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_DESCRIPTION,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Tags ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_TAGS,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<td>").append("Partager ").append("</td>");
        webmast_builder.append("<td>").append("<input readonly=\"readonly\" id=\"result_share\" >").append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("</table>");
        webmast_builder.append("</div>");
    }

    private void buildWidgetGeneratorZone(){
        webmast_builder.append("<div id=\"automatic_integration_javascript\">");
        webmast_builder.append("<span class=\"script_title\">Générer un bouton automatiquement</span>");

        webmast_builder.append("<table>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Titre ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_TITLE,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Lien ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_LINK,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Description ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_DESCRIPTION,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Tags ").append("</td>");
        webmast_builder.append("<td>").append(buildInput(PostFields.HTTP_TAGS,"")).append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("<tr>");
        webmast_builder.append("<td>").append("Type de bouton ").append("</td>");
        webmast_builder.append("<td>").append("<select class=\"postinfo\">"
                + "<option value=\"icone\">Icône</option>"
                + "</select>").append("</td>");
        webmast_builder.append("</tr>");
        webmast_builder.append("</table>");
        webmast_builder.append("<div id=\"integration_result\">");
        webmast_builder.append("<div id=\"integration_title\">");
        webmast_builder.append("Copiez ce texte et insérez-le dans la structure "
                + "de votre site :");
        webmast_builder.append("</div>");

        webmast_builder.append("<div id=\"integration_text\" class=\"tocopy\">");
        webmast_builder.append("Texte à copier (changera automatiquement quand "
                + "lorsque vous cliquez ou tapez dans les options)");
        webmast_builder.append("<button style=\"text-align:center\" onClick=\"replaceIntegrationText()\"> Cliquez pour afficher le bouton sans option</button>");
        webmast_builder.append("</div>");
        webmast_builder.append("</div>");
        webmast_builder.append("</div>");
    }

    private void buildWidgetJavascriptInsert() {
        webmast_builder.append("Avant toute chose il est nécessaire d'intégrer "
                + "ce script dans <em>toutes</em> les pages où les boutons sont "
                + "à mettre.");
        webmast_builder.append("<div class=\"tocopy\">");
        webmast_builder.append(StringEscapeUtils.escapeHtml("<script type=\"text/javascript\">"));
        webmast_builder.append("<br>").append("(function(){ ").append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml(" var vn = document.createElement('SCRIPT'),")).append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("vnhand = document.getElementsByTagName('SCRIPT')[0];"));
        webmast_builder.append("<br>").append("vn.type=\'text/javascript\';").append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("vn.async=true;"));
        webmast_builder.append("<br>").append("vn.src='http://widget.voxnucleus.fr/boutons.js';").append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml(" vnhand.parentNode.insertBefore(vn,vnhand);"));
        webmast_builder.append("<br>").append(StringEscapeUtils.escapeHtml("  })();"));
        webmast_builder.append("<br>").append(StringEscapeUtils.escapeHtml(" </script>"));
        webmast_builder.append("</div>");
    }

    private String buildInput(String name,String verificatorInfos) {
        return "<input " + verificatorInfos+ " class=\"postinfo "+ "\" name=\"" + name +"\" >";
    }

    private void buildExplicationZone() {
        webmast_builder.append("<h3>Fonctionnement</h3>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("S'il est possible de générer automatiquement le "
                + "bouton il est aussi tout à fait possible de le faire manuellement.")).append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("La première chose à remarquer est que la totalité du bouton est contenu "
                + "à intérieur d'une balise ancre (anchor) <a>. La classe "
                + "indique au script que c'est un bouton de VoxNucleus tandis que "
                + "la seconde classe donne le type de bouton qui est demandé.")).append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("A l'intérieur de la balise <a> (anchor) on trouve les classes "
                + "\"vn_data\" (VoxNucleus Data) qui indique que ce que contient "
                + "le span est à utiliser et une seconde classe qui indique "
                + "le type de données stockées. On retrouve donc : vn_title, vn_link,"
                + " vn_tags, vn_description. Enfin le style=\"display:hidden\" fais en sorte que "
                + "les données restent invisibles pour le lecteur afin de ne pas "
                + "géner sa lecture."));
        webmast_builder.append("<h4>Les options du bouton</h4>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("Il existe une seule option réellement importante "
                + "si vous êtes le créateur de la page : la zone \"adresse\". En effet "
                + "le script par défaut ira chercher l'adresse contenue dans la barre d'adresse"
                + " du browser. Or il peut arriver que la page ait des arguments (? &)."
                + " En remplissant la zone adresse vous pouvez désactiver ce comportement "
                + "par défaut et indiquer d'aller chercher une page en particulier.")).append("<br>");
        webmast_builder.append(StringEscapeUtils.escapeHtml("Les autres boutons permettent de préremplir les champs lors"
                + "de la publiciation par les visiteurs de VoxNucleus."));

    }

}
