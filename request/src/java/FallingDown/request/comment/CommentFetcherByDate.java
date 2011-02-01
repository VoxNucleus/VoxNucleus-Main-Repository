package FallingDown.request.comment;

import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.util.HashFilterComment;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * Classes that sends a HashMap of Comments sorted by date
 * @author victork
 */
public class CommentFetcherByDate {

    String postId;
    int debut;
    int fin;
    private final String DB_TO_SEARCH = "listCommentsByPost";
    // Indicate the maximum number of comments to retrieve.
    private final int MAX_NUMBER_COMMENTS_TO_RETRIEVE = 50;

    /**
     * Constructor of the method. The three arguments indicates they key that the program should look for,
     * the numbers of responses it can send;
     * @param id
     * @param deb
     * @param fin
     */
    public CommentFetcherByDate(String id, int deb, int fin) {

        this.postId = id;
        if (deb < fin) {
            this.debut = deb;
            this.fin = fin;
        } else {
            this.fin = deb;
            this.debut = fin;
        }

        // Verify that one can only have MAX_NUMBER_COMMENTS_TO_RETRIEVE / request maximum
        if (fin - deb > MAX_NUMBER_COMMENTS_TO_RETRIEVE) {
            fin = deb + MAX_NUMBER_COMMENTS_TO_RETRIEVE;
        }
    }

    /**
     *
     * Returns a HashMap containing all (fin-deb) comments, organized by date
     * @return
     */
    public HashMap<String, HashMap<String, String>> getHashMapComments()
            throws UnavailableException, NotFoundException,
            InvalidRequestException, TimedOutException, PoolExhaustedException,
            TException, Exception {
        int decalage = 0;

        HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
        List<SuperColumn> listSuperCol = getSuperColumns();
        for (int indexSuperCol = 0; (indexSuperCol < listSuperCol.size()) && decalage < fin; indexSuperCol++) {
            //We wait to reach the required number
            if (indexSuperCol >= debut) {
                List<Column> listCol = listSuperCol.get(indexSuperCol).getColumns();
                HashMap<String, String> temp = new HashMap<String, String>();
                for (int indexCol = 0; (indexCol < listCol.size()) && (decalage < fin); indexCol++) {
                    insertInHash(temp, listCol.get(indexCol).getName(), listCol.get(indexCol).getValue());
                }
                if (temp.size() > 0) {
                    HashFilterComment filter = new HashFilterComment(temp);
                    result.put(Integer.toString(decalage), filter.getHashBack());
                    decalage++;
                }
            }

        }
        return result;
    }
/**
 * insert all but one thing normally
 * @param hash
 * @param colName
 * @param colValue
 */
    private void insertInHash(HashMap<String, String> hash, byte[] colName, byte[] colValue) {
        if (StringUtils.string(colName).equals(CommentFields.UUID)) {
            hash.put(StringUtils.string(colName), EasyUUIDget.toUUID(colValue).toString());

        } else {
            hash.put(StringUtils.string(colName), StringUtils.string(colValue));
        }

    }

    private List<SuperColumn> getSuperColumns() throws TException, 
            PoolExhaustedException, UnavailableException,
            NotFoundException, InvalidRequestException, TimedOutException, Exception {
        FallingDownConnector connector = new FallingDownConnector();
        List<SuperColumn> list = null;
        try {
            list = connector.getSuperSliceWithPredicate(DB_TO_SEARCH, null, postId);
        } finally {
            connector.release();
        }
        return list;

    }
}
