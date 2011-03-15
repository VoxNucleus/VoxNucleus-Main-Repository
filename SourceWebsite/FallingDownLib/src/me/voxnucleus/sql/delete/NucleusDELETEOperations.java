/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.voxnucleus.sql.delete;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.FallingDownLib.CommonClasses.PostFields;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 * All operations for nucleus delete operations are put here
 * @author victork
 */
public class NucleusDELETEOperations {


    /**
     * Delete a Nucleus using its key
     * @param key
     * @throws SQLException
     */
    public static void deletePost(String key) throws SQLException,Exception{
        StringBuilder req_builder = new StringBuilder();
        req_builder.append("DELETE FROM ").append(Tables.TABLE_NUCLEUS);
        req_builder.append(" WHERE ").append(PostFields.DB_KEY).append(" = ");
        req_builder.append("'").append(key).append("' ;");
        Connection conn = null;
        try{
            conn = VoxNucleusSQLConnection.openConnection();
            Statement stmt =conn.createStatement();
            stmt.executeUpdate(req_builder.toString());
        }finally{
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }

}
