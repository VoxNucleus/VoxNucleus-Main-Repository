/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FallingDown.request.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.util.HashFilterPost;
import me.prettyprint.cassandra.model.InvalidRequestException;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class PostByUserFetcher {

    private final int MAX_NUMBER_POST_TO_RETRIEVE = 10;
    String id;
    int begin;
    int nbToRetrieve;

    public PostByUserFetcher(String userId, int begin, int end) {
        id = userId;
        this.begin = Math.abs(begin);
        nbToRetrieve = end - begin;
        if (nbToRetrieve > MAX_NUMBER_POST_TO_RETRIEVE || nbToRetrieve < 0) {
            nbToRetrieve = MAX_NUMBER_POST_TO_RETRIEVE;
        }

    }

    /**
     * This method will go & retrieve each post in the list.
     * @return
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */
    public HashMap<String, HashMap<String, String>> getResultFromDatabase() throws PoolExhaustedException, TException, Exception {
        HashMap<String, HashMap<String, String>> result = new HashMap<String, HashMap<String, String>>();
        ArrayList<String> keysToRetrieve = getKeys();
        for (int index = 0; index < keysToRetrieve.size(); index++) {
            HashFilterPost filter = new HashFilterPost(Post.getPostFromDatabase(keysToRetrieve.get(index)));
            result.put(Integer.toString(index), filter.getHashBack());
        }
        return result;
    }

    /**
     * This method get a list of keys that need to be retrieved in order to have the posts by a user
     * TODO integration of a shift
     * @param connector
     * @return
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws NotFoundException
     * @throws TimedOutException
     * @throws TException
     */
    private ArrayList<String> getKeys() throws InvalidRequestException,
            UnavailableException, NotFoundException, TimedOutException, TException, PoolExhaustedException, Exception {
        FallingDownConnector connector = new FallingDownConnector();
        int keyFound = 0;
        int decalage = 0;
        int indexSuperColumn = 0;
        ArrayList<String> listKeys = new ArrayList<String>();
        //TODO : Get only superColumn "listPost".
        // At the moment does not give the right informations (null in fact)
        List<SuperColumn> list = connector.getSuperSliceWithPredicateWithSuperColumn(FallingDownConnector.DB_POST_BY_USER, null, null, id);
        while (keyFound < nbToRetrieve && indexSuperColumn < list.size()) {
            int indexColumn = 0;
            List<Column> tempColList = list.get(indexSuperColumn).columns;
            while (keyFound < nbToRetrieve && indexColumn < tempColList.size()) {
                if (decalage < begin) {
                    listKeys.add(StringUtils.string(tempColList.get(indexColumn).getValue()));
                    keyFound++;
                }
                decalage++;
                indexColumn++;
            }
            indexSuperColumn++;
        }
        connector.release();
        return listKeys;
    }
}
