package me.FallingDownLib.CommonClasses.www.post;

import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintPostMini implements ToCodeConverter {

    private StringBuilder mini_post_builder;
    private String postId;
    private String title;
    private String imagePath;
    private String nbVotes;

    /**
     * Default constructor
     */

    private PrintPostMini() {
        mini_post_builder = new StringBuilder();
    }

    /**
     * Constructor
     * @param postId
     * @param title
     * @param nbVotes
     * @param imagePath
     */

    private PrintPostMini(String postId, String title,String nbVotes, String imagePath) {
        this();
        this.postId = postId;
        this.title = title;
        this.imagePath = imagePath;
        this.nbVotes=nbVotes;
    }

    /**
     *
     * @param postId
     * @param title
     * @param nbVotes
     * @param imagePath
     * @return instance of a PrintPostMini
     */

    public static PrintPostMini getInstance(String postId, String title,String nbVotes, String imagePath) {
        return new PrintPostMini(postId, title,nbVotes, imagePath);
    }

    /**
     * build code of the mini post
     */
    private void buildMiniPost() {
        mini_post_builder.append("<div class=\"mini_post\">");
        mini_post_builder.append(SiteElements.insertImage("post/"+postId+"/"+imagePath, postId));
        mini_post_builder.append("<a href=\"/post/").append(postId).append("\">").append(title).append("</a>");
        mini_post_builder.append("<div class=\"nbVotes\">").append(nbVotes).append("</div>");
        mini_post_builder.append("</div>");
    }

    /**
     *
     * @return code for a mini post
     */
    public String getHTMLCode() {
        buildMiniPost();
        return mini_post_builder.toString();
    }
}
