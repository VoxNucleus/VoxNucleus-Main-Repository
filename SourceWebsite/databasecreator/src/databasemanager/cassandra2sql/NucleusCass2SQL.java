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
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.util.IterateOverKey;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.voxnucleus.sql.insert.NucleusINSERTOperations;
import me.voxnucleus.sql.insert.VoxOperations;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 * Class dedicated to the transfer from Cassandra database to SQL relationnal database
 *  for querying
 * @author victork
 */
public class NucleusCass2SQL {

    public static void transfer(){
        IterateOverKey iterate = new IterateOverKey(ColumnFamilies.DB_POSTS, 1000);

        HashMap<String, List<Column>> result;
        while (!(result= iterate.getNextSet(null)).isEmpty()) {
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String pkey = iterator.next();
                try {

                    PostHash pHash = new PostHash(Post.getPostFromDatabase(pkey));
                    List<Column> col_list = result.get(pkey);
                    NucleusINSERTOperations.insertNucleus(pHash.getKey(), pHash.getTitle(), pHash.getAuthor(), Long.parseLong(pHash.getTimestamp()), EasyUUIDget.toUUID(pHash.getUUID().getBytes()), pHash.getCategory(),
                            pHash.getSubcategory(), pHash.getCategory(), Integer.parseInt(pHash.getNbVotes()));
                } catch (PoolExhaustedException ex) {
                    Logger.getLogger(CassandraToSQL.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PostDoesNotExist ex) {
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
