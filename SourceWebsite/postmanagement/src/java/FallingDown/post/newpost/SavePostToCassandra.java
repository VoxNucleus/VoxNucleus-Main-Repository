package FallingDown.post.newpost;

import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.connectors.WidgetConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.Exceptions.PostAlreadyExist;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.CommonClasses.util.LongToByteValue;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import me.voxnucleus.sql.insert.NucleusINSERTOperations;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 * This class help saving a post to all databases of Cassandra.
 * @author victork
 */
public class SavePostToCassandra {


    private static final int GEN_GENERAL=0;
    private static final int GEN_CATEGORY=1;
    private static final int GEN_CATEGORY_SUBCATEGORY=2;

    /**
     * 
     * @param p
     * @throws PoolExhaustedException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws PostAlreadyExist
     * @throws NotFoundException
     * @throws Exception
     */

    protected SavePostToCassandra(Post p) throws PoolExhaustedException, InvalidRequestException,
            UnavailableException,PostAlreadyExist, NotFoundException, Exception {
        FallingDownConnector connector = new FallingDownConnector();
        if (connector.getCount(FallingDownConnector.DB_POSTS, null, p.key) > 0) {
            connector.release();
            throw new PostAlreadyExist("Un post avec un titre");
        } else {
            insertInPermanantDatabase(connector, p);
            insertInLast10KDatabase(connector, p);
            NucleusINSERTOperations.insertNucleus(p.key, p.title, p.author,
                    p.creation_timestamp,p.timeUUID , p.category, p.subcategory, p.category, 0);
            insertInPostByUserDatabase(connector, p);
            insertInTagDatabase(connector,p);
            insertToIndex(connector,p);
            insertInPostByURL(p);
            updateUser(connector,p);
            connector.release();
        }

    }

    /**
     * Insert general informations about a post.
     * Key,
     *
     * TODO : BatchInsert
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     */
    private void insertInPermanantDatabase(FallingDownConnector connector, Post p) throws InvalidRequestException, NotFoundException, UnavailableException, Exception {
        HashMap<String, List<Column>> toInsert = new HashMap<String, List<Column>>();
        toInsert.put(FallingDownConnector.DB_POSTS, ColumnUtil.HashToArrayCol(p.toHashMapDB()));
        connector.batchInsertColumn(p.key, toInsert);
    }

    /**
     * Insert the Post in the database BestPostByDate with key Best24Hours
     * THe post is only staying in this database 24H before getting moved
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */
    private void insertIn24HDatabase(FallingDownConnector connector, Post p) throws InvalidRequestException,
            NotFoundException, UnavailableException, Exception {
        connector.insertInfoSuperColumnWithKey(FallingDownConnector.KEY_POSTS_24H+generateCategoryKeys(p,GEN_GENERAL),
                LongToByteValue.longToByteArray(0), p.key, p.key,
                FallingDownConnector.DB_BEST_POSTS_BY_DATE);

        connector.insertInfoSuperColumnWithKey(FallingDownConnector.KEY_POSTS_24H+p.category+SubCategories.SUB_CATEGORIES[0][0],
                LongToByteValue.longToByteArray(0), p.key, p.key,
                FallingDownConnector.DB_BEST_POSTS_BY_DATE);

        if (!p.category.equals("none")) {
            connector.insertInfoSuperColumnWithKey(FallingDownConnector.KEY_POSTS_24H + p.category + p.subcategory,
                    LongToByteValue.longToByteArray(0), p.key, p.key,
                    FallingDownConnector.DB_BEST_POSTS_BY_DATE);
        }


    }

    /**
     *
     * Insert in temporary Database (limited to 10.000)
     * DANGER : Speed improvement to do by using superColumn and fragmenting the number of Posts
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     */

    private void insertInLast10KDatabase(FallingDownConnector connector, Post p) throws InvalidRequestException, NotFoundException, UnavailableException, Exception {
        connector.insertInfoColumnWithkey("Posts"+Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL]+
                SubCategories.SUB_CATEGORIES[0][0], p.timeUUID_as_byte, p.key, FallingDownConnector.DB_RECENT_POSTS);
        connector.insertInfoColumnWithkey("Posts"+p.category+SubCategories.SUB_CATEGORIES[0][0], p.timeUUID_as_byte, p.key, FallingDownConnector.DB_RECENT_POSTS);
        if (!p.category.equals("none")) {
            connector.insertInfoColumnWithkey("Posts" + p.category + p.subcategory, p.timeUUID_as_byte,
                    p.key, FallingDownConnector.DB_RECENT_POSTS);
        }



    }

    /**
     * Insert in listPostByUser.
     * The key is the author of the post.
     * SuperColumn is listPost (for the moment, need to be changed in the future)
     * Column is the timeUUID of the post
     * Value of Column is the key of the post
     *
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */


    private void insertInPostByUserDatabase(FallingDownConnector connector, Post p) throws InvalidRequestException, UnavailableException, TimedOutException, TException{
        connector.insertInfoSuperColumnWithKey(p.author, 
                StringUtils.bytes("listPost"), p.timeUUID_as_byte, StringUtils.bytes(p.key),
                FallingDownConnector.DB_POST_BY_USER);
    }

    /**
     * This method insert in <code>Tags</code> Column Family using the ArrayList
     * in Post.
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */

    private void insertInTagDatabase(FallingDownConnector connector, Post p) throws InvalidRequestException, NotFoundException, UnavailableException, Exception{
       byte[] column= p.timeUUID_as_byte;
        for(int index=0; index<p.tagsArray.size();index++){
            String keyTemp = p.tagsArray.get(index);
            connector.insertInfoColumnWithkey(keyTemp, column, p.key, FallingDownConnector.DB_TAGS);
        }
    }

    /**
     * 
     * @param p
     * @throws IllegalArgumentException
     * @throws UnavailableException
     * @throws InvalidRequestException
     * @throws PoolExhaustedException
     * @throws NotFoundException
     * @throws TimedOutException
     * @throws TException
     * @throws IllegalStateException
     * @throws Exception
     */
    private void insertInPostByURL(Post p) throws IllegalArgumentException,
            UnavailableException, InvalidRequestException, PoolExhaustedException,
            NotFoundException, TimedOutException, TException, IllegalStateException,
            Exception{
        if (!p.category.equals("Opinion")) {
            WidgetConnector widConnector = WidgetConnector.getInstance(p.link);
            widConnector.saveIntoDatabase(p.timeUUID_as_byte, p.key);
        }
    }

    private void insertToIndex(FallingDownConnector connector, Post p) throws
            InvalidRequestException, NotFoundException, UnavailableException, Exception{
        connector.insertInfoColumnWithkey("Posts", p.timeUUID_as_byte, p.key, FallingDownConnector.DB_INDEX_CREATED);
    }

    /**
     * Update nbPosts in user
     * @param connector
     * @param p
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */

    private void updateUser(FallingDownConnector connector,Post p)
            throws InvalidRequestException, NotFoundException,
            UnavailableException, Exception {
        String nbPosts= StringUtils.string(connector.getInfoColumnWithkey(p.author,
                "nbPosts",FallingDownConnector.DB_USERS));
        connector.insertInfoColumnWithkey(p.author, "nbPosts",
                Integer.toString(Integer.parseInt(nbPosts)+1),
                FallingDownConnector.DB_USERS);
    }


    /**
     * Generate the last part of the Key
     * @param p
     * @param choice chose one of the following code in the class
     * @return
     */

    private String generateCategoryKeys(Post p, int choice){
        StringBuilder keyBuilder =new StringBuilder();
        switch(choice){
            case 0:
                keyBuilder.append(Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL]).append(Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL]);
                break;
            case 1:
                keyBuilder.append(p.category).append(Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL]);
                break;
            case 2:
                if(p.subcategory.equals("none")){
                    keyBuilder.append("");
                }else{
                    keyBuilder.append(p.category).append(p.subcategory);
                }
                break;
        }
        return keyBuilder.toString();
    }

}
