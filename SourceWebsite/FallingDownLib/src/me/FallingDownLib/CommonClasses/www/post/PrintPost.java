package me.FallingDownLib.CommonClasses.www.post;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.util.MyPattern;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * Provide HTML code for
 * @author victork
 */
public class PrintPost implements ToCodeConverter {

    StringBuilder postBuilder;

    public static int BEGIN_INDEX_SHORT_URL=7;
    public static int MAX_NUMBER_CHARACTERS=400;

    /**
     * Default constructor. Not called from the outside
     */
    protected PrintPost() {
        postBuilder = new StringBuilder();
    }

    /**
     * Constructor with all the data
     * @param postId
     * @param title
     * @param description
     * @param author
     * @param imageName
     * @param nbComments
     * @param nbVotes
     */
    public PrintPost(String postId, String title, String description, String author,
            String link, String imageName, String nbComments, String nbVotes) {
        this();
        buildPost(postId, title, description, author, link, imageName, nbComments, nbVotes);
    }

    /**
     * 
     * @param pHash
     */
    public PrintPost(PostHash pHash) {
        this(pHash.getKey(), pHash.getTitle(), pHash.getDescription(), pHash.getAuthor(), pHash.getLink(),
                pHash.getFilename(), pHash.getNbComments(), pHash.getNbVotes());
    }

    /**
     * build post
     * @param postId
     * @param title
     * @param description
     * @param author
     * @param link
     * @param imageName
     * @param nbComments
     * @param nbVotes
     */
    private void buildPost(String postId, String title, String description,
            String author, String link, String imageName, String nbComments, String nbVotes) {
        postBuilder.append("<div class=\"post\" id=\"p_").append(postId).append("\" >");
        postBuilder.append(buildRank(postId, nbVotes));
        buildPostContent(postId, description, title,link, imageName);
        buildBottom(author, nbComments);
        postBuilder.append("</div>");

    }

    /**
     * build rank
     * @param postId
     * @param nbVotes
     */
    public static String buildRank(String postId, String nbVotes) {
        StringBuilder rankbuilder = new StringBuilder();
        rankbuilder.append("<div class=\"rank\">");
        rankbuilder.append("<div class=\"nbVotes\"><div class=\"score\">");
        rankbuilder.append(nbVotes).append("</div>");
        rankbuilder.append("<div class=\"text_vote\">Votes</div>").append("</div>");
        rankbuilder.append("<div class=\"vote_zone\">").
                append("<a rel=\"#logsub\" quicklogin=\"/usermanagement/quicklogin\" onclick=\"doVote(\'").
                append(postId).
                append("\',this)\"> Voter </a>").
                append("</div>");
        rankbuilder.append("</div>");
        return rankbuilder.toString();

    }

    public static String buildNbVotesOnly(String postId, String nbVotes){
        StringBuilder rankbuilder = new StringBuilder();
        rankbuilder.append("<div class=\"rank\">");
        rankbuilder.append("<div class=\"nbVotes\"><div class=\"score\">");
        rankbuilder.append(nbVotes).append("</div>");
        rankbuilder.append("<div class=\"text_vote\">Votes</div>").append("</div>");
        rankbuilder.append("</div>");

        return rankbuilder.toString();
    }

    /**
     * Build content
     * @param postId
     * @param description
     * @param title
     * @param imageName
     */
    private void buildPostContent(String postId, String description,
            String title, String link, String imageName) {
        postBuilder.append("<div class=\"post_content\">");
        buildImageLink(postId, link, imageName);
        postBuilder.append("<a href=\"/post/").append(postId).append("\">");
        postBuilder.append(title).append("</a>");
        postBuilder.append("<div class=\"description\">")
                .append("<a href=\"/post/");
        postBuilder.append(postId).append("\">").append(getShortenedURL(link))
                .append("<span class=\"ellipsis_text\">");
        postBuilder.append(description.substring(0,
                Math.min(description.length(), MAX_NUMBER_CHARACTERS)));
        postBuilder.append("</span></a>").append("</div>");//TODO finish that
        postBuilder.append("</div>");

    }

    /**
     * Include image of post
     * @param postId
     * @param imageName
     */
    public static String buildPostImage(String postId, String imageName) {
        if (imageName.equals("null")) {
            return ("<img src=\"/image/website/post/imagepostdefault.png\">");
        } else {
            return ("<img src=\"/image/post/" + (postId)+"/"+imageName+"\">");
        }
    }


    private void buildImageLink(String postId, String link, String imageName) {
        if (link.equals("null")) {
            postBuilder.append("<a href=\"/r/").append(postId).append("\">");
            postBuilder.append(buildPostImage(postId, imageName));
            postBuilder.append("</a>");
        } else {
            postBuilder.append("<a href=\"/r/").append(postId).append("\" target=\"_blank\" >");
            postBuilder.append(buildPostImage(postId, imageName));
            postBuilder.append("</a>");
        }
    }

    /**
     * build div bottom
     * @param author
     * @param nbComments
     */
    private void buildBottom(String author, String nbComments) {
        postBuilder.append("<div class=\"bottom\">");
        postBuilder.append("<div class=\"author\">").append("<a href=\"/user/").append(author).append("\">").append(author).append("</a>").append("</div>");
        postBuilder.append("<div class=\"comments\">").append(nbComments).append(" commentaires </div>");
        postBuilder.append("</div>");
    }


    private String getShortenedURL(String url) {
        if (!url.equals("null")) {
            String short_url = "<span style=\"color:grey;font-size:smaller;margin-right:10px\" class=\"short_url\">";
            Pattern URLshortenerPattern = Pattern.compile(MyPattern.HTTP_LINK_SHORTENER,Pattern.CASE_INSENSITIVE);
            Matcher URLshortenerMatch = URLshortenerPattern.matcher(url);
            if (URLshortenerMatch.find()) {
                short_url+=URLshortenerMatch.group(0).substring(BEGIN_INDEX_SHORT_URL);
            } else {
                short_url += url.substring(BEGIN_INDEX_SHORT_URL);
            }
            short_url += "</span>";
            return short_url;
        } else {
            return "";
        }
    }


    /**
     *
     * @return HTML code for a post
     */
    public String getHTMLCode() {
        return postBuilder.toString();
    }


}
