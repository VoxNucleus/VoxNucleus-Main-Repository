package me.FallingDownLib.functions.interestingposts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.DatabaseUtil;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.interfaces.database.SavableToCassandra;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class InterestingPostList extends ConcurrentHashMap<String,InterestingPostEntry>
        implements Serializable, SavableToCassandra {


    private String category;
    private String sub_category;
    private long timestampLastTimeSaved;
    private final long MIN_INTERVAL_REFRESH = 10l * 60l * 1000l;
    private static ConcurrentHashMap<String, ConcurrentHashMap<String,InterestingPostList>> instance = null;
    public static final String KEY_INTERESTING_POST_LIST = "InterestingPostList";
    private static final String COLUMN_INTERESTING_POST_OBJECT = "complete_object";
    private static final String COLUMN_INTERESTING_POST_RESULT = "result";
    private long timestampLastTimeRefreshed;

    protected InterestingPostList(String category, String sub_category) {
        super();
        this.category=category;
        this.sub_category=sub_category;
        timestampLastTimeRefreshed=0;
        timestampLastTimeSaved=0;
    }

    /**
     * This method will first try to recover a previous instance from the database, if it doesn't exist, it will create one
     * new
     * @return instance of InterestingPostList
     */
    public static InterestingPostList getInstance(String category, String sub_category) {
        if (instance == null) {
            instance = new ConcurrentHashMap<String, ConcurrentHashMap<String, InterestingPostList>>();
            InterestingPostList temp = getInstanceFromDatabase(category, sub_category);
            if (temp == null) {
                ConcurrentHashMap<String, InterestingPostList> toInsert = new ConcurrentHashMap<String, InterestingPostList>();
                toInsert.put(sub_category, new InterestingPostList(category, sub_category));
                instance.put(category, toInsert);
            } else {
                ConcurrentHashMap<String, InterestingPostList> toInsert = new ConcurrentHashMap<String, InterestingPostList>();
                toInsert.put(sub_category, temp);
                instance.put(category, toInsert);
            }
        } else {
            if (instance.containsKey(category)) {
                if (!instance.get(category).containsKey(sub_category)) {
                    InterestingPostList temp = getInstanceFromDatabase(category, sub_category);
                    if(temp==null){
                        instance.get(category).put(sub_category,new InterestingPostList(category, sub_category));
                    }else{
                        instance.get(category).put(sub_category, temp);
                    }
                }
            } else {
                InterestingPostList temp=getInstanceFromDatabase(category, sub_category);
                if (temp == null) {
                    ConcurrentHashMap<String, InterestingPostList> toInsert = new ConcurrentHashMap<String, InterestingPostList>();
                    toInsert.put(sub_category, new InterestingPostList(category, sub_category));
                    instance.put(category, toInsert);
                } else {
                    ConcurrentHashMap<String, InterestingPostList> toInsert = new ConcurrentHashMap<String, InterestingPostList>();
                    toInsert.put(sub_category, temp);
                    instance.put(category, toInsert);
                }
            }
        }
        return instance.get(category).get(sub_category);
    }

    /**
     *
     * @return null if no instance is saved in the database, a copy of the instance if it is present in the database
     */
    private static InterestingPostList getInstanceFromDatabase(String category, String sub_category){
        return getObjectFromCassandra(category, sub_category);
    }


    /**
     * Add vote to the list.
     *
     * @param postId
     */

    public synchronized void addVote(String postId) {
        if(this.containsKey(postId)){
            super.get(postId).addVote(super.size());
        }else{
            InterestingPostEntry toInsert = new InterestingPostEntry(postId);
            toInsert.addVote(super.size());
            super.put(postId, toInsert);
        }
        refreshChecker();
       saveToDatabaseChecker();
       
    }

    /**
     * Check if the object needs to be saved
     */

    private void saveToDatabaseChecker() {
        if ((Calendar.getInstance().getTimeInMillis() - this.timestampLastTimeSaved) > this.MIN_INTERVAL_REFRESH) {
            saveObjectStateToCassandra();
        }
    }

    /**
     * Check if the map has been refreshed
     */

    private synchronized void refreshChecker(){
        if ((Calendar.getInstance().getTimeInMillis() - this.timestampLastTimeRefreshed) > this.MIN_INTERVAL_REFRESH) {
            refreshHashMap();
        }
    }



    /**
     * Insert the whole object in the Cassandra database
     * The object is serialized and inserted as a Byte array in the database
     */
    public void saveObjectStateToCassandra() {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            ByteArrayOutputStream bstreamout = new ByteArrayOutputStream();
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bstreamout);
            out.writeObject(this);
            connector.insertInfoColumnWithkey(KEY_INTERESTING_POST_LIST+category+sub_category,
                    StringUtils.bytes(COLUMN_INTERESTING_POST_OBJECT),
                    bstreamout.toByteArray(), DatabaseUtil.DB_SERIALIZABLE);
            timestampLastTimeSaved=Calendar.getInstance().getTimeInMillis();
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }

    }

    /**
     *
     * @return the result as an arrayList
     */

    public static ArrayList<String> getResultFromCassandra(String category, String sub_category) {
        FallingDownConnector connector = null;
        ArrayList<String> list = new ArrayList();
        try {
            connector = new FallingDownConnector();
            byte[] result = connector.getInfoColumnWithkey(KEY_INTERESTING_POST_LIST + category + sub_category,
                    COLUMN_INTERESTING_POST_RESULT, DatabaseUtil.DB_SERIALIZABLE);
            ByteArrayInputStream bStreamIn = new ByteArrayInputStream(result);
            ObjectInputStream in = new ObjectInputStream(bStreamIn);
            list = (ArrayList<String>) in.readObject();
        } catch (NotFoundException ex) {
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
        return list;
    }

    /**
     *
     * @return the whole object from Cassandra database
     */

    public static InterestingPostList getObjectFromCassandra(String category, String sub_category) {
        FallingDownConnector connector = null;
        InterestingPostList result = null;
        try {
            connector = new FallingDownConnector();
            byte[] dbResult = connector.getInfoColumnWithkey(KEY_INTERESTING_POST_LIST + category + sub_category, COLUMN_INTERESTING_POST_OBJECT, DatabaseUtil.DB_SERIALIZABLE);
            ByteArrayInputStream bStreamIn = new ByteArrayInputStream(dbResult);
            ObjectInputStream in = new ObjectInputStream(bStreamIn);
            result = (InterestingPostList) in.readObject();
        } catch (NotFoundException ex) {
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InterestingPostList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
        return result;
    }

    /**
     * Update the score of all the InterestingPost entries
     * If one has a negative score then it is deleted.
     *
     * The Arraylist has been added to prevent throwing
     * of COncurrentThreadModification during iteration
     */

    private void refreshHashMap() {
        ArrayList<String> keysToDelete =new ArrayList<String>();
        Iterator<String> iterator= super.keySet().iterator();
        while(iterator.hasNext()){
            String key=iterator.next();
            super.get(key).refreshScore(super.size());
            if(super.get(key).getScore()<0){
                keysToDelete.add(key);
                
            }
        }

        for(int index=0;index < keysToDelete.size();index++)
            super.remove(keysToDelete.get(index));

        timestampLastTimeRefreshed=Calendar.getInstance().getTimeInMillis();
    }
}
