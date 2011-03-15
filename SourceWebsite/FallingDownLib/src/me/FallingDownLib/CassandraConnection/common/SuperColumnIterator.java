package me.FallingDownLib.CassandraConnection.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.connectors.Connector;
import me.prettyprint.cassandra.model.NotFoundException;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class SuperColumnIterator {

    private String columnFamily;
    private String key;
    private int nb_SupercolToRetrieve;
    private long index;
    private byte[] startSupercol;
    private byte[] endSupercol;

     private SuperColumnIterator(String CF, String rel_key,int maxCol){
        columnFamily=CF;
        key=rel_key;
        nb_SupercolToRetrieve=maxCol;
        index=0l;
        startSupercol=new byte[]{};
        endSupercol=new byte[]{};
    }

    /**
     *
     * @param CF
     * @param rel_key
     * @return
     */

    public static SuperColumnIterator getSupercolIterator(String CF, String rel_key) {
        return new SuperColumnIterator(CF, rel_key, 500);
    }

    public static SuperColumnIterator getSupercolIterator(String CF, String rel_key, int maxCol) {
        return new SuperColumnIterator(CF, rel_key, maxCol);
    }


    public List<SuperColumn> next(){
        Connector connector=new Connector();
        ArrayList<SuperColumn> listCol=new ArrayList<SuperColumn>();
        try {
            Keyspace ks = connector.getKeyspace();
            SlicePredicate sp = new SlicePredicate();
            SliceRange sliceR = new SliceRange();
            sliceR.setCount(nb_SupercolToRetrieve);
            sliceR.setStart(startSupercol);
            sliceR.setFinish(endSupercol);
            sliceR.setReversed(true);
            sp.setSlice_range(sliceR);
            ColumnParent cp = new ColumnParent();
            cp.setColumn_family(columnFamily);
            listCol = (ArrayList<SuperColumn>) ks.getSuperSlice(key, cp, sp);
            if(startSupercol.length>1)
                listCol.remove(0);
            if(listCol.size()>0)
                startSupercol = listCol.get(listCol.size() - 1).getName();
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

    public List<SuperColumn> get(long start, long end) {
        ArrayList<SuperColumn> result = new ArrayList<SuperColumn>();
        while (index + nb_SupercolToRetrieve <= start ) {
            if(next().isEmpty())
                break;
        }
        while (index < end) {
            ArrayList<SuperColumn> temp = (ArrayList<SuperColumn>) next();
            if (temp.isEmpty()) {
                break;
            } else {
                int begin_index = 0;
                int end_index = temp.size();
                int reduced_index= (int) (index - temp.size());
                begin_index = Math.max((int) (start - reduced_index),0);
                end_index = Math.min( (int) ( end -reduced_index ),temp.size());
                result.addAll(temp.subList(begin_index, end_index));
            }
        }
        return result;
    }



}
