package me.FallingDownLib.CassandraConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.prettyprint.cassandra.service.*;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class FallingDownConnector {

    // list of Databases that can be used
    public static final String DB_USERS = "Users";
    public static final String DB_POSTS = "Posts";
    public static final String DB_RECENT_POSTS = "Last10KPosts";
    public static final String DB_BEST_POSTS_BY_DATE = "BestByDate";
    public static final String DB_SESSIONS = "Sessions";
    public static final String DB_COMMENTS_BY_POSTS = "listCommentsByPost";
    public static final String DB_POST_BY_USER = "listPostsByUser";
    public static final String DB_VOTES_BY_USER = "listVotesByUser";
    public static final String DB_TAGS = "Tags";

    //List of indexing databases
    public static final String DB_INDEX_CREATED="CreatedNotIndexed";
    public static final String DB_INDEX_MODIFIED="ModifiedNotIndexed";
    public static final String DB_INDEX_DELETED="DeletedNotIndexed";

    //Key for DB_RECENT_POSTS
    public static final String KEY_LAST10K_POSTS="Posts";
    // Keys for DB_TOP_POTS
    public static final String KEY_POSTS_24H = "Best24Hours";
    public static final String KEY_POSTS_1WEEK = "Best1Week";
    public static final String KEY_POSTS_1MONTH = "Best1Month";
    public static final String KEY_POSTS_1YEAR = "Best1Year";
    // Informations about the server
    private Keyspace keyspace;
    CassandraClient client;
    CassandraClientPool pool;
    private static final int CASSANDRA_PORT = 9160;
    private static final String CASSANDRA_KEYSPACE = "FallingDown";
    private static final String CASSANDRA_HOST = "localhost";

    /**
     * Constructor of the class
     * @throws PoolExhaustedException
     * @throws Exception
     * @throws TException
     */

    public FallingDownConnector() throws PoolExhaustedException, Exception, TException {
        client = openCassandraConnection();
        keyspace = client.getKeyspace(CASSANDRA_KEYSPACE);
    }

    /**
     * Opens a connexion to the database.
     * Need to be check on later
     * @return
     * @throws PoolExhaustedException
     * @throws Exception
     */

    private CassandraClient openCassandraConnection() throws PoolExhaustedException, Exception {
        pool = CassandraClientPoolFactory.getInstance().get();
        client = pool.borrowClient(CASSANDRA_HOST, CASSANDRA_PORT);
        return client;
    }

    public byte[] getInfoColumnWithkey(String key, String desiredInfo, String chosen_DB)
            throws InvalidRequestException,
            NotFoundException, UnavailableException, Exception {
        ColumnPath columnPathUser = createColumnPath(chosen_DB, null, desiredInfo);
        return keyspace.getColumn(key, columnPathUser).getValue();
    }

    /**
     * return the timestamp of a Column
     * @param key
     * @param desiredInfo
     * @param chosen_DB
     * @return
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */

    public long getTimestampColumnWithkey(String key, String desiredInfo, String chosen_DB)
            throws InvalidRequestException, NotFoundException, UnavailableException, Exception {
        ColumnPath columnPathUser = createColumnPath(chosen_DB, null, desiredInfo);
        return keyspace.getColumn(key, columnPathUser).getTimestamp();
    }

    /**
     * Insert in a key a Column with ColumnName the String toInsert in the chosen_DB database
     * @param key
     * @param columnName
     * @param toInsert
     * @param chosen_DB
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */
    public void insertInfoColumnWithkey(String key, String columnName,
            String toInsert, String chosen_DB)
            throws InvalidRequestException,
            NotFoundException, UnavailableException, Exception {

        ColumnPath columnPathUser = createColumnPath(chosen_DB, null, columnName);
        keyspace.insert(key, columnPathUser, StringUtils.bytes(toInsert));
    }

    public void insertInfoColumnWithkey(String key, byte[] columnName, String toInsert, String chosen_DB)
            throws InvalidRequestException,
            NotFoundException, UnavailableException, Exception {
        ColumnPath columnPathUser = createColumnPath(chosen_DB, null, columnName);
        keyspace.insert(key, columnPathUser, StringUtils.bytes(toInsert));
    }

    /**
     * Insert in a Column a byte[] toInsert directly to overcome problems with encoding.
     * @param key
     * @param columnName
     * @param toInsert
     * @param chosen_DB
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws Exception
     */
    public void insertInfoColumnWithkey(String key, byte[] columnName, byte[] toInsert, String chosen_DB)
            throws InvalidRequestException,
            NotFoundException, UnavailableException, Exception {
        ColumnPath columnPathUser = createColumnPath(chosen_DB, null, columnName);
        
        keyspace.insert(key, columnPathUser, toInsert);
    }

    public void insertInfoSuperColumnWithKey(String key, byte[] bSuperColumnName, 
            String columnName, String toInsert, String chosen_DB)
            throws InvalidRequestException, UnavailableException, TimedOutException, TException {
        ColumnPath newCP = createColumnPath(chosen_DB, bSuperColumnName, columnName);
        keyspace.insert(key, newCP, StringUtils.bytes(toInsert));
    }

    public void insertInfoSuperColumnWithKey(String key, 
            byte[] bSuperColumnName,byte[] bColumnName, byte[] bToInsert, String chosen_DB)
            throws InvalidRequestException, UnavailableException, TimedOutException, TException {
        ColumnPath newCP = createColumnPath(chosen_DB, bSuperColumnName, bColumnName);
        keyspace.insert(key, newCP, bToInsert);
    }


    public Map<String, List<Column>> getRegularSliceWithRange(String DB,String starting_key,String end_key, int number)
            throws InvalidRequestException, UnavailableException, TimedOutException, TException {
        ColumnParent col = new ColumnParent();
        col.setColumn_family(DB);
        SlicePredicate slicePredicate = new SlicePredicate();
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        sliceRange.setCount(number);
        sliceRange.setReversed(true);
        slicePredicate.setSlice_range(sliceRange);
        KeyRange key_range = new KeyRange();
        key_range.setStart_key(starting_key);
        key_range.setEnd_key(end_key);
        return keyspace.getRangeSlices(col, slicePredicate, key_range);
    }

    public List<Column> getSlice(String chosen_DB, String key) 
            throws InvalidRequestException, NotFoundException, UnavailableException,
            TimedOutException, TException {

        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_DB);

        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        slicePredicate.setSlice_range(sliceRange);
        return keyspace.getSlice(key, col, slicePredicate);
    }

    public List<SuperColumn> getKeySuperColumn(String chosen_DB,String key)throws InvalidRequestException, NotFoundException, UnavailableException,
            TimedOutException, TException {
        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_DB);
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        slicePredicate.setSlice_range(sliceRange);
        return keyspace.getSuperSlice(key, col, slicePredicate);
    }

    public List<Column> getKeyColumn(String chosen_CF,String key)
            throws InvalidRequestException, NotFoundException, UnavailableException, TException, TimedOutException{
        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_CF);
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        slicePredicate.setSlice_range(sliceRange);
        return keyspace.getSlice(key, col, slicePredicate);
    }


    public List<Column> getSliceWithPredicate(String chosen_DB, String name, String key) 
            throws InvalidRequestException, NotFoundException, UnavailableException,
            TimedOutException, TException {
        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_DB);

        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        slicePredicate.setSlice_range(sliceRange);
        if (name != null) {
            slicePredicate.addToColumn_names(StringUtils.bytes(name));
        }
        return keyspace.getSlice(key, col, slicePredicate);
    }

    /**
     * This method returns a superSlice (a list of SuperColumns) for the specified Key
     * if name == null, then the whole vector is returned.
     * @param chosen_DB
     * @param name
     * @param Key
     * @return
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */
    public List<SuperColumn> getSuperSliceWithPredicate(String chosen_DB, String name, String Key) 
            throws InvalidRequestException, NotFoundException, UnavailableException,
            TimedOutException, TException {

        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_DB);
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        if (name != null) {
            slicePredicate.addToColumn_names(StringUtils.bytes(name));
        }
        slicePredicate.setSlice_range(sliceRange);
        return keyspace.getSuperSlice(Key, col, slicePredicate);

    }



    /**
     * Similar than get SuperSliceWithPredicate, but now a specific superColumn is chosen
     * @param chosen_DB
     * @param bSuperColumnName
     * @param bColumnName
     * @param Key
     * @return
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */

    public List<SuperColumn> getSuperSliceWithPredicateWithSuperColumn(String chosen_DB,
            byte[] bSuperColumnName ,byte[] bColumnName, String Key) throws
            InvalidRequestException, NotFoundException, UnavailableException,
            TimedOutException, TException {

        ColumnParent col = new ColumnParent();
        col.setColumn_family(chosen_DB);
        col.setSuper_column(bSuperColumnName);
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        SlicePredicate slicePredicate = new SlicePredicate();
        if (bColumnName != null) {
            slicePredicate.addToColumn_names(bColumnName);
        }
        slicePredicate.setSlice_range(sliceRange);

        return keyspace.getSuperSlice(Key, col, slicePredicate);

    }


    /**
     * Get a superColumn
     * @param chosenDatabase
     * @param key
     * @param superColumn
     * @return Asked SuperColumn
     */

    public SuperColumn getSuperColumn(String chosenDatabase,String key,byte[] superColumn) throws InvalidRequestException, NotFoundException, UnavailableException, TException, TimedOutException{
        ColumnPath colPath = createColumnPath(chosenDatabase, superColumn,(byte[]) null);
        return keyspace.getSuperColumn(key, colPath);
    }


    /**
     * Method which enables one to delete a Column inside a SuperColumn ora key
     * If SuperColumn is set to 0 then I just delete a Column
     * @param chosen_DB
     * @param key
     * @param bSuperColumnName
     * @param bColumnName
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */

    public void deleteColumnInSuperColumn(String chosen_DB, String key, byte[] bSuperColumnName, byte[] bColumnName) throws InvalidRequestException,
            UnavailableException, TimedOutException, TException {
        ColumnPath newCP = createColumnPath(chosen_DB, bSuperColumnName, bColumnName);
        keyspace.remove(key, newCP);
    }


    /**
     * Easily create a custom column for SuperColumnname in byte[] and ColumnName
     * in String
     * @param chosen_DB
     * @param superColumn
     * @param columnName
     * @return
     */
    protected ColumnPath createColumnPath(String chosen_DB, byte[] superColumn, String columnName) {
        ColumnPath columnPath = new ColumnPath(chosen_DB);
        if(superColumn!=null)
            columnPath.setSuper_column(superColumn);
        columnPath.setColumn(StringUtils.bytes(columnName));
        return columnPath;
    }

    /**
     * Easily create a custom column for SuperColumnname in byte[] and ColumnName
     * in String
     * @param chosen_DB
     * @param superColumn
     * @param columnName
     * @return
     */

    protected ColumnPath createColumnPath(String chosen_DB, byte[] superColumn, byte[] columnName) {
        ColumnPath columnPath = new ColumnPath(chosen_DB);
        if(superColumn!=null)
            columnPath.setSuper_column(superColumn);
        columnPath.setColumn(columnName);
        return columnPath;
    }


    /**
     * Get the number of Column inside a key or inside a superColumn (if superColumn is set
     * @param chosen_DB
     * @param bSuperColumn
     * @param key
     * @return
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     */
    public int getCount(String chosen_DB, byte[] bSuperColumn, String key) throws
            InvalidRequestException, UnavailableException, TException, TimedOutException{
        
        ColumnParent colParent = new ColumnParent();
        colParent.setColumn_family(chosen_DB);
        if(bSuperColumn!=null)
            colParent.setSuper_column(bSuperColumn);

        return keyspace.getCount(key, colParent);
    }

    /**
     * Insert a whole bunch of columns into database
     * @param key
     * @param toInsert Column family - Columns
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     */

    public void batchInsertColumn(String key,
            HashMap<String,List<Column>> toInsert)
            throws InvalidRequestException, UnavailableException, TException,
            TimedOutException{
        keyspace.batchInsert(key, toInsert, null);
        
    }
//TODO Create batchInsertColumn in superColu=


    public void batchInsertColumnInSupercolumns(String chosen_DB,
            HashMap<String,List<Column>> toInsert)
            throws InvalidRequestException, UnavailableException, TException,
            TimedOutException{
        keyspace.batchInsert(chosen_DB, toInsert, null);
    }


    /**
     * TODO : Clean
     * Delete all keys, no regards to column values etc...
     * @param chosen_DB
     * @param keys
     */

    public void batchDeleteKeys(String chosen_DB,ArrayList<String> keys)
            throws InvalidRequestException, UnavailableException, TException, TimedOutException{
        BatchMutation mutation = new BatchMutation();
        Deletion del = new Deletion();
        ArrayList<String> database= new ArrayList<String>();
        del.setTimestamp(keyspace.createTimestamp());
        database.add(chosen_DB);
        for(int index=0; index<keys.size();index++){
            mutation.addDeletion(keys.get(index), database,del );

        }
        keyspace.batchMutate(mutation);
    }

    /**
     * TODO : Clean
     * Delete a set of column for a key
     * @param chosen_DB
     * @param key
     * @param columnToDelete
     */

    public void batchDeleteColumns(String chosen_DB, String key,
            ArrayList<byte[]> columnToDelete)
            throws InvalidRequestException, UnavailableException, TException,
            TimedOutException {
        BatchMutation mutation = new BatchMutation();
        ArrayList<String> database = new ArrayList<String>();
        Deletion del = new Deletion();
        SlicePredicate predicate = new SlicePredicate();
        database.add(chosen_DB);
        predicate.setColumn_names(columnToDelete);
        del.setPredicate(predicate);
        del.setTimestamp(keyspace.createTimestamp());
        mutation.addDeletion(key, database, del);
        keyspace.batchMutate(mutation);
    }

    /*public HashMap<String,ArrayList<SuperColumn>> getRangeSuperSlice(String chosen_ColumnFamily,
            String key,byte[] startingSuperCol,int numberToRetrieve){
        ColumnParent clp = new ColumnParent(chosen_ColumnFamily);
        SlicePredicate slPred = new SlicePredicate();

        SliceRange sRange = new SliceRange();
        sRange.setCount(numberToRetrieve);
        return keyspace.getSuperRangeSlices(clp, null, null);
    }*/


    /**
     * @param chosen_DB
     * @param key
     * @param columnToDelete
     * @param superColHost
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     */

    public void batchDeleteColumnsInSuperColumns(String chosen_DB, String key,
            HashMap<byte[],ArrayList<byte[]>> columnsInSupercol) throws InvalidRequestException, UnavailableException, TException, TimedOutException{
        BatchMutation mutation = new BatchMutation();
        ArrayList<String> database = new ArrayList<String>();
        database.add(chosen_DB);
        Iterator<byte[]> supercolIterator = columnsInSupercol.keySet().iterator();
        while(supercolIterator.hasNext()){
            Deletion del = new Deletion();
            byte[] supercolumn =supercolIterator.next();
            SlicePredicate predicate = new SlicePredicate();
            predicate.setColumn_names(columnsInSupercol.get(supercolumn));
            del.setPredicate(predicate);
            del.setSuper_column(supercolumn);
            del.setTimestamp(keyspace.createTimestamp());
            mutation.addDeletion(key, database, del);
        }
        keyspace.batchMutate(mutation);
    }


    public void batchDeleteSuperColumns(String chosen_DB, String key,
            ArrayList<byte[]> superColumnToDelete )
            throws InvalidRequestException, UnavailableException, TException, TimedOutException {
        BatchMutation mutation = new BatchMutation();
        
        for(int index=0; index< superColumnToDelete.size();index++){
            Deletion del = new Deletion();
            del.setSuper_column(superColumnToDelete.get(index));
            mutation.addDeletion(key, null, del);
        }

        keyspace.batchMutate(mutation);
    }

    /**
     *
     * @param chosen_ColumnFamily
     * @param starting_Key
     * @param numberToRetrieve
     * @return numberToRetrieve
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     */
    public Map<String, List<Column>> getRangeSlices(String chosen_ColumnFamily,
            String starting_Key, int numberToRetrieve)
            throws InvalidRequestException, UnavailableException, TException, TimedOutException {
        ColumnParent clp = new ColumnParent(chosen_ColumnFamily);
        SlicePredicate sp = new SlicePredicate();
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        //sp.setColumn_names(new ArrayList<byte[]>())
        sp.setSlice_range(sliceRange);
        KeyRange keyRange = new KeyRange();
        keyRange.setCount(numberToRetrieve);

        if (org.apache.commons.lang.StringUtils.isBlank(starting_Key)) {
            keyRange.setStart_key("");
        } else {
            keyRange.setStart_key(starting_Key);
        }
        keyRange.setEnd_key("");
        return keyspace.getRangeSlices(clp, sp, keyRange);
    }



    /**
     *
     * @return Timestamp
     */

    public long getTimestamp(){
        return keyspace.createTimestamp();
    }

    /**Must be called just after cassandra calls
     * 
     */

    public void release(){
        try {
            if (client != null) {
                pool.releaseClient(client);
            }
        } catch (Exception e) {
            //Nothing should happen here either
        }
    }


}
