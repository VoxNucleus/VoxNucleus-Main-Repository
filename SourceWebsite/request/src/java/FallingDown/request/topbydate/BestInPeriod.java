package FallingDown.request.topbydate;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.thrift.TException;

import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.util.GarbageCollector;
import me.FallingDownLib.CommonClasses.util.HashFilterPost;
import me.prettyprint.cassandra.service.PoolExhaustedException;
/**
 * This class is dedicated to retrieve informations from the database.
 * It is called by the other BestInXXX
 * @author victork
 */
public class BestInPeriod {

    /**
     *
     * @return Two levels HashMap containing all required informations.
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */

    public static HashMap<String, HashMap<String,String> >
            getResultFromDatabase(ArrayList<String> keysToRetrieve,
            String time_filter,String category,  String sub_category) throws Exception{
        HashMap<String, HashMap<String,String>> result = new HashMap<String, HashMap<String,String>>();
        GarbageCollector toDestroy = new GarbageCollector();
        int real_index=0;
        for(int index=0;index<keysToRetrieve.size();index++ ){
            try {
                HashFilterPost filter = new HashFilterPost(Post.getPostFromDatabase(keysToRetrieve.get(index)));
                result.put(Integer.toString(real_index), filter.getHashBack());
                real_index++;
            }catch(PostDoesNotExist post){
                /*toDestroy.addColumnInSupercolumnToDelete(FallingDownConnector.DB_BEST_POSTS_BY_DATE,
                        time_filter+category+sub_category, retrievedColumnInformations.get(index).get("supercolumn"),
                        retrievedColumnInformations.get(index).get("UUID"));*/
            }
        }
        toDestroy.flushGarbage(null);
        return result;
    }
    
}
