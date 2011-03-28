/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.cassandracrawler;

import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class DatabaseRetriever {
    public static final String DB_CREATED_NOT_INDEXED="CreatedNotIndexed";
    public static final String DB_MODIFIED_NOT_INDEXED="ModifiedNotIndexed";
    public static final String DB_DELETED_NOT_INDEXED="DeletedNotIndexed";

    ArrayList<Column> postList;
    ArrayList<Column> userList;
    ArrayList<Column> commentList;
    
    public DatabaseRetriever(){
        
    }

    /**
     * launch process of retrieving for CreatedNotIndexed
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */

    public void retrieve(String database) throws PoolExhaustedException, TException, Exception {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            postList=new ArrayList(connector.getSlice(database,"Posts"));
            commentList=new ArrayList(connector.getSlice(database, "Comments"));
            userList=new ArrayList(connector.getSlice(database, "Users"));
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }
    



    /**
     * Retrieve list of id of Posts to archive
     * @return list of id of not indexed posts
     */

    public ArrayList<Column> getPostList(){
        return postList;
    }

    public ArrayList<Column> getUserList(){
        return userList;
    }

    public ArrayList<Column> getCommentList(){
        return commentList;
    }

    /**
     * Convert list of column into list of string
     * @param slice
     * @return
     */
    private ArrayList<String> column2Array(List<Column> slice) {
        ArrayList<String> temp = new ArrayList<String>();
        for(int i=slice.size()-1;i>=0;i++){
            temp.add(StringUtils.string(slice.get(i).getValue()));
        }
        return temp;
    }
}
