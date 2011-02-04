package FallingDown.request.interestingpost;

import java.util.ArrayList;
import java.util.HashMap;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.util.HashFilterPost;
import me.FallingDownLib.CommonClasses.xml.HashToXML;
import me.FallingDownLib.functions.interestingposts.InterestingResults;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class GetInterestingEntries {
    private HashMap<String, HashMap<String,String> >  result;
    private ArrayList<String> listId;

    public GetInterestingEntries(int beginning, int numberToRetrieve, String category, String sub_category) {
        InterestingResults interesting_res=InterestingResults.getList(category, sub_category);
        listId = interesting_res.getList(beginning, beginning +numberToRetrieve);
    }

    private void compileInformationsFromPostId() throws
            PoolExhaustedException, PostDoesNotExist, TException, Exception {
        result = new HashMap<String, HashMap<String, String>>();
        for (int array_index = listId.size()-1; array_index >= 0; array_index--) {
            HashFilterPost filter = new HashFilterPost(Post.getPostFromDatabase(listId.get(array_index)));
            result.put(Integer.toString(array_index), filter.getHashBack());
        }
    }

    /**
     * Converts a given list of Posts to XML
     * @return The result at a String in XML format (See doc)
     */
    protected String convertPostsToXML() throws PoolExhaustedException,
            PostDoesNotExist, TException, Exception {
        compileInformationsFromPostId();
        HashToXML hash = new HashToXML();
        hash.PostHashMapToXML(result);
        return hash.getString();
    }
}
