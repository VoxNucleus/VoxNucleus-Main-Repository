package me.FallingDownLib.CassandraConnection.connectors;

import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.PostReport;
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
public class PostReportConnector {

    public static final String KEY_REPORTED_POST="reported-posts";

    private byte[] colNameUUID;
    private String postId;
    private String userReporter;
    private String givenReason;

    /**
     * 
     * @param uuid
     * @param pId
     * @param user
     * @param reason
     */

    private PostReportConnector(byte[] uuid,String pId, String reason, String user){
        colNameUUID=uuid;
        postId=pId;
        userReporter=user;
        givenReason=reason;
    }



    /**
     *
     * @param uuid
     * @param pId
     * @param user
     * @param reason
     * @return
     */

    public static PostReportConnector getInsertInstance(byte[] uuid, 
            String pId, String reason, String user){
        return new PostReportConnector(uuid,pId,reason,user);
    }


    /**
     * 
     * @param uuid
     * @param user
     * @param report
     * @return
     */
    public static PostReportConnector getInsertInstance(byte[] uuid,
            String user,PostReport report){
        return new PostReportConnector(uuid,user,report.getPostId(),report.getReason());
    }

    /**
     * 
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws Exception
     */

    public void doSave() throws IllegalArgumentException, NotFoundException, 
            TException, IllegalStateException, PoolExhaustedException,
            InvalidRequestException, UnavailableException, TimedOutException, Exception{
        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_REPORTED);
            cp.setColumn(colNameUUID);
            keyspace.insert(KEY_REPORTED_POST, cp,StringUtils.bytes(constructColValue()));
        } finally{
            Connector.releaseClient(connector);
        }
    }

    /**
     *
     * @return
     */

    private String constructColValue(){
        return postId+"/"+userReporter+"/"+givenReason;
    }

}
