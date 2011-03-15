package me.voxnucleus.sql.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.FallingDownLib.CommonClasses.PostFields;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 *
 * @author victork
 */
public class NucleusUPDATEOperations {

    public static void updateVote(String post_key, int value_to_increment) throws SQLException,Exception {
        StringBuilder update_req_builder = new StringBuilder();
        update_req_builder.append("UPDATE ").append(Tables.TABLE_NUCLEUS);
        update_req_builder.append(" SET ").append(PostFields.DB_NBVOTES).append(" = ");
        update_req_builder.append(PostFields.DB_NBVOTES).append("+ ").append(value_to_increment);
        update_req_builder.append(" WHERE ").append(PostFields.DB_KEY).append(" = ");
        update_req_builder.append("'").append(post_key).append("'");
        update_req_builder.append(";");

        Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(update_req_builder.toString());
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }

    public static void addPositiveVote(String post_key) throws SQLException,Exception{
        updateVote(post_key,1);
    }

    public static void addNegativeVote(String post_key) throws SQLException,Exception{
        updateVote(post_key,-1);
    }
}
