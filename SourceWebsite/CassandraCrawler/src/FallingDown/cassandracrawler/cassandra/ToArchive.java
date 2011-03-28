package FallingDown.cassandracrawler.cassandra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class ToArchive {

    ArrayList<Column> listPostToArchive;
    ArrayList<Column> listCommentToArchive;
    ArrayList<Column> listUserToArchive;

    private String columnFamily;

    private final int MAX_NUMBER_IN_QUEUE = 100;
    private final String DB_INDEXED_ARCHIVE="IndexedArchive";

    public final static int IS_A_POST=0;
    public final static int IS_A_COMMENT=1;
    public final static int IS_A_USER=2;

    public final static String PREFIX_COMMENT="comment-";
    public final static String PREFIX_POST="post-";
    public final static String PREFIX_USER="user-";


    /**
     * Constructor, does nothing
     */
    public ToArchive() {
        listPostToArchive = new ArrayList<Column>();
        listCommentToArchive=new ArrayList<Column>();
        listUserToArchive = new ArrayList<Column>();
    }

    /**
     * 
     * @param col
     */

    public void addToArchive(Column col, int type) {
        switch(type){
            case IS_A_POST:
                addPostToArchive(col);
                break;
            case IS_A_COMMENT:
                addCommentToArchive(col);
                break;
            case IS_A_USER:
                addUserToArchive(col);
                break;
        }
    }

    /**
     * 
     * @param cf
     */

    public void setColumnFamily(String cf){
        columnFamily=cf;
    }

    /**
     *
     * @param col
     */

    private void addPostToArchive(Column col){
        if (!(totalListsSize() > MAX_NUMBER_IN_QUEUE)) {
            listPostToArchive.add(col);
        } else {
            listPostToArchive.add(col);
            insertInArchive();
            clearLists();
        }
    }

    /**
     *
     * @param col
     */

    private void addCommentToArchive(Column col){
        if (!(listCommentToArchive.size() > MAX_NUMBER_IN_QUEUE)) {
            listCommentToArchive.add(col);
        } else {
            listCommentToArchive.add(col);
            insertInArchive();
            clearLists();
        }
    }

    /**
     *
     * @param col
     */

    private void addUserToArchive(Column col){
        if (!(listUserToArchive.size() > MAX_NUMBER_IN_QUEUE)) {
            listUserToArchive.add(col);
        } else {
            listUserToArchive.add(col);
            insertInArchive();
            clearLists();
        }

    }

    /**
     *
     * @return length of the sum of the lists
     */

    private int totalListsSize(){
        return (listPostToArchive.size()+listCommentToArchive.size()+listUserToArchive.size());
    }

    /**
     * Clear all lists
     */


    private void clearLists(){
        listCommentToArchive.clear();
        listPostToArchive.clear();
        listUserToArchive.clear();
    }


    /**
     * In case process is interrupted or if there 
     * are still some data that needs to be archived
     */

    public void flushToArchive() {
        if(totalListsSize()>0){
            insertInArchive();
            clearLists();
        }
    }

    /**
     * Insert into archive database
     */
    private void insertInArchive() {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            HashMap<String, List<Column>> toInsert = new HashMap<String, List<Column>>();
            toInsert.put(DB_INDEXED_ARCHIVE, listPostToArchive);
            connector.batchInsertColumn(getKey(IS_A_POST), toInsert);
            toInsert.clear();
            toInsert.put(DB_INDEXED_ARCHIVE, listCommentToArchive);
            connector.batchInsertColumn(getKey(IS_A_COMMENT), toInsert);
            toInsert.clear();
            toInsert.put(DB_INDEXED_ARCHIVE, listUserToArchive);
            connector.batchInsertColumn(getKey(IS_A_USER), toInsert);
            destroyKeys(connector);
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    /**
     *
     * @param entry
     * @return
     */

    private ArrayList<byte[]> transformColumnToByte(ArrayList<Column> entry){
        ArrayList<byte[]> result= new ArrayList<byte[]>();
        for(int index=0;index<entry.size();index++){
            result.add(entry.get(index).getName());
        }
        return result;
    }


    /**
     * Destroys keys that have been transfered
     * @param connector
     */

    private void destroyKeys(FallingDownConnector connector){
        ArrayList<byte[]> listPostToDelete=transformColumnToByte(listPostToArchive);
        ArrayList<byte[]> listCommentToDelete=transformColumnToByte(listCommentToArchive);
        ArrayList<byte[]> listUserToDelete=transformColumnToByte(listUserToArchive);
        try {
            connector.batchDeleteColumns(columnFamily, "Posts", listPostToDelete);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connector.batchDeleteColumns(columnFamily, "Comments", listCommentToDelete);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connector.batchDeleteColumns(columnFamily, "Users", listUserToDelete);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(ToArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * @return key with the following property :
     * YearMonthDay (like : 20100706 for the six of July 2010)
     * TODO : modify so it can be used for posts, user and comments
     */

    private String getKey(int type){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String prefix;
        switch(type){
            case IS_A_POST:
                prefix=PREFIX_POST;
                break;
            case IS_A_COMMENT:
                prefix=PREFIX_COMMENT;
                break;
            case IS_A_USER:
                prefix=PREFIX_USER;
                break;
            default:
                return "ERROR";

        }
        return prefix+sdf.format(cal.getTime());
    }
}
