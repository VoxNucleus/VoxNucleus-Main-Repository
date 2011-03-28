/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package databasemanager.cassandra2sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.FallingDownLib.CassandraConnection.util.IterateOverKey;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserHash;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.voxnucleus.sql.insert.UserINSERTOperations;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 * Class dedicated to convert User on Cassandra to their SQL relative
 * @author victork
 */
public class UserCass2SQL {



    public static void transfer(){
        IterateOverKey iterate = new IterateOverKey(ColumnFamilies.DB_USERS, 1000);

        HashMap<String, List<Column>> result;
        while (!(result= iterate.getNextSet(null)).isEmpty()) {
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String pkey = iterator.next();
                try {

                    UserHash uHash = new UserHash(User.getUserFromDatabase(pkey));
                    List<Column> col_list = result.get(pkey);
                    UserINSERTOperations.insertUser(uHash.getUsername(),uHash.getEmail(),UUID.fromString(uHash.getUUID()),0);
                } catch (PoolExhaustedException ex) {
                    Logger.getLogger(CassandraToSQL.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TException ex) {
                    Logger.getLogger(CassandraToSQL.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    System.out.println(pkey);
                    Logger.getLogger(CassandraToSQL.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }
}
