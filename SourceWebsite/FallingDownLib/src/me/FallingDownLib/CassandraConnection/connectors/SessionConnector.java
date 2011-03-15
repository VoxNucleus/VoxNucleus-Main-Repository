package me.FallingDownLib.CassandraConnection.connectors;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.FallingDownSessionFields;
import me.prettyprint.cassandra.service.BatchMutation;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class SessionConnector {

    private String sessionKey;

    /**
     *
     * @param sessionId
     * @return
     */
    public static SessionConnector getConnector(String sessionId){
        return new SessionConnector(sessionId);
    }

    /**
     *
     * @param sessionId
     */
    private SessionConnector(String sessionId) {
        sessionKey=sessionId;
    }

    /**
     * TODO
     */
    public void deleteSession(){

        Connector connector = new Connector();
        ArrayList<byte[]> columnNames =buildSessionColNames();
        try {
            Keyspace ks = connector.getKeyspace();
            BatchMutation mutation = new BatchMutation();
            Deletion del = new Deletion();
            ArrayList<String> database = new ArrayList<String>();
            del.setTimestamp(ks.createTimestamp());
            database.add(ColumnFamilies.DB_SESSIONS);
            mutation.addDeletion(sessionKey, database, del);
            ks.batchMutate(mutation);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SessionConnector.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(connector!=null)
                Connector.releaseClient(connector);
        }
    }

    private ArrayList<byte[]> buildSessionColNames() {
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        result.add(StringUtils.bytes(FallingDownSessionFields.DB_EXPIRATION_DATE));
        result.add(StringUtils.bytes(FallingDownSessionFields.DB_LOGIN));
        result.add(StringUtils.bytes(FallingDownSessionFields.DB_SAVED_IP));
        result.add(StringUtils.bytes(FallingDownSessionFields.DB_SESSION_ID));
        return result;
    }

}
