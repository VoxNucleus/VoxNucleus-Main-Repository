package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.prettyprint.cassandra.service.BatchMutation;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class LatestConnector {

    private int numberToRetrieve;
    private Keyspace keyspace;
    private String category;
    private String sub_category;
    private byte[] post_uuid;
    public static String KEY_LAST10K_POSTS="Posts";

    private LatestConnector(int askNumber,String cat, String sub_cat){
        numberToRetrieve=askNumber;
        category=cat;
        sub_category=sub_cat;
    }

    private LatestConnector(byte[] uuid,String cat, String sub_cat){
        post_uuid=uuid;
        category=cat;
        sub_category=sub_cat;
    }


    public static LatestConnector getInstance(int askedNumber, String cat,String sub_cat){
        return (new LatestConnector(askedNumber,cat,sub_cat));
    }

    public static LatestConnector getInstance(byte[] uuid, String cat,String sub_cat){
        return (new LatestConnector(uuid,cat,sub_cat));
    }


    /**
     *
     * @return the result of the sliceRange
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    private HashMap<String,List<Column>> askDatabase() throws
            IllegalArgumentException, NotFoundException, TException,
            IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        HashMap<String,List<Column>> rangeSliceResult=new HashMap<String,List<Column>>();
        try {
            keyspace = connector.getKeyspace();
            KeyRange kr = new KeyRange();
            kr.setStart_key(KEY_LAST10K_POSTS+category+sub_category);
            kr.setEnd_key(KEY_LAST10K_POSTS+category+sub_category);
            SlicePredicate sp = new SlicePredicate();
            SliceRange sliceRange = new SliceRange();
            sliceRange.setStart(new byte[]{});
            sliceRange.setFinish(new byte[]{});
            sliceRange.setCount(numberToRetrieve);
            sliceRange.setReversed(true);
            sp.setSlice_range(sliceRange);
            ColumnParent cp = new ColumnParent();
            cp.setColumn_family(ColumnFamilies.DB_RECENT_POSTS);
            rangeSliceResult=new HashMap<String,List<Column>>(keyspace.getRangeSlices(cp, sp, kr));
        } finally {
            Connector.releaseClient(connector);
        }
        return rangeSliceResult;

    }

    /**
     *
     * @return
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws IllegalStateException
     * @throws TException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public List<Column> getLatestColumns() throws IllegalArgumentException, NotFoundException,
            IllegalStateException, TException, PoolExhaustedException, Exception{
        HashMap<String,List<Column>> sliceRangeResult =askDatabase();
        if(sliceRangeResult.isEmpty()){
            return new ArrayList<Column>();
        }else{
            return sliceRangeResult.get(KEY_LAST10K_POSTS+category+sub_category);

        }
    }

    public void removePost() throws Exception {
        Connector connector = new Connector();
        Keyspace ks;
        try {
            ks = connector.getKeyspace();
            BatchMutation bm = new BatchMutation();
            Deletion del = new Deletion();
            SlicePredicate sp = new SlicePredicate();
            ArrayList<byte[]> col = new ArrayList<byte[]>();
            col.add(post_uuid);
            sp.setColumn_names(col);
            del.setPredicate(sp);
            del.setTimestamp(ks.createTimestamp());
            ArrayList<String> cf = new ArrayList<String>();
            cf.add(ColumnFamilies.DB_RECENT_POSTS);
            bm.addDeletion(KEY_LAST10K_POSTS + "Tout" + "Tout", cf, del);
            bm.addDeletion(KEY_LAST10K_POSTS + category + "Tout", cf, del);
            bm.addDeletion(KEY_LAST10K_POSTS + category + sub_category, cf, del);
            ks.batchMutate(bm);
        } finally {
            Connector.releaseClient(connector);
        }
    }

}
