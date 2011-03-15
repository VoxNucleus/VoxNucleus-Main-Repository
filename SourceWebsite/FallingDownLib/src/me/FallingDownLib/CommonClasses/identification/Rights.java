package me.FallingDownLib.CommonClasses.identification;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.connectors.PostConnector;
import me.FallingDownLib.CommonClasses.PostFields;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class Rights {

    /**
     * 
     * @param pass
     * @param postId
     * @return true if Pass associated enables one to delete the post
     */


    public static boolean hashRightToDeletePost(Pass pass, String postId){
        if(pass.isAdministrator() || pass.isManager())
            return true;
        else{
            boolean authorized=false;
            PostConnector pConnector = PostConnector.getInstance(postId);
            try {
                String postAuthor=StringUtils.string(pConnector.getField(StringUtils.bytes(PostFields.DB_AUTHOR)));
                if(pass.getUsername().equals(postAuthor))
                    authorized=true;
            } catch (Exception ex) {
                Logger.getLogger(Rights.class.getName()).log(Level.SEVERE, null, ex);
            }
            return authorized;
        }
    }

    /**
     *
     * @param pass
     * @param postId
     * @return true if someone can modify a post, false otherwise.
     */
    public static boolean hashRightToModifyPost(Pass pass, String postId){
        if(pass.isAdministrator() || pass.isManager())
            return true;
        else{
            boolean authorized=false;
            PostConnector pConnector = PostConnector.getInstance(postId);
            try {
                String postAuthor=StringUtils.string(pConnector.getField(StringUtils.bytes(PostFields.DB_AUTHOR)));
                if(pass.getUsername().equals(postAuthor))
                    authorized=true;
            } catch (Exception ex) {
                Logger.getLogger(Rights.class.getName()).log(Level.SEVERE, null, ex);
            }
            return authorized;
        }
    }

    /**
     * TODO grand rights
     * @param pass
     * @param uuid
     * @param posttId
     * @return true if Pass associated enables one to delete the comment
     */
    public static boolean hasRightToDeleteComment(Pass pass,String uuid,String posttId){
        return false;
    }

}
