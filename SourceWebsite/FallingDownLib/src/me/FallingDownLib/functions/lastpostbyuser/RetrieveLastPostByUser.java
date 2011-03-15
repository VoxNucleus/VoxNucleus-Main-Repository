package me.FallingDownLib.functions.lastpostbyuser;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.connectors.LastPostsByUserConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;
import me.prettyprint.cassandra.utils.StringUtils;
/**
 *
 * @author victork
 */
public class RetrieveLastPostByUser {
    private String userId;
    private int offset;
    private int numberToRetrieve=10;


    /**
     * 
     * @param uId
     * @param offset
     */
    public RetrieveLastPostByUser(String uId,int offset,int numberToRetrieve){
        this.offset=offset;
        userId=uId;
        this.numberToRetrieve=numberToRetrieve;
    }


    /**
     * 
     * @return
     */
    public ArrayList<String> returnResult(){
        LastPostsByUserConnector connector=LastPostsByUserConnector.getInstance(userId, offset, numberToRetrieve);
        ArrayList<String> result = new ArrayList<String>();
        try {
            ArrayList<Column> cols = connector.getColumns();
            for(int index_col=0;index_col<cols.size();index_col++){
                result.add(StringUtils.string(cols.get(index_col).getValue()));
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RetrieveLastPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
