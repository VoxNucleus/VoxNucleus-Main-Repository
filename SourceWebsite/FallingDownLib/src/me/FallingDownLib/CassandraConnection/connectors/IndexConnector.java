package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.prettyprint.cassandra.service.BatchMutation;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class IndexConnector {

    private String columnFamily;
    private String key;

    /**
     *
     * @param columnFamily
     */
    protected IndexConnector(String cf){
        columnFamily=cf;
    }

    public IndexConnector getConnector(String cf){
        return new IndexConnector(cf);
    }

    public void setKey(String key){
        this.key=key;
    }

    public void doDelete(ArrayList<byte[]> col){
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            BatchMutation bm = new BatchMutation();
            bm.addDeletion(key, null, null);
            //ks.batchMutate(null);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(IndexConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
