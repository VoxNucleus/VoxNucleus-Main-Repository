package me.voxnucleus.cassandra.connector;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.connectors.Connector;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.NotFoundException;

/**
 *
 * @author victork
 */
public class ListVotesByCommentConnector {

    private String comment_uuid;
    private String user_id;

    /**
     * 
     * @param uuid
     */
    protected ListVotesByCommentConnector(String u_id,String uuid) {
        user_id=u_id;
        comment_uuid = uuid;
    }

    /**
     * 
     * @param uuid
     * @return
     */
    public static ListVotesByCommentConnector getInstance(String u_id,String uuid) {
        return new ListVotesByCommentConnector(u_id,uuid);
    }

    /**
     * 
     * @param u_id
     */
    public void setUser(String u_id) {
        user_id = u_id;
    }

    /**
     * 
     * @return
     */
    public boolean has_voted() {
        Connector connector = new Connector();
        boolean hasVoted = true;
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTE_BY_COMMENT);
            cp.setColumn(StringUtils.bytes(user_id));
            if (ks.getColumn(comment_uuid, cp) != null) {
                hasVoted = true;
            } else {
                hasVoted = false;
            }
        } catch (NotFoundException ex) {
            hasVoted = false;
        } catch (Exception ex) {
            Logger.getLogger(ListVotesByCommentConnector.class.getName()).log(Level.SEVERE, null, ex);
            hasVoted = true;
        } finally {
            Connector.releaseClient(connector);
        }
        return hasVoted;
    }

    /**
     *
     * @param vote
     */
    public void insert_vote(String vote) {
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_VOTE_BY_COMMENT);
            cp.setColumn(StringUtils.bytes(user_id));
            int nb_vote;
            try {
                nb_vote = Integer.parseInt(StringUtils.string(ks.getColumn(user_id, cp).getValue()));
            } catch (NotFoundException ex) {
                nb_vote = 0;
            }
            nb_vote++;
            ks.insert(comment_uuid, cp, StringUtils.bytes(Integer.toString(nb_vote)));
        } catch (Exception ex) {
            Logger.getLogger(ListVotesByCommentConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    public void remove_vote() {
        //TODO
    }
}
