package me.voxnucleus.sql.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.FallingDownLib.CommonClasses.UserFields;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 *
 * @author victork
 */
public class UserUPDATEOperations {

    /**
     * Update reputation by any given quantity
     * @param user_id
     * @param increment_value
     * @throws SQLException
     */
    public static void updateReputation(String user_id, int increment_value) throws SQLException,Exception{
        StringBuilder req_builder = new StringBuilder();
        req_builder.append("UPDATE ").append(Tables.TABLE_USERS);
        req_builder.append(" SET ").append(UserFields.DB_REPUTATION).append(" = ");
        req_builder.append(UserFields.DB_REPUTATION).append("+ ").append(increment_value);
        req_builder.append(" WHERE ").append(UserFields.DB_USERNAME).append(" = ");
        req_builder.append("'").append(user_id).append("'");
        req_builder.append(";");

        Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(req_builder.toString());
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }
    /**
     * Update the reputation of an user by adding 1 to it
     * @param user_id
     * @throws SQLException
     */

    public void addReputation(String user_id) throws SQLException,Exception{
        updateReputation(user_id,1);
    }

    /**
     * Update by -1 the reputation of an user
     * @param user_id
     * @throws SQLException
     */
    public void decreaseReputation(String user_id) throws SQLException,Exception{
        updateReputation(user_id,-1);
    }
}
