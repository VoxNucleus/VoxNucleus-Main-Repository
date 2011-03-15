package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class WidgetConnector {

    private String url;
    private byte[] uuid;


    protected WidgetConnector(String url_ask){
        url= url_ask;
    }

    protected WidgetConnector(String url_ask,byte[] post_uuid){
        this(url_ask);
        uuid=post_uuid;   
    }


    public static WidgetConnector getInstance(String url_ask){
        return new WidgetConnector(url_ask);
    }

    public static WidgetConnector getInstance(String url_ask,byte[] post_uuid){
        return new WidgetConnector(url_ask,post_uuid);
    }

    /**
     *
     * @param uuid
     * @param postId
     * @throws IllegalArgumentException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws NotFoundException
     * @throws TimedOutException
     * @throws Exception
     */

    public void saveIntoDatabase(byte[] uuid, String postId) throws IllegalArgumentException,
            InvalidRequestException, UnavailableException, TException,
            IllegalStateException, PoolExhaustedException, NotFoundException,
            TimedOutException, Exception {
        Connector connector = new Connector();
        try {
            ColumnPath cp = new ColumnPath();
            cp.setColumn(uuid);
            cp.setColumn_family(ColumnFamilies.DB_POST_BY_URL);
            Keyspace ks = connector.getKeyspace();
            ks.insert(url, cp, StringUtils.bytes(postId));
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * 
     * @return The first two id of the posts
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public ArrayList<String> getFirstTwoFromDatabase() throws InvalidRequestException,
            UnavailableException, TException, TimedOutException,
            IllegalArgumentException, IllegalStateException,
            PoolExhaustedException, Exception {
        Connector connector = new Connector();

        ArrayList<String> id_list;

        try {
            Keyspace ks = connector.getKeyspace();
            ColumnParent cp = new ColumnParent();
            SlicePredicate sp = new SlicePredicate();
            SliceRange sr = new SliceRange();
            cp.setColumn_family(ColumnFamilies.DB_POST_BY_URL);
            sr.setCount(2);
            sr.setStart(new byte[]{});
            sr.setFinish(new byte[]{});
            sp.setSlice_range(sr);
            id_list=new ArrayList<String>(ColumnUtil.listColTolistString_value(
                    ks.getSlice(url, cp, sp)));
        } catch (NotFoundException ex) {
            id_list= new ArrayList<String>();
        } finally {
            Connector.releaseClient(connector);
        }
        return id_list;
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
            cp.setColumn(uuid);
            cp.setColumn_family(ColumnFamilies.DB_POST_BY_URL);
            ks.remove(url, cp);
        } finally {
            Connector.releaseClient(connector);
        }
    }

}
