package me.FallingDownLib.CassandraConnection.connectors;

import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class CommentConnector {

    private byte[] comment_UUID;
    private String postId;


    /**
     * Basic constructor
     * @param pId
     * @param uuid
     */
    private CommentConnector(String pId,byte[] uuid){
        comment_UUID=uuid;
        postId=pId;
    }

    /**
     * Get an instance of the connector
     * @param pId
     * @param uuid
     * @return
     */
    public static CommentConnector getInstance(String pId,byte[] uuid){
        return new CommentConnector(pId,uuid);
    }

    /**
     * 
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TException
     * @throws TimedOutException
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public void deleteComment() throws InvalidRequestException, UnavailableException,
            TException, TimedOutException, IllegalArgumentException,
            NotFoundException, IllegalStateException, PoolExhaustedException,
            Exception{
        Connector connector = new Connector();
        try{
            Keyspace keyspace=connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            keyspace.remove(postId, cp);
        }finally{
            Connector.releaseClient(connector);
        }
    }

    /**
     * 
     * @param field
     * @return
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws IllegalArgumentException
     * @throws TException
     * @throws IllegalStateException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public byte[] getField(byte[] field) throws InvalidRequestException,
            NotFoundException, IllegalArgumentException, TException,
            IllegalStateException, UnavailableException, TimedOutException,
            PoolExhaustedException, Exception{
        Connector connector = new Connector();
        byte[] result={};
        try{
            Keyspace keyspace=connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            cp.setColumn(field);
            result=keyspace.getColumn(postId, cp).getValue();
        }finally{
            Connector.releaseClient(connector);
        }
        return result;
    }

    public void setField(byte[] colName, byte[] colValue) throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            cp.setColumn(colName);
            keyspace.insert(postId, cp, colValue);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * 
     * @param to_add
     * @throws Exception
     */

    public void update_score(int to_add) throws Exception{
        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            cp.setColumn(StringUtils.bytes(CommentFields.DB_SCORE));
            int reputation=Integer.parseInt(
                    StringUtils.string(keyspace.getColumn(postId, cp).getValue()));
            reputation+=to_add;
            keyspace.insert(postId, cp, StringUtils.bytes(Integer.toString(to_add)));
        } catch(NotFoundException ex){
            //set score to 0
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            cp.setColumn(StringUtils.bytes(CommentFields.DB_SCORE));
            keyspace.insert(postId, cp, StringUtils.bytes(Integer.toString(0)));
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     *
     * @return true if the comment exists, false otherwise.
     */
    public boolean exists() throws IllegalArgumentException, TException, 
            IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        boolean does_exist=false;
        byte[] result = {};
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_COMMENTS_BY_POSTS);
            cp.setSuper_column(comment_UUID);
            cp.setColumn(StringUtils.bytes(CommentFields.DB_TIMESTAMP));
            result = keyspace.getColumn(postId, cp).getValue();
            does_exist=true;
        } catch (NotFoundException ex) {
            does_exist=false;
        } finally {
            Connector.releaseClient(connector);
        }
        return does_exist;
    }

}
