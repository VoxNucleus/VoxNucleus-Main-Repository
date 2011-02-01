package FallingDown.request.lastentries;

import java.util.ArrayList;
import java.util.HashMap;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.prettyprint.cassandra.service.*;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.util.HashFilterPost;
import me.FallingDownLib.CommonClasses.xml.HashToXML;
import me.FallingDownLib.functions.recent.RetrieveLastEntries;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * Constructeur of the class that reads data in and out
 * At the moment no tag or anything required, will be implemented later
 * It's not him which is going to send XML informations back to the user.
 * @author victork
 */
public class GetLastEntries {
    private HashMap<String, HashMap<String,String> >  result;
    private final ArrayList<String> listId;

    public GetLastEntries(int beginning, int numberToRetrieve,String category,String sub_category) throws TException,
            InvalidRequestException, PoolExhaustedException, UnavailableException, TimedOutException, Exception{
        RetrieveLastEntries lastEntries = new RetrieveLastEntries(beginning,numberToRetrieve,category,sub_category);
        listId = lastEntries.returnResult();
    }

    /**
     *
     * @param connector
     * @throws PoolExhaustedException
     * @throws PostDoesNotExist
     * @throws TException
     * @throws Exception
     */

    private void compileInformationsFromPostId() throws
            PoolExhaustedException, PostDoesNotExist, TException, Exception {
        result = new HashMap<String, HashMap<String, String>>();
        for(int array_index = 0; array_index < listId.size(); array_index++) {
            HashFilterPost filter = new HashFilterPost(Post.getPostFromDatabase(listId.get(array_index)));
            result.put(Integer.toString(array_index), filter.getHashBack());
        }
    }


    /**
     * Converts a given list of Posts to XML
     * @return The result at a String in XML format (See doc)
     */

    protected String convertPostsToXML() throws PoolExhaustedException,
            PostDoesNotExist, TException, Exception{
        compileInformationsFromPostId();
        HashToXML hash = new HashToXML();
        hash.PostHashMapToXML( result);
        return hash.getString();
    }
    
}
