package me.FallingDownLib.CommonClasses.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * Class that destroy data across Cassandra.
 * this class must be called when there is a reference to a post that does not exist, or something else
 * @author victork
 */
public class GarbageCollector {

    private HashMap<String, HashMap<String, ArrayList<byte[]>>> columnCollection;
    private HashMap<String, HashMap<String, ArrayList<byte[]>>> supercolumnCollection;
    private HashMap<String, HashMap<String, HashMap<byte[], ArrayList<byte[]>>>> columnsInSupercolumnCollection;

    /**
     * Constructor.
     * This method just build the first
     */
    public GarbageCollector() {
        columnCollection = new HashMap<String, HashMap<String, ArrayList<byte[]>>>();
        supercolumnCollection = new HashMap<String, HashMap<String, ArrayList<byte[]>>>();
        columnsInSupercolumnCollection = new HashMap<String, HashMap<String, HashMap<byte[], ArrayList<byte[]>>>>();
    }


    /**
     *  Method that flush all columns and supercolums
     * @param connector
     */

    public void flushGarbage(FallingDownConnector connector) {
        try {
            if (connector == null) {
                connector = connectToDatabase();
            }
            flushColumns(connector);
            flushSuperColumns();
            flushColumnsInSupercolumns(connector);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    private FallingDownConnector connectToDatabase() throws PoolExhaustedException, TException, Exception {
        FallingDownConnector connector = null;
        connector = new FallingDownConnector();
        return connector;
    }

    private void flushColumns(FallingDownConnector connector) {

        if (!columnCollection.isEmpty()) {
            Iterator<String> colFamIterator = columnCollection.keySet().iterator();
            while (colFamIterator.hasNext()) {
                String colFamily = colFamIterator.next();
                HashMap<String, ArrayList<byte[]>> setKeyToDelete = columnCollection.get(colFamily);
                browseKeys(colFamily, setKeyToDelete, connector);
            }
        }

    }

    /**
     * Go across keys
     * @param columnFamily
     * @param setKeyToDelete
     * @param connector
     */
    private void browseKeys(String columnFamily, HashMap<String, ArrayList<byte[]>> setKeyToDelete,
            FallingDownConnector connector) {
        if (!setKeyToDelete.isEmpty()) {
            Iterator<String> keyIterator = setKeyToDelete.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                destroyColumns(columnFamily, key, setKeyToDelete.get(key), connector);
            }
        }
    }

    /**
     * Call FallingDownConnector
     * @param columnFamily
     * @param key
     * @param columns
     * @param connector
     */
    private void destroyColumns(String columnFamily, String key,
            ArrayList<byte[]> columns, FallingDownConnector connector) {

        //TODO Implement exceptions
        try {
            connector.batchDeleteColumns(columnFamily, key, columns);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void flushSuperColumns() {
    }

    private void flushColumnsInSupercolumns() {
    }

    /**
     * Add one column to the garbage
     * @param columnFamily
     * @param key
     * @param supercolumn
     * @param column
     */
    public void addColumnToDelete(String columnFamily, String key, byte[] column) {
        if (!columnCollection.containsKey(columnFamily)) {
            HashMap<String, ArrayList<byte[]>> setOfKeys = new HashMap<String, ArrayList<byte[]>>();
            ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
            setOfColumns.add(column);
            setOfKeys.put(key, setOfColumns);
            columnCollection.put(columnFamily, setOfKeys);
        } else {
            if (!columnCollection.get(columnFamily).containsKey(key)) {
                ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
                setOfColumns.add(column);
                columnCollection.get(columnFamily).put(key, setOfColumns);
            } else {
                columnCollection.get(columnFamily).get(key).add(column);
            }
        }
    }

    /**
     * Add one supercolumn to the garbage
     * @param columnFamily
     * @param key
     * @param supercolumn
     * @param column
     */
    public void addSupercolumnToDelete(String columnFamily, String key, byte[] supercolumn) {
        if (!supercolumnCollection.containsKey(columnFamily)) {
            HashMap<String, ArrayList<byte[]>> setOfKeys = new HashMap<String, ArrayList<byte[]>>();
            ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
            setOfColumns.add(supercolumn);
            setOfKeys.put(key, setOfColumns);
            supercolumnCollection.put(columnFamily, setOfKeys);
        } else {
            if (!supercolumnCollection.get(columnFamily).containsKey(key)) {
                ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
                setOfColumns.add(supercolumn);
                supercolumnCollection.get(columnFamily).put(key, setOfColumns);
            } else {
                supercolumnCollection.get(columnFamily).get(key).add(supercolumn);
            }
        }
    }

    /**
     * Add one column contained in a superColumn
     * @param columnFamily
     * @param key
     * @param supercolumn
     * @param column
     */
    public void addColumnInSupercolumnToDelete(String columnFamily, String key, byte[] supercolumn, byte[] column) {
        if (!columnsInSupercolumnCollection.containsKey(columnFamily)) {
            HashMap<String, HashMap<byte[], ArrayList<byte[]>>> setOfKeys = new HashMap<String, HashMap<byte[], ArrayList<byte[]>>>();
            HashMap<byte[], ArrayList<byte[]>> setOfSupercolumns = new HashMap<byte[], ArrayList<byte[]>>();
            ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
            setOfColumns.add(column);
            setOfSupercolumns.put(supercolumn, setOfColumns);
            setOfKeys.put(key, setOfSupercolumns);
            columnsInSupercolumnCollection.put(columnFamily, setOfKeys);
        } else {
            if (!columnsInSupercolumnCollection.get(columnFamily).containsKey(key)) {
                HashMap<byte[], ArrayList<byte[]>> setOfSupercolumns = new HashMap<byte[], ArrayList<byte[]>>();
                ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
                setOfColumns.add(column);
                setOfSupercolumns.put(supercolumn, setOfColumns);
                columnsInSupercolumnCollection.get(columnFamily).put(key, setOfSupercolumns);
            } else {
                if (!columnsInSupercolumnCollection.get(columnFamily).get(key).containsKey(supercolumn)) {
                    ArrayList<byte[]> setOfColumns = new ArrayList<byte[]>();
                    setOfColumns.add(column);
                    columnsInSupercolumnCollection.get(columnFamily).get(key).put(supercolumn, setOfColumns);
                } else {
                    columnsInSupercolumnCollection.get(columnFamily).get(key).get(supercolumn).add(column);
                }

            }
        }
    }

    /**
     * Destroy Columns in SuperColumns
     * @param connector
     */
    private void flushColumnsInSupercolumns(FallingDownConnector connector) {
        Iterator<String> columnFamilyIterator = columnsInSupercolumnCollection.keySet().iterator();
        while (columnFamilyIterator.hasNext()) {
            String columnFamily = columnFamilyIterator.next();
            Iterator<String> keyIterator = columnsInSupercolumnCollection.get(columnFamily).keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                destroyColumnsInSupercolumns(columnFamily, key,
                        columnsInSupercolumnCollection.get(columnFamily).get(key), connector);
            }
        }
    }

    /**
     * Do the destroying part
     * @param columnFamily
     * @param key
     * @param columnsInSupercolumns
     * @param connector
     */

    private void destroyColumnsInSupercolumns(String columnFamily, String key,
            HashMap<byte[], ArrayList<byte[]>> columnsInSupercolumns, FallingDownConnector connector) {

        //TODO Implement exceptions
        try {
            connector.batchDeleteColumnsInSuperColumns(columnFamily, key, columnsInSupercolumns);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(GarbageCollector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
