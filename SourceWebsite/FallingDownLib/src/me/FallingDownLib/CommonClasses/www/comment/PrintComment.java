package me.FallingDownLib.CommonClasses.www.comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.util.MyPattern;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintComment implements ToCodeConverter {

    private StringBuilder comment_builder;
    private CommentHash cHash;

    /**
     * Base constructor
     */

    protected PrintComment(){
        comment_builder = new StringBuilder();
    }

    /**
     *
     * @param comment
     */

    public PrintComment(CommentHash comment){
        this();
        cHash= comment;
    }
    
    /**
     *
     * @return the html code of the comment
     */
    public String getHTMLCode() {
        buildComment();
        return comment_builder.toString();
    }

    /**
     * Build the comment corpse.
     */

    private void buildComment() {
        comment_builder.append("<div class=\"comment\" id=\"u_").append(cHash.getUUID()).append("\">");
        comment_builder.append("<div class=\"top_comment\">").append(cHash.getAuthor());
        comment_builder.append("</div>");
        comment_builder.append("<div class=\"corpse_comment\">");

        comment_builder.append("<div class=\"title_comment\" >");
        comment_builder.append(cHash.getTitle());
        comment_builder.append("</div>");
        comment_builder.append(MyPattern.filterNewLine(cHash.getText()));
        comment_builder.append("</div>");

        comment_builder.append("<div class=\"bottom_comment\"> <span class=\"date_comment\">");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MyPattern.COMMENT_DATE_PATTERN);
            comment_builder.append(dateFormat.format(new Date(Long.parseLong(cHash.getTimestamp()))));
        } catch (NumberFormatException ex) {
            comment_builder.append("Date inconnue");
        }
        comment_builder.append("</span>");
        comment_builder.append("</div>");
        comment_builder.append("</div>");
    }
}
