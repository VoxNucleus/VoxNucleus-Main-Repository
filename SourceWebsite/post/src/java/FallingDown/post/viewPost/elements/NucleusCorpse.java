package FallingDown.post.viewPost.elements;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.util.MyPattern;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class NucleusCorpse implements ToCodeConverter {

    private StringBuilder n_corpse_builder;
    private PostHash nucleus_Info;
    private boolean canBeModified = false;

    protected NucleusCorpse() {
        n_corpse_builder = new StringBuilder();
    }

    protected NucleusCorpse(PostHash nuc_Hash) {
        this();
        nucleus_Info = nuc_Hash;
    }

    public void setCanBeModified(boolean can) {
        canBeModified = can;
    }

    public static NucleusCorpse getInstance(PostHash nuc_Hash) {
        return new NucleusCorpse(nuc_Hash);
    }

    public String getHTMLCode() {

        buildCorpse();
        return n_corpse_builder.toString();
    }

    private void buildCorpse() {
        insertPostInformations();
    }

    /**
     * Insert informations about the Post.
     * A span with 30 px width in inserted at the beginning of the description
     * @param data
     */
    private void insertPostInformations() {
        n_corpse_builder.append("<div id=\"").append(nucleus_Info.getKey()).append("\" class=\"post_content\">");
        insertPostHead();
        n_corpse_builder.append("<div id=\"post_description\">");
        n_corpse_builder.append(getRank());
        n_corpse_builder.append("<span style=\"width:30px;display:inline-block;\"></span>").append(MyPattern.filterNewLine(nucleus_Info.getDescription()));
        n_corpse_builder.append("</div>");
        if (!nucleus_Info.isOpinion()) {
            insertLink();
        }
        insertModifyZone();
        n_corpse_builder.append("</div>");
    }

    /**
     * Insert the title
     */
    private void insertPostHead() {
        n_corpse_builder.append("<div class=\"post_head\">");
        n_corpse_builder.append("<div id=\"image\" >").append(insertPostImage()).append("</div>");
        n_corpse_builder.append("<h1><a target=\"_blank\" href=\"/r/").append(nucleus_Info.getKey()).append("\"  >").append(nucleus_Info.getTitle()).append("</a> </h1>");
        constructDate();
        n_corpse_builder.append("<span id=\"author\" >").append("<a href=\"/user/").
                append(nucleus_Info.getAuthor()).append("\">").append(nucleus_Info.getAuthor()).append("</a>").append("</span>");
        n_corpse_builder.append("</div>");

    }

    /**
     * This method provides an easy way to insert an image
     * @param data
     * @return
     */
    private String insertPostImage() {
        if (nucleus_Info.getFilename().equals("null")) {
            return (SiteElements.insertImage("website/post/imagepostdefault.png", "Image par défaut"));
        } else {
            return SiteElements.insertImage("post/" + nucleus_Info.getKey() + "/"
                    + nucleus_Info.getFilename(), "Image de " + nucleus_Info.getKey());
        }
    }

    /**
     *
     */
    private void constructDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MyPattern.POST_DATE_PATTERN);
            n_corpse_builder.append("<i>").append(dateFormat.format(new Date(Long.parseLong(nucleus_Info.getTimestamp())))).append("</i>");
        } catch (NumberFormatException ex) {
            //TODO do nothing
        }
    }

    /**
     *
     */
    private void insertModifyZone() {
        if (canBeModified) {
            n_corpse_builder.append("<span class=\"modify_zone\">");
            n_corpse_builder.append("<a href=\"/postmanagement/modifypost?postId=");
            n_corpse_builder.append(nucleus_Info.getKey()).append("\">Modifier ce noyau</a>");
            n_corpse_builder.append("</span>");
        }
    }

    /**
     * Insert the link
     */
    private void insertLink() {
        n_corpse_builder.append("<div class=\"getting_out\">");
        n_corpse_builder.append("<div class=\"link_to_page\"><a target=\"_blank\" href=\"/r/").append(nucleus_Info.getKey()).append("\"  >Rejoindre la page du lien</a></div>");
        n_corpse_builder.append("<div id=\"link\" >Lien : ").append(nucleus_Info.getLink()).append(" ( ").append(nucleus_Info.getNbRedirect()).append(" clicks)").append("</div>");
        n_corpse_builder.append("</div>");
    }

    /**
     *
     * @return
     */
    public String getRank() {
        return me.FallingDownLib.CommonClasses.www.post.PrintPost.buildRank(
                nucleus_Info.getKey(), nucleus_Info.getNbVotes());
    }
}
