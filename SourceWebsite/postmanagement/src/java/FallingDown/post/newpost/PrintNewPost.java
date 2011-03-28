package FallingDown.post.newpost;

import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.CommonClasses.SupportedLanguages;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.voxnucleus.www.helpers.NucleusCreationHelper;

/**
 *
 * @author victork
 */
public class PrintNewPost implements ToCodeConverter {

    private PostFiller pFill;
    private HttpServletRequest request;
    StringBuilder newpost_Builder;
    private Pass pass;

    /**
     * 
     */
    private PrintNewPost() {
        newpost_Builder = new StringBuilder();
    }

    /**
     * 
     * @param p_link
     * @param p_title
     * @param p_description
     * @param p_tags
     */
    private PrintNewPost(HttpServletRequest in_request) {
        this();
        request = in_request;
        pass = Pass.getPass(request);
        pass.launchAuthentifiate();
    }

    /**
     * 
     * @param in_request
     * @return
     */
    public static PrintNewPost getInstance(HttpServletRequest in_request) {
        return new PrintNewPost(in_request);
    }

    /**
     * 
     */
    private void buildPage() {
        buildHead();
        buildBody();
        buildFooter();
    }

    /**
     * Construct head
     */
    private void buildHead() {
        Metadata meta = Metadata.getInstance();
        meta.setDescription("Créer un noyau pour tout ce qui est intéressant : "
                + "Blog, vidéo, site web, faites montez sa réputation ou simplement "
                + "faites profiter les utilisateurs de vos découvertes.");
        meta.setAdditionnalKeywords("création, nouveau, noyau");
        newpost_Builder.append(SiteElements.getDoctypeHTML());
        newpost_Builder.append("<head>");
        newpost_Builder.append(meta.getHTMLCode());
        newpost_Builder.append(SiteElements.getCommonCSSStyle());
        newpost_Builder.append(SiteElements.getCommonScripts());
        newpost_Builder.append(SiteElements.getOneColumnStyle());
        newpost_Builder.append(SiteDecorations.setFavIcon());
        newpost_Builder.append("<script type=\"text/javascript\" "
                + "src=\"/jsp/postmanagement/postmanagementscript.js\"></script>");
        newpost_Builder.append("<script type=\"text/javascript\" "
                + "src=\"/jsp/postmanagement/image_file.js\"></script>");
        newpost_Builder.append("<script type=\"text/javascript\" "
                + "src=\"/jsp/plugins/autoresize.jquery.min.js\"></script>");
        newpost_Builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/newpost_page.css\">");
        newpost_Builder.append("<title>VoxNucleus : Créer un nouveau noyau</title>");
        newpost_Builder.append("</head>");
    }

    /**
     * Construct body
     */
    private void buildBody() {
        newpost_Builder.append(SiteElements.displayMenu("", "", "", pass));
        newpost_Builder.append("<body>");
        newpost_Builder.append("<div id=\"container\">");
        newpost_Builder.append("<div id=\"content\">");
        newpost_Builder.append("<h1> Nouveau noyau </h1>");
        if (!pass.getIsAuthentified()) {
            newpost_Builder.append("<div id=\"disclaimer\"> Vous n'êtes pas "
                    + "identifié, vous ne pouvez pas créer de noyau... Merci de vous "
                    + "<a href=\"/usermanagement/subscribe\">enregistrer</a> ou "
                    + "de vous <a href=\"/usermanagement/login.jsp\">connecter</a> </div>");
        }

        buildForm();
        newpost_Builder.append("</div>");
        newpost_Builder.append("</div>");
        newpost_Builder.append("</body>");
    }

    private void buildForm() {
        NucleusCreationHelper helper =NucleusCreationHelper.getHelper();
        SupportedLanguages.getInstance();
        newpost_Builder.append("<form id=\"newpost_form\" name=\"post_form\" "
                + "enctype=\"multipart/form-data\" "
                + "action=\"/postmanagement/validatePost\" method=\"POST\">");
        newpost_Builder.append("<table id=\"table_newpost\">");
        newpost_Builder.append(buildTR(buildTDLeft("Catégorie " + helper.getHelpTooltip(PostFields.HTTP_CATEGORY)) + buildTD(Categories.getSELECT_HTML())));
        newpost_Builder.append(buildTR(buildTDLeft("Sous Catégorie "+ helper.getHelpTooltip(PostFields.HTTP_SUB_CATEGORY)) + buildTD(SubCategories.getSELECT_HTML(1)
                + SubCategories.getSELECT_HTML(2) + SubCategories.getSELECT_HTML(3) + SubCategories.getSELECT_HTML(4)
                + SubCategories.getSELECT_HTML(5) + SubCategories.getSELECT_HTML(6))));
        newpost_Builder.append("<tr id=\"link\">");
        newpost_Builder.append(buildTDLeft("Lien "+ helper.getHelpTooltip(PostFields.HTTP_LINK))).append(buildTD(buildInput("input_link",
                "link","",pFill.getURL() ,"url"," required =\"required\"")));
        newpost_Builder.append("</tr>");
        newpost_Builder.append(buildTR(buildTDLeft("Titre"+helper.getHelpTooltip(PostFields.HTTP_TITLE)) +
                buildTD(buildInput("input_title", "title","", pFill.getTitle(), "text","maxlength=\"60\" required=\"required\""))));
        newpost_Builder.append("<tr id=\"input_upload\">");
        newpost_Builder.append(buildTDLeft("Image Associée"+helper.getHelpTooltip(PostFields.HTTP_REMOTE_FILE))).append(buildTD(buildInput("image", "file", "file_uploader", "", "file")));
        newpost_Builder.append("</tr>");
        newpost_Builder.append(buildTR(buildTDLeft("")
                + buildTD("<span style=\"cursor:pointer;"
                + "font-weight:bolder;font-size:90%;color:#281D57;\" class=\"remote_uploader remote\">"
                + "Cliquer pour sélectionner un fichier depuis une adresse</span>")));
        newpost_Builder.append(
                buildTR(
                buildTDLeft("Description"+ helper.getHelpTooltip(PostFields.HTTP_DESCRIPTION))
                + buildTD(getBigTextarea(pFill.getDescription()))));
        newpost_Builder.append(buildTR(buildTDLeft("Langue"+helper.getHelpTooltip(PostFields.HTTP_LANGUAGE) )
                + buildTD(SupportedLanguages.getSELECT_HTML("fr"))));
        newpost_Builder.append(buildTR(buildTDLeft("Tags"+helper.getHelpTooltip(PostFields.HTTP_TAGS)) + buildTD(buildInput("input_tags", "tags","", pFill.getTags(), "text",
                " required=\"required\" pattern=\".{2,}\" "))));
        newpost_Builder.append("</table>");
        newpost_Builder.append("<p align=\"center\"> "
                + "<button id=\"newuser_submit\" type=\"submit\">Créer ce noyau</button> </p>");
        newpost_Builder.append("</form>");
    }

    /**
     * Display footer
     */
    private void buildFooter() {
        newpost_Builder.append(SiteElements.displayFooter(pass));
    }

    /**
     * Build line of the table
     * @param inside
     * @return
     */

    private String buildTR(String inside) {
        return "<tr>" + inside + "</tr>";

    }

    /**
     * build a single column of the table
     * @param inside
     * @return
     */
    private String buildTD(String inside) {
        return "<td>" + inside + "</td>";
    }

    /**
     * Build the left column (that has a special class)
     * @param inside
     * @return
     */
    private String buildTDLeft(String inside) {
        return "<td class=\"left_col\">" + inside + "</td>";
    }

    /**
     * 
     * @param id
     * @param name
     * @param value
     * @param type
     * @return
     */
    public static String buildInput(String id, String name, String value, String type) {
        return buildInput(id, name, "", value, type);
    }

    public static String buildInput(String id, String name, String add_class, String value, String type) {
        return buildInput(id,  name, add_class,  value, type,"");
    }

    public static String buildInput(String id, String name, String add_class, String value, String type,String verificatorInfos) {
        return "<input " + verificatorInfos+ " class=\"postinfo " + add_class + "\" id=\"" + id + "\" name=\"" + name + "\" value=\"" + value + "\" type=\"" + type + "\" >";
    }
    /**
     *
     * @return html code of newPost
     */
    public String getHTMLCode() {
        buildPage();
        return newpost_Builder.toString();

    }

    /**
     * Pattern : pattern=\"[^.]+\"
     * @return textarea for description
     */
    public static String getBigTextarea(String inDescription) {
        return "<textarea required=\"required\" "
                + " class=\"postinfo\" name=\"description\" id=\"textarea_description\""
                + " ROWS=\"5\" COLS=\"70\" >"+inDescription+"</TEXTAREA>";
    }


    public void attachPostFiller(PostFiller postfiller){
        pFill=postfiller;
    }
}
