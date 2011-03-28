package FallingDown.user.createUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.prettyprint.cassandra.service.*;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.UnavailableException;

/**
 * @author victork
 */
public class SaveUserToCassandra {
    private User userToInsert;
    private byte[] uuid;

    /**
     * Save all user informations into Cassandra.
     * @param u
     * @throws PoolExhaustedException
     * @throws Exception
     */

    protected SaveUserToCassandra(User u) throws PoolExhaustedException, Exception {
        this.userToInsert=u;
        uuid = EasyUUIDget.getByteUUID();
        HashMap<String, byte[]> mapToInsert=userToInsert.toHashMapDB();
        mapToInsert.put(UserFields.UUID, uuid);
        ArrayList<Column> listCol=ColumnUtil.HashToArrayCol(mapToInsert);
        HashMap<String,List<Column>> toInsert = new HashMap<String,List<Column>>();

        toInsert.put(FallingDownConnector.DB_USERS, listCol);
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            connector.batchInsertColumn(userToInsert.username, toInsert);
            saveUserToNotIndexedDB(connector);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }

    }

    /**
     * Insert in database of newly created things
     * @param connector
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */

    private void saveUserToNotIndexedDB(FallingDownConnector connector)
            throws InvalidRequestException, NotFoundException, UnavailableException, Exception {
        connector.insertInfoColumnWithkey("Users",
                uuid,
                userToInsert.username,
                FallingDownConnector.DB_INDEX_CREATED);
    }


}
