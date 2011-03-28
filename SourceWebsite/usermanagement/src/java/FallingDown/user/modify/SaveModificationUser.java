package FallingDown.user.modify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class SaveModificationUser {

    private String userToModify;
    private HashMap<String,String> mapOfModification;

    private SaveModificationUser(String user) {
        userToModify = user;
    }

    /**
     *
     * @param user
     * @return
     */
    public static SaveModificationUser getInstance(String user){
        return new SaveModificationUser(user);
    }

    /**
     *
     * @param mapModification
     */

    public void setHashMapModification(HashMap<String,String> mapModification){
        mapOfModification=mapModification;
    }

    /**
     * TODO : change that, no FallingDownConnector
     * @throws PoolExhaustedException
     * @throws TException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws Exception
     */

    public void doSave()
            throws PoolExhaustedException, TException, InvalidRequestException,
            UnavailableException, TimedOutException, Exception {
        buildHashMap();
        FallingDownConnector connector = null;
        HashMap<String, List<Column>> toInsert = new HashMap<String, List<Column>>();
        toInsert.put(userToModify, buildHashMap());
        try {
            connector = new FallingDownConnector();
            connector.batchInsertColumn(userToModify, toInsert);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }


    /**
     * 
     * @return
     */

    private ArrayList<Column> buildHashMap(){
        return ColumnUtil.HashStringToArrayCol(mapOfModification);
    }

}
