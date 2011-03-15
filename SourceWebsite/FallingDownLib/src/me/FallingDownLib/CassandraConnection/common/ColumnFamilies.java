package me.FallingDownLib.CassandraConnection.common;

/**
 *
 * @author victork
 */
public class ColumnFamilies {

    public static final String DB_USERS = "Users";
    public static final String DB_POSTS = "Posts";
    public static final String DB_RECENT_POSTS = "Last10KPosts";
    public static final String DB_BEST_POSTS_BY_DATE = "BestByDate";
    public static final String DB_SESSIONS = "Sessions";
    public static final String DB_COMMENTS_BY_POSTS = "listCommentsByPost";
    public static final String DB_POST_BY_USER = "listPostsByUser";

    //Votes related CF

    public static final String DB_VOTES_BY_USER = "listVotesByUser";
    public static final String DB_VOTES_BY_POST = "listVotesByPost";
    public static final String DB_VOTE_BY_COMMENT="listVotesByComment";


    //Tag CF
    public static final String DB_TAGS = "Tags";

    //
    public static final String DB_POST_BY_URL="PostByURL";

    //Admin & mod CF

    public static final String DB_MODERATOR="moderators";
    public static final String DB_ADMINISTRATOR="administrators";

    //List of indexing databases
    public static final String DB_INDEX_CREATED="CreatedNotIndexed";
    public static final String DB_INDEX_MODIFIED="ModifiedNotIndexed";
    public static final String DB_INDEX_DELETED="DeletedNotIndexed";


    public static final String DB_REPORTED="reported";
}
