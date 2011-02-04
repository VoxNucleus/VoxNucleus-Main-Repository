package FallingDown.request.relatedPosts;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.PostFields;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class SearchRelatedPosts {

    private String postId;
    private int numberToRetrieve;
    private String tags;
    private String title;
    private ArrayList<String> searchableArray;

    /**
     *  Constructor
     * @param id
     * @param numberAsked
     */

    public SearchRelatedPosts(String id, int numberAsked){
        postId=id;
        numberToRetrieve = numberAsked;
        searchableArray = new ArrayList<String>();
        fetchTagsFromDatabase();
    }




    private String fetchTagsFromDatabase(){
        FallingDownConnector connector=null;
        try {
            connector = new FallingDownConnector();
            title = StringUtils.string(connector.getInfoColumnWithkey(postId, PostFields.DB_TITLE, FallingDownConnector.DB_POSTS));
            tags = StringUtils.string(connector.getInfoColumnWithkey(postId, PostFields.DB_TAGS, FallingDownConnector.DB_POSTS));
            analyseTags(tags);
            if(searchableArray.size()<1)
                analyseTitle(title);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(SearchRelatedPosts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(SearchRelatedPosts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SearchRelatedPosts.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(connector!=null)
                connector.release();
        }
        return null;
    }

    

    public ArrayList<String> getRelatedPostId(){
        return null;
    }

    /**
     * Search in tags
     * @param tags
     */

    private void analyseTags(String tags) {
        String[] temp= tags.split(",");
        if(temp.length>0)
            for(int index=0;index<temp.length;index++)
                this.searchableArray.add(temp[index]);
    }

    /**
     * Search inside title
     * @param title
     */

    private void analyseTitle(String title) {
        String[] temp= title.split(" ");
        if(temp.length>0)
            for(int index=0;index<temp.length;index++)
                this.searchableArray.add(temp[index]);
    }

}
