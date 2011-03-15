package me.FallingDownLib.CassandraConnection.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class IterateOverKey {

    public String colFamily;
    private int nbToRetrieve;
    public String startingKey;

    public IterateOverKey(String columnFamily, int number){
        colFamily=columnFamily;
        nbToRetrieve=number;
        startingKey="";
    }

    /**
     *
     * @param startOnKey not used at the moment
     * @return
     */

    public HashMap<String,List<Column>> getNextSet(String startOnKey){
        FallingDownConnector connector=null;
        HashMap<String, List<Column>> result = null;
        try {
            connector = new FallingDownConnector();
            Map<String, List<Column>> temp_result = connector.getRangeSlices(colFamily, startingKey, nbToRetrieve);
            temp_result.remove(startingKey);
            Iterator<String> hashIterator = temp_result.keySet().iterator();
            //Need better solution ?
            while (hashIterator.hasNext()) {
                startingKey = hashIterator.next();
            }
            result = new HashMap(temp_result);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(IterateOverKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(IterateOverKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(IterateOverKey.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
        return result;
    }
}
