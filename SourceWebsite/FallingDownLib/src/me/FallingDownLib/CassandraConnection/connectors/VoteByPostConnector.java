package me.FallingDownLib.CassandraConnection.connectors;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.NotFoundException;

/**
 * Votes by post.
 * Each column has for name : the username, value the uuid of the vote
 * @author victork
 */
public class VoteByPostConnector {
    private final String postId;


    protected VoteByPostConnector(String pId){
        postId=pId;
    }

    public static VoteByPostConnector getConnector(String pId){
        return new VoteByPostConnector(pId);
    }

    public void insertVote(String uId,byte[] uuid) throws Exception{
        Connector connector = new Connector();
        try{
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTES_BY_POST);
            cp.setColumn(StringUtils.bytes(uId));
            ks.insert(postId, cp, uuid);
        }finally{
            Connector.releaseClient(connector);
        }
    }
    
    /**
     * t
     * @param userId
     * @return
     */

    public boolean hasUserVotedForPost(String userId) {
        Connector connector = new Connector();
        boolean hasVoted = false;
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTES_BY_POST);
            cp.setColumn(StringUtils.bytes(userId));
            if (ks.getColumn(postId, cp) != null) {
                hasVoted = true;
            } else {
                hasVoted = false;
            }
        } catch (NotFoundException ex) {
            Logger.getLogger(VoteByPostConnector.class.getName()).log(Level.SEVERE, null, ex);
            hasVoted = false;
        } catch (Exception ex) {
            Logger.getLogger(VoteByPostConnector.class.getName()).log(Level.SEVERE, null, ex);
            hasVoted = true;
        } finally {
            Connector.releaseClient(connector);
        }
        return hasVoted;
    }

}
