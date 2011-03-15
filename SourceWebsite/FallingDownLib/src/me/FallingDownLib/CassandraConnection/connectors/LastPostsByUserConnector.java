package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.util.ListUtil;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class LastPostsByUserConnector {

    private int numberToRetrieve;
    private String userId;
    private int offset;
    private Keyspace keyspace;
    private byte[] post_uuid;

    public static final String SUPERCOL="listPost";

    /**
     * 
     * @param uId
     * @param offset
     * @param numberToRetrieve
     */
    private LastPostsByUserConnector(String uId, int offset, int numberToRetrieve) {
        userId = uId;
        this.numberToRetrieve = numberToRetrieve;
        this.offset = offset;
    }


    private LastPostsByUserConnector(String uId, byte[] uuid) {
        userId = uId;
        post_uuid=uuid;
    }

    /**
     *
     * @param uId
     * @param offset
     * @param numberToRetrieve
     * @return
     */
    public static LastPostsByUserConnector getInstance(String uId, int offset, int numberToRetrieve) {
        return new LastPostsByUserConnector(uId, offset, numberToRetrieve);
    }

    public static LastPostsByUserConnector getInstance(String uId, byte[] uuid) {
        return new LastPostsByUserConnector(uId, uuid);
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
    private ArrayList<Column> askDatabase() throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        ArrayList<Column> colList = new ArrayList<Column>();
        try {
            keyspace = connector.getKeyspace();
            SlicePredicate sp = new SlicePredicate();
            SliceRange sliceRange = new SliceRange();
            sliceRange.setStart(new byte[]{});
            sliceRange.setFinish(new byte[]{});
            sliceRange.setCount(offset + numberToRetrieve);
            sliceRange.setReversed(true);
            sp.setSlice_range(sliceRange);
            ColumnParent cp = new ColumnParent();
            cp.setColumn_family(ColumnFamilies.DB_POST_BY_USER);
            cp.setSuper_column(StringUtils.bytes(SUPERCOL));
            colList = new ArrayList<Column>(keyspace.getSlice(userId, cp, sp));
        } finally {
            Connector.releaseClient(connector);
        }
        return colList;
    }

    /**
     * 
     * @throws Exception
     */

    public void removePost() throws Exception {
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn(post_uuid);
            cp.setSuper_column(StringUtils.bytes(SUPERCOL));
            cp.setColumn_family(ColumnFamilies.DB_POST_BY_USER);
            ks.remove(userId, cp);
        } finally {
            Connector.releaseClient(connector);
        }
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
    public ArrayList<Column> getColumns() throws IllegalArgumentException,
            NotFoundException, TException, IllegalStateException, PoolExhaustedException,
            Exception {
        return askDatabase();
    }


    public ArrayList<Column> getGoodNumberOfColumns() {
        ArrayList<Column> listCol=null;
        try {
            listCol = askDatabase();
            listCol = new ArrayList<Column>(ListUtil.subList(listCol, offset,
                    offset+numberToRetrieve));
        } catch (Exception ex) {
            listCol=new ArrayList<Column>();
        }
        return listCol;
    }


}
