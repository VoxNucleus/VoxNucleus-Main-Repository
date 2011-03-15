package me.FallingDownLib.CassandraConnection.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.connectors.Connector;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class ColumnIterator {

    private String columnFamily;
    private String key;
    private int nb_colToRetrieve;

    private long index;

    private byte[] startCol;
    private byte[] endCol;

    /**
     * Constructor
     * @param CF Column Family
     * @param rel_key
     * @param maxCol number of column that is going to be retrieved at each iteration.
     */

    private ColumnIterator(String CF, String rel_key,int maxCol){
        columnFamily=CF;
        key=rel_key;
        nb_colToRetrieve=maxCol;
        index=0l;
        startCol=new byte[]{};
        endCol=new byte[]{};
    }

    /**
     *
     * @param CF
     * @param rel_key
     * @return
     */

    public static ColumnIterator getColIterator(String CF, String rel_key){
        return new ColumnIterator(CF, rel_key,500);
    }

    /**
     *
     * @param CF
     * @param rel_key
     * @param maxCol
     * @return
     */

    public static ColumnIterator getColIterator(String CF, String rel_key, int maxCol){
        return new ColumnIterator(CF, rel_key,maxCol);
    }

    /**
     *
     * @return
     */

    public List<Column> next(){
        Connector connector=new Connector();
        ArrayList<Column> listCol=new ArrayList<Column>();
        try {
            Keyspace ks = connector.getKeyspace();
            SlicePredicate sp = new SlicePredicate();
            SliceRange sliceR = new SliceRange();
            sliceR.setCount(nb_colToRetrieve);
            sliceR.setStart(startCol);
            sliceR.setFinish(endCol);
            sliceR.setReversed(true);
            sp.setSlice_range(sliceR);
            ColumnParent cp = new ColumnParent();
            cp.setColumn_family(columnFamily);
            listCol = (ArrayList<Column>) ks.getSlice(key, cp, sp);
            if(startCol.length>1)
                listCol.remove(0);
            if(listCol.size()>0)
                startCol = listCol.get(listCol.size() - 1).getName();
            index = index + listCol.size();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ColumnIterator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            Connector.releaseClient(connector);
        }
        return listCol;
    }

    /**
     * 
     * @param start
     * @param end
     * @return
     */

    public List<Column> get(long start, long end) {
        ArrayList<Column> result = new ArrayList<Column>();
        while (index + nb_colToRetrieve <= start ) {
            if(next().isEmpty())
                break;
        }
        while (index < end) {
            ArrayList<Column> temp = (ArrayList<Column>) next();
            if (temp== null || temp.isEmpty()) {
                break;
            } else {
                int begin_index = 0;
                int end_index = temp.size();
                int reduced_index= (int) (index - temp.size());
                begin_index = Math.min(Math.max((int) (start - reduced_index),0),temp.size());
                end_index = Math.min( (int) ( end -reduced_index ),temp.size());
                result.addAll(temp.subList(begin_index, end_index));
            }
        }
        return result;
    }

}
