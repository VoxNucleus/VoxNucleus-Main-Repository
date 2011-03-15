package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.prettyprint.cassandra.service.BatchMutation;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class BestByDateConnector {

    private int numberToRetrieve;
    private int offset;
    private String category;
    private String sub_category;
    private String time_filter;
    private byte[] postId;
    private byte[] rank;


    private BestByDateConnector(int nbToRetrieve,int in_offset,
            String cat, String sub_cat
            ,String filter){
        numberToRetrieve =nbToRetrieve;
        offset=in_offset;
        category=cat;
        sub_category=sub_cat;
        time_filter=filter;
    }


    protected BestByDateConnector(byte[] superCol_rank, byte[] pId, String cat,
            String sub_cat,String filter){
        rank=superCol_rank;
        postId=pId;
        category=cat;
        sub_category=sub_cat;
        time_filter=filter;
        
    }

    /**
     * 
     * @param numberToRetrieve
     * @param offset
     * @param cat
     * @param sub_cat
     * @param filter
     * @return
     */
    public static BestByDateConnector getInstance(int numberToRetrieve, int offset,
            String cat, String sub_cat, String filter) {
        return new BestByDateConnector(numberToRetrieve, offset,
                cat, sub_cat, filter);
    }

    /**
     * Made to retrieve or delete one particular post
     * @param superCol
     * @param postIdName
     * @param cat
     * @param sub_cat
     * @param filter
     * @return
     */

    public static BestByDateConnector getInstance(byte[] superCol, byte[] postIdName,
            String cat, String sub_cat, String filter) {
        return new BestByDateConnector(superCol, postIdName,
                cat, sub_cat, filter);
    }

    /**
     * 
     * @return
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    private ArrayList<SuperColumn> askDatabase() throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        ArrayList<SuperColumn> listSuperCol=new ArrayList<SuperColumn>();
        Connector connector = new Connector();
        SlicePredicate sp = new SlicePredicate();
        SliceRange sliceRange = new SliceRange();
        sliceRange.setStart(new byte[]{});
        sliceRange.setFinish(new byte[]{});
        sliceRange.setCount(numberToRetrieve);
        sliceRange.setReversed(true);
        sp.setSlice_range(sliceRange);
        ColumnParent cp = new ColumnParent();
        cp.setColumn_family(ColumnFamilies.DB_BEST_POSTS_BY_DATE);
        try {
            Keyspace ks = connector.getKeyspace();
            listSuperCol = new ArrayList<SuperColumn>(ks.getSuperSlice(time_filter + category + sub_category, cp, sp));
        } finally {
            Connector.releaseClient(connector);
        }
        return listSuperCol;
    }

    /**
     * 
     * @return
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */
    public ArrayList<Column> getBestSuperColumns() throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        ArrayList<Column> result = new ArrayList();
        ArrayList<SuperColumn> superColList = askDatabase();
        boolean areCollected = false;
        int numberCollected = 0;

        int colGlobalIndex = 0;
        int index_SuperColumn = 0;
        while (!areCollected && index_SuperColumn < superColList.size()) {
            if (superColList.get(index_SuperColumn).getColumnsSize() + colGlobalIndex > offset) {
                List<Column> listCol = superColList.get(index_SuperColumn).getColumns();
                for (int index_col = Math.max(0,offset-colGlobalIndex);
                        index_col < Math.min(listCol.size(), (colGlobalIndex +superColList.get(index_SuperColumn).getColumnsSize() - offset) + numberToRetrieve);
                        index_col++) {
                    result.add(result.size(),listCol.get(index_col));
                    numberCollected++;
                    if (numberCollected >= numberToRetrieve) {
                        areCollected = true;
                        break;
                    }
                    
                }
                colGlobalIndex += superColList.get(index_SuperColumn).getColumnsSize();
            } else {
                colGlobalIndex += superColList.get(index_SuperColumn).getColumnsSize();
            }
            index_SuperColumn++;
        }
        return result;
    }

    /**
     *
     * @param timeFiler
     * @param category
     * @param sub_category
     * @param superCol
     * @param col
     * @return
     */


    public static boolean isPresentInBestByDate(String timeFiler,
            String category,String sub_category, byte[] superCol, byte[] col) {
        boolean isPresent = false;
        Connector connector = new Connector();

        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_BEST_POSTS_BY_DATE);
            cp.setSuper_column(superCol);
            cp.setColumn(col);
            byte[] column = ks.getColumn(timeFiler + category + sub_category, cp).getName();
            if (column.length > 0) {
                isPresent = true;
            } else {
                isPresent = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
            isPresent = false;
        } finally {
            Connector.releaseClient(connector);
        }
        return isPresent;
    }


    public void removeFromBest(){
        Connector connector = new Connector();
        Keyspace ks;
        try {
            ks = connector.getKeyspace();
            BatchMutation bm = new BatchMutation();
            Deletion del = new Deletion();
            del.setSuper_column(rank);
            SlicePredicate sp = new SlicePredicate();
            ArrayList<byte[]> col = new ArrayList<byte[]>();
            col.add(postId);
            sp.setColumn_names(col);
            del.setPredicate(sp);
            del.setTimestamp(ks.createTimestamp());
            ArrayList<String> cf = new ArrayList<String>();
            cf.add(ColumnFamilies.DB_BEST_POSTS_BY_DATE);
            bm.addDeletion(time_filter + "Tout" + "Tout", cf, del);
            bm.addDeletion(time_filter + category + "Tout", cf, del);
            bm.addDeletion(time_filter + category + sub_category, cf, del);
            ks.batchMutate(bm);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BestByDateConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connector.releaseClient(connector);
        }
            

    }
}
