/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.FallingDownLib.CommonClasses;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.cassandra.thrift.Column;
import java.util.List;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostInfo;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.post.TagsAnalyser;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.util.FieldVerificator;
import me.FallingDownLib.CommonClasses.util.ScriptDestroyer;

/**
 * Enables one to verify the validity of a Post
 * @author victork
 */
public class Post {

    public String key;
    public String title;
    public String Description;
    public String link;
    public String author;
    public String language;
    public String imageName;
    public byte[] timeUUID_as_byte;
    public String category;
    public String subcategory;
    public long creation_timestamp;
    public final UUID timeUUID;

    public String hostName;
    public String IPAddress;

    //Change to vector / String[]
    public String tags;
    public ArrayList<String> tagsArray;
    public static final int MAX_NUMBER_LETTERS_DESCRIPTION_POST = 2000;
    public static final int MAX_NUMBER_LETTERS_TITLE_POST = 60;





    /**
     * Create a new post from a HashMap of data
     * @param map
     * @param author
     */
    public Post(HashMap<String, String> map, String author) {
        ScriptDestroyer scriptDestroyer= new ScriptDestroyer(map.get("title"));
        this.title = scriptDestroyer.getPurgedTextBack();
        creation_timestamp=Calendar.getInstance().getTimeInMillis();
        scriptDestroyer.setNewText(map.get("description"));
        this.Description = scriptDestroyer.getPurgedTextBack();
        TagsAnalyser analyser = new TagsAnalyser(map.get("tags"));
        this.tags = analyser.getCleanedString();
        this.tagsArray= analyser.getListTags();
        this.language=map.get("language");
        this.author = author;
        key = createKey();
        timeUUID=EasyUUIDget.getTimeUUID();
        timeUUID_as_byte = EasyUUIDget.asByteArray(timeUUID);
        category=map.get("categories");
        if (isOpinion()) {
            link="null";
        } else {
            scriptDestroyer.setNewText(linkAnalyser(map.get("link")));
            link = scriptDestroyer.getPurgedTextBack();
        }
        subcategory=getSubcategory(category,map);
    }

    /**
     * Analyze all informations of a Post.
     * Throw error if one of them is missing
     * @throws IncorrectPostInfo
     */
    public void validatePost() throws IncorrectPostInfo {
        FieldVerificator.post_verify_title(title);
        FieldVerificator.post_verify_description(Description);
        FieldVerificator.post_verify_language(language);
        FieldVerificator.post_verify_category_subcategory(category, subcategory);
        if ( !isOpinion() && !FieldVerificator.post_verify_http_link(link)) {
            throw new IncorrectPostInfo("L'adresse internet que vous avez entrée"
                    + " est incorrecte...");
        }
    }

    /**
     *
     * @return true if post is opinion
     */

     private boolean isOpinion() {
        if (category.equals("Opinion")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Create unique key that will be inserted into the
     * @return
     */
    private String createKey() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String resultat = sdf.format(cal.getTime()) + standardizeTitle(title);
        return resultat;
    }

     private String getSubcategory(String category,HashMap<String,String> map){
         if(category.equals(Categories.MAIN_CATEGORIES[Categories.CATEGORY_OPINION]))
             return "none";
         return map.get("sub_categories_"+category);

    }

    /**
     * Takes a title and standardize it (removes all non alphanumerical characters
     * @param title
     * @return
     */
    private String standardizeTitle(String title) {
        String standardTitle = title;
        standardTitle = standardTitle.replaceAll("[^a-zA-Z0-9]", "");
        if (standardTitle.length() > 15) {
            return standardTitle.substring(0, 14);
        } else {
            return standardTitle;
        }
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Return a HashMap containing informations about a Post.
     * Warning, some informations may be filtered after for privacy.
     * @param postId
     * @return HashMap containing informations about a post.
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */
    static public HashMap<String, String> getPostFromDatabase(String postId)
            throws PoolExhaustedException, PostDoesNotExist, TException, Exception {
        HashMap<String, String> post = new HashMap<String, String>();
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            List<Column> list = connector.getSlice(FallingDownConnector.DB_POSTS, postId);
            if (!list.isEmpty()) {
                for (int index = 0; index < list.size(); index++) {
                    post.put(StringUtils.string(list.get(index).getName()),
                            columnValue(list.get(index)));
                }
                //For later, count nb of comments
                //connector.getCount(FallingDownConnector.DB_COMMENTS_BY_POSTS, bSuperColumn, postId);
                post.put(CommentFields.NB_COMMENTS, Integer.toString(connector.getCount(FallingDownConnector.DB_COMMENTS_BY_POSTS, null, postId)));

                return post;

            } else {
                throw new PostDoesNotExist("Le post cherché n'existe pas");
            }
        } finally {
            if(connector!=null)
                connector.release();
        }
    }

    /**
     * Converts / returns the value of the column
     * @param col
     * @return the value of the column formatted as a string.
     */
    static private String columnValue(Column col) {
            if (StringUtils.string(col.getName()).equals(PostFields.DB_TIMEUUID)) {
                return EasyUUIDget.toUUID(col.getValue()).toString();
            } else {
                return StringUtils.string(col.getValue());
            }
    }

    /**
     * Put informations about the poster's connexion such as its IP and hostname.
     * @param _IP
     * @param _Host
     */

    public void setConnexionDetails(String _IP, String _Host){
        if(_Host.length()>0){
            this.IPAddress = _IP;
            this.hostName=_Host;
        }else{
            IPAddress=_IP;
            hostName="none";
        }
    }

    /**
     * Analyse a link
     * @param link
     * @return "null" if there is no link, otherwise the link itself.
     */

    private String linkAnalyser(String link) {
        String test = link;
        if(link.length()==0){
            return "null";
        }else{
            return link;
        }
    }

    /**
     * Add 1 to viewCount if it exists, otherwise, it is set to 1
     * @param idPost
     */
    static public void updateViewCount(String idPost) throws PoolExhaustedException, TException, InvalidRequestException, UnavailableException, Exception {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            try {
                long viewCount = (Long.parseLong(StringUtils.string(connector.getInfoColumnWithkey(idPost, "nbViews", FallingDownConnector.DB_POSTS)))) + 1;
                connector.insertInfoColumnWithkey(idPost, "nbViews", Long.toString(viewCount), FallingDownConnector.DB_POSTS);
            } catch (NotFoundException ex) {
                connector.insertInfoColumnWithkey(idPost, "nbViews", "1", FallingDownConnector.DB_POSTS);
            }
        } finally {
            if(connector!=null)
                connector.release();
        }
    }


    /**
     *
     * @return HashMap for batchInsertion
     */

    public HashMap<String,byte[]> toHashMapDB(){
        HashMap<String,byte[]> result = new HashMap<String,byte[]>();
        result.put(PostFields.DB_KEY, StringUtils.bytes(key));
        result.put(PostFields.DB_TITLE, StringUtils.bytes(title));
        result.put(PostFields.DB_DESCRIPTION, StringUtils.bytes(Description));
        result.put(PostFields.DB_LINK, StringUtils.bytes(link));
        result.put(PostFields.DB_AUTHOR, StringUtils.bytes(author));
        result.put(PostFields.DB_LANGUAGE, StringUtils.bytes(language));
        result.put(PostFields.DB_FILENAME, StringUtils.bytes(imageName));
        result.put(PostFields.UUID, timeUUID_as_byte);
        result.put(PostFields.DB_CATEGORY, StringUtils.bytes(category));
        result.put(PostFields.DB_SUB_CATEGORY, StringUtils.bytes(subcategory));
        result.put(PostFields.DB_POSTER_REMOTE_ADDRESS, StringUtils.bytes(hostName));
        result.put(PostFields.DB_POSTER_IP, StringUtils.bytes(IPAddress));
        result.put(PostFields.DB_TAGS, StringUtils.bytes(tags));
        result.put(PostFields.DB_TIMESTAMP_CREATED,StringUtils.bytes(Long.toString(creation_timestamp)));
        result.put(PostFields.DB_NBVOTES, StringUtils.bytes("0"));
        result.put(PostFields.DB_NB_VIEWS, StringUtils.bytes("0"));
        result.put(PostFields.DB_NB_REDIRECT, StringUtils.bytes("0"));
        return result;
    }

}
