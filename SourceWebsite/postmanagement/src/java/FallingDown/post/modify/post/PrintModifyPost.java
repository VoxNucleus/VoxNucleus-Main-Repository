package FallingDown.post.modify.post;

import FallingDown.post.newpost.PrintNewPost;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintModifyPost implements ToCodeConverter {

    private StringBuilder modifyp_builder;
    private String postId;
    private Pass uPass;
    private PostHash pHash;

    /**
     * 
     */
    protected PrintModifyPost() {
        modifyp_builder = new StringBuilder();
    }

    /**
     *
     * @param pId
     */
    protected PrintModifyPost(String pId, Pass pass) {
        this();
        uPass = pass;
        postId = pId;
    }

    /**
     *
     * @param pId
     * @return
     */
    public static PrintModifyPost getInstance(String pId, Pass pass) {
        return new PrintModifyPost(pId, pass);
    }

    public void attachPostHash(PostHash postHash) {
        pHash = postHash;
    }

    public String getHTMLCode() {
        buildPage();
        return modifyp_builder.toString();
    }

    private void buildPage() {
        modifyp_builder.append(SiteElements.getDoctypeHTML());
        modifyp_builder.append("<html>");
        buildHead();
        modifyp_builder.append("<body>");
        buildBody();
        buildFooter();
        modifyp_builder.append("</body>");
        modifyp_builder.append("</html>");

    }

    private void buildFooter() {
        modifyp_builder.append(SiteElements.displayFooter(uPass));

    }

    /**
     *
     */
    private void buildBody() {

        modifyp_builder.append(SiteElements.displayMenu("", "", "", uPass));
        modifyp_builder.append("<div id=\"container\">");
        modifyp_builder.append("<div id=\"content\">");
        modifyp_builder.append("<h1>Modifier votre noyau</h1>");
        buildModifyForm();
        modifyp_builder.append("</div>");
        modifyp_builder.append("</div>");

    }

    /**
     * Build head of the page
     */
    private void buildHead() {
        modifyp_builder.append("<head>");
        Metadata meta = Metadata.getInstance();
        meta.setDescription("Modifiez votre noyau en quelques secondes.");
        meta.setAdditionnalKeywords("modification, noyau");
        modifyp_builder.append(SiteDecorations.setFavIcon());
        modifyp_builder.append(SiteElements.getCommonCSSStyle());
        modifyp_builder.append(SiteElements.getCommonScripts());
        modifyp_builder.append(SiteElements.getOneColumnStyle());
        modifyp_builder.append(meta.getHTMLCode());
        modifyp_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/newpost_page.css\">");
        modifyp_builder.append("<script type=\"text/javascript\"  src=\"/jsp/postmanagement/modifypost.js\"></script>");
        modifyp_builder.append("<script type=\"text/javascript\" src=\"/jsp/plugins/autoresize.jquery.min.js\"></script>");
        modifyp_builder.append("<title>Modification du noyau ").append(postId).append("</title>");
        modifyp_builder.append("</head>");

    }

    private void buildModifyForm() {
        modifyp_builder.append("<div class=\"info\">");
        modifyp_builder.append("Vous pouvez modifier votre noyau en quelques clicks,"
                + " cependant certaines données ne sont pas pour l'instant modifiables."
                + "Des mises à jour dans le futur seront là pour vous permettre une "
                + "plus grande liberté.");
        modifyp_builder.append("</div>");
        modifyp_builder.append("<form method=\"POST\" action=\"/postmanagement/post/domodify\""
                + " id=\"modification_form\">");
        modifyp_builder.append("<table>");
        modifyp_builder.append("<tr>");
        modifyp_builder.append("<td class=\"left_col\">");
        modifyp_builder.append("Titre");
        modifyp_builder.append("</td>");
        modifyp_builder.append("<td>");
        modifyp_builder.append(PrintNewPost.buildInput(PostFields.HTTP_TITLE,
                PostFields.HTTP_TITLE, "", pHash.getTitle(), "text",
                "required=\"required\" pattern=\".{2,}\""));
        modifyp_builder.append("</td>");
        modifyp_builder.append("</tr>");
        modifyp_builder.append("<tr>");
        modifyp_builder.append("<td class=\"left_col\">");
        modifyp_builder.append("Description");
        modifyp_builder.append("</td>");
        modifyp_builder.append("<td>");
        modifyp_builder.append(PrintNewPost.getBigTextarea(pHash.getDescription()));
        modifyp_builder.append("</td>");
        modifyp_builder.append("</tr>");
        modifyp_builder.append("<tr>");
        modifyp_builder.append("<td class=\"left_col\">");
        modifyp_builder.append("Ajouter des tags");
        modifyp_builder.append("</td>");
        modifyp_builder.append("<td>");
        modifyp_builder.append(PrintNewPost.buildInput(PostFields.HTTP_TAGS,
                PostFields.HTTP_TAGS, "", pHash.getTags(), "text", "required=\"required\" pattern=\".{2,}\""));
        modifyp_builder.append("</td>");
        modifyp_builder.append("</tr>");

        modifyp_builder.append("</table>");

        modifyp_builder.append(PrintNewPost.buildInput(PostFields.HTTP_KEY,
                PostFields.HTTP_KEY, "", postId, "hidden", ""));

        modifyp_builder.append("<p align=\"center\">");
        modifyp_builder.append("<button type=\"submit\">Modifier ce noyau</button>");
        modifyp_builder.append("</p>");
        modifyp_builder.append("</form>");

    }
}
