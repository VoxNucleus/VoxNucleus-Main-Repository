/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.FallingDownLib.CommonClasses;

import java.util.HashMap;

/**
 * Provides an easy way to get informations from a Post.
 * @author victork
 */
public class PostHash extends HashMap<String,String> {

    public PostHash(HashMap<String, String> map){
        super.putAll(map);
    }

    public String getAuthor(){
        return super.get(PostFields.DB_AUTHOR);
    }

    public String getFilename(){
        return super.get(PostFields.DB_FILENAME);
    }

    public String getKey(){
        return super.get(PostFields.DB_KEY);
    }

    public String getTitle(){
        return super.get(PostFields.DB_TITLE);
    }

    public String getDescription(){
        return super.get(PostFields.DB_DESCRIPTION);
    }

    public String getTimestamp(){
        return super.get(PostFields.DB_TIMESTAMP_CREATED);
    }

    public String getTags(){
        return super.get(PostFields.DB_TAGS);
    }

    public String getLanguage(){
        return super.get(PostFields.DB_LANGUAGE);
    }

    public String getNbRedirect(){
        return super.get(PostFields.DB_NB_REDIRECT);
    }

    /**
     *
     * @return number of votes per Post
     */
    public String getNbVotes(){
        return super.get(PostFields.DB_NBVOTES);
    }

    /**
     *
     * @return number of comments
     */
    public String getNbComments(){
        return super.get(CommentFields.NB_COMMENTS);
    }

    public String getLink(){
        return super.get(PostFields.DB_LINK);
    }

    public String getUUID(){
        return super.get(PostFields.UUID);
    }

    public String getCategory(){
        return super.get(PostFields.DB_CATEGORY);
    }


    public String getSubcategory(){
        return super.get(PostFields.DB_SUB_CATEGORY);
    }

    /**
     *
     * @return true if the post is an opinion
     */
    public boolean isOpinion(){
        if(super.get(PostFields.DB_CATEGORY).equals("Opinion"))
        {
            return true;
        }else{
            return false;
        }
    }

}
