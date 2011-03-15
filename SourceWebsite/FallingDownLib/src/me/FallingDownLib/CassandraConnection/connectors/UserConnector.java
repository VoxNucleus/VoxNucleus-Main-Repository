package me.FallingDownLib.CassandraConnection.connectors;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.UserFields;
import me.prettyprint.cassandra.service.Keyspace;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class UserConnector {

    private String userId;

    private UserConnector(String uId){
        userId=uId;
    }

    /**
     * 
     * @param uId
     * @return
     */
    public static UserConnector getInstance(String uId){
        return (new UserConnector(uId));
    }

    /**
     * CAREFUL THIS METHOD DOES NOT THROW ANYTHING
     * @return the role of the user
     */

    public String getRole(){
        String role="none";
        Connector connector = new Connector();
        int colCount=0;
        try{
            try {
                Keyspace keyspace = connector.getKeyspace();
                ColumnPath cp = new ColumnPath();
                cp.setColumn_family(ColumnFamilies.DB_USERS);
                cp.setColumn(StringUtils.bytes(UserFields.DB_ROLE));
                role=StringUtils.string(keyspace.getColumn(userId, cp).value);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotFoundException ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            Connector.releaseClient(connector);
        }
        return role;
    }

    /**
     * 
     * @param colName
     * @param colValue
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */
    public void setField(byte[] colName, byte[] colValue) throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(colName);
            keyspace.insert(userId, cp, colValue);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * Remove a field
     * @param colName
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */
    public void removeField(byte[] colName) throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(colName);
            keyspace.remove(userId, cp);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     *
     * @param colName
     * @return the field of an user
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public byte[] getField(byte[] colName) throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        byte[] res=null;
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(colName);
            res =keyspace.getColumn(userId, cp).getValue();
        } finally {
            Connector.releaseClient(connector);
        }
        return res;
    }


    /**
     *
     * @return true if the user is suspended, false if it is suspended
     */
    public boolean getSuspended(){
        boolean result=true;
        try {
            result=Boolean.parseBoolean(StringUtils.string(getField(StringUtils.bytes(UserFields.DB_SUSPENDED))));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }



    /**
     * Insert a batch of columns into a key
     * @param listFields
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */
    public void batchSetField(List<Column> listFields)
            throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        Connector connector = new Connector();
        HashMap<String, List<Column>> toInsert = new HashMap<String, List<Column>>();
        toInsert.put(ColumnFamilies.DB_USERS, listFields);
        try {
            Keyspace keyspace = connector.getKeyspace();
            keyspace.batchInsert(userId, toInsert, null);
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * 
     * @return
     */

    public boolean isUserPresent() {
        Connector connector = new Connector();
        boolean isUserPresent=false;
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnParent cp = new ColumnParent();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            if(keyspace.getCount(userId, cp)>0){
                isUserPresent=true;
            }else{
                isUserPresent=false;
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connector.releaseClient(connector);
        }

        return isUserPresent;
    }

    /**
     * Update reputation by a certain amount
     * @param add_reputation
     * @throws Exception
     */
    public void update_reputation(int add_reputation) throws Exception{

        Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(StringUtils.bytes(UserFields.DB_REPUTATION));
            int reputation=Integer.parseInt(StringUtils.string(keyspace.getColumn(userId, cp).getValue()));
            reputation += add_reputation;
            keyspace.insert(userId, cp, StringUtils.bytes(Integer.toString(reputation)));
        }catch(NotFoundException ex){
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(StringUtils.bytes(UserFields.DB_REPUTATION));
            keyspace.insert(userId, cp, StringUtils.bytes(Integer.toString(0)));
        } finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * Decrement reputation by 1
     * (== add 1 to reputation)
     * @throws Exception
     */

    public void increment_reputation() throws Exception{
        update_reputation(1);
    }

    /**
     * Decrement reputation by 1
     * (== add (-1) to reputation)
     * @throws Exception
     */
    public void decrement_reputation() throws Exception{
        update_reputation(-1);
    }

    /**
     * 
     * @param add_vote
     * @throws Exception
     */

    public void update_vote_count(int add_vote) throws Exception{
         Connector connector = new Connector();
        try {
            Keyspace keyspace = connector.getKeyspace();
            ColumnPath cp = new ColumnPath();
            cp.setColumn_family(ColumnFamilies.DB_USERS);
            cp.setColumn(StringUtils.bytes(UserFields.DB_NBVOTES));
            int nb_votes=Integer.parseInt(StringUtils.string(keyspace.getColumn(userId, cp).getValue()));
            nb_votes += add_vote;
            keyspace.insert(userId, cp, StringUtils.bytes(Integer.toString(nb_votes)));
        }finally {
            Connector.releaseClient(connector);
        }
    }

    /**
     * s
     * @throws Exception
     */

    public void increment_vote_count() throws Exception{
        update_vote_count(1);
    }
}
