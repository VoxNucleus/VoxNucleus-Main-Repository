package me.voxnucleus.cassandra.connector;

import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.connectors.Connector;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.NotFoundException;

/**
 * Vote By user connector
 * @author victork
 */
public class VoteByUserConnector {

    private String user_id;

    /**
     *Constructor
     * @param u_id
     */

    protected VoteByUserConnector(String u_id){
        user_id=u_id;
    }

    /**
     *
     * @param u_id
     * @return an instance of the connector
     */

    public static VoteByUserConnector getConnector(String u_id){
        return new VoteByUserConnector(u_id);
    }

    /**
     * Add one vote for the user
     * @param post_id
     * @param uuid
     * @throws Exception
     */
    public void add_vote(String post_id,byte[] uuid) throws Exception{
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTES_BY_USER);
            cp.setColumn(StringUtils.bytes(post_id));
            ks.insert(user_id, cp, uuid);
        } finally {
            Connector.releaseClient(connector);
        }
    }


    /**
     * 
     * @param post_id
     * @return true if user has voted, false otherwise
     * @throws Exception
     */

    public boolean has_user_voted(String post_id) throws Exception{
        boolean has_voted=true;
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTES_BY_USER);
            cp.setColumn(StringUtils.bytes(post_id));
            ks.getColumn(user_id, cp);
        }catch(NotFoundException ex){
            has_voted=false;
        } finally {
            Connector.releaseClient(connector);
        }

        return has_voted;
    }

}
