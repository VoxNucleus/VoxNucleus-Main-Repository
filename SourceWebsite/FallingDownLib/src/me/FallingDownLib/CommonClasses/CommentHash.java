package me.FallingDownLib.CommonClasses;

import java.util.HashMap;

/**
 * Wrapper that extends an HashMap representation of a comment
 * @author victork
 */
public class CommentHash extends HashMap<String,String>{

    public CommentHash(HashMap<String,String> comment){
        super.putAll(comment);
    }

    /**
     *
     * @return Author of the comment
     */

    public String getAuthor(){
        return super.get(CommentFields.DB_AUTHOR);
    }

    /**
     * 
     * @return Text of the comment
     */

    public String getText(){
        return super.get(CommentFields.DB_TEXT);
    }

    /**
     *
     * @return The id of the post
     */

    public String getRelatedPostId(){
        return super.get(CommentFields.DB_RELATED_POSTID);
    }

    /**
     *
     * @return the title of the comment
     */

    public String getTitle(){
        return super.get(CommentFields.DB_TITLE);
    }

    /**
     *
     * @return String representation of a long timestamp
     */

    public String getTimestamp(){
        return super.get(CommentFields.DB_TIMESTAMP);
    }

    /**
     *
     * @return UUID of the comment
     */

    public String getUUID(){
        return super.get(CommentFields.UUID);
    }

}
