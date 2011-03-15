package me.FallingDownLib.CassandraConnection.connectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.PostFields;
import me.prettyprint.cassandra.service.BatchMutation;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Deletion;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class PostConnector {

    private String postId;

    private PostConnector(String pId){
        postId=pId;
    }

    public static PostConnector getInstance(String pId){
        return new PostConnector(pId);
    }

    public byte[] getField(byte[] fieldName) throws IllegalArgumentException,
            NotFoundException, TException, IllegalStateException,
            PoolExhaustedException, Exception{
        Connector connector = new Connector();
        byte[] value = {};
        try{
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_POSTS);
            cp.setColumn(fieldName);
            value = keyspace.getColumn(postId, cp).getValue();
        }finally{
            Connector.releaseClient(connector);
        }
        return value;
    }


    public void batchInsertFields(List<Column> listFields) throws IllegalArgumentException,
            TException, InvalidRequestException, UnavailableException,
            TimedOutException, IllegalStateException, PoolExhaustedException,
            NotFoundException, Exception{
        Connector connector = new Connector();
        HashMap<String, List<Column>> toInsert = new HashMap<String, List<Column>>();
        toInsert.put(ColumnFamilies.DB_POSTS, listFields);
        try {
            Keyspace keyspace = connector.getKeyspace();
            keyspace.batchInsert(postId, toInsert, null);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * Remove the post
     * @throws Exception
     */
    public void removeNucleus() throws Exception {
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            BatchMutation mutation = new BatchMutation();
            Deletion del = new Deletion();
            ArrayList<String> database = new ArrayList<String>();
            del.setTimestamp(ks.createTimestamp());
            database.add(ColumnFamilies.DB_POSTS);
            mutation.addDeletion(postId, database, del);
            ks.batchMutate(mutation);
        } finally {
            Connector.releaseClient(connector);
        }
    }


    /**
     * Add to vote
     * @param to_add
     * @throws Exception
     */
    public void add_to_votes(int to_add) throws Exception{
        Connector connector = new Connector();
        try {
            Keyspace ks = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_POSTS);
            cp.setColumn(StringUtils.bytes(PostFields.DB_NBVOTES));
            int nb_votes=Integer.parseInt(StringUtils.string(
                    ks.getColumn(postId, cp).getValue()));
            nb_votes+=to_add;
            ks.insert(postId, cp, StringUtils.bytes(Integer.toString(nb_votes)));
        } finally {
            Connector.releaseClient(connector);
        }
    }


}
