package me.FallingDownLib.CommonClasses;

/**
 *
 * @author victork
 */
public class CommentFields {

    public static final String DB_TITLE="title";
    public static final String DB_TEXT="text";
    public static final String DB_RELATED_POSTID="relatedPostId";
    public static final String DB_AUTHOR="author";
    public static final String DB_NBVOTES="nbVotes";
    public static final String DB_TIMESTAMP="timestamp";
    public static final String DB_SCORE="score";
    /**
     * Fields for SolR
     */
    public static final String INDEX_DOCTYPE="comment";
    public static final String INDEX_DATE_CREATED="c_date_created";
    public static final String INDEX_TITLE="c_title";
    public static final String INDEX_TEXT="c_text";
    public static final String INDEX_RELATED_POSTID="c_relatedPostId";
    public static final String INDEX_AUTHOR="c_author";
    public static final String UUID="uuid";
    public static final String INDEX_NBVOTES="c_nbVotes";
    public static final String INDEX_TIMESTAMP="c_timestamp";

    public static final String HTTP_COMMENT_TEXT="text";
    public static final String HTTP_COMMENT_TITLE="title";

    public static final String NB_COMMENTS="nbComments";


}
