/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package me.voxnucleus.sql.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;
import me.FallingDownLib.CommonClasses.UserFields;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 *
 * @author victork
 */
public class UserINSERTOperations {

    public static void insertUser(String username,String email, UUID uuid, int reputation) throws SQLException,Exception{
        StringBuilder statement_text = new StringBuilder();
        statement_text.append("INSERT INTO ").append(Tables.TABLE_USERS);
        statement_text.append("(").append(UserFields.DB_USERNAME).append(",");
        statement_text.append(UserFields.DB_EMAIL).append(",").append(UserFields.DB_UUID);
        statement_text.append(",").append(UserFields.DB_REPUTATION);
        statement_text.append(") VALUES (?,?,?::uuid,?);");
        Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            PreparedStatement stmt = conn.prepareStatement(statement_text.toString());
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, uuid.toString());
            stmt.setInt(4, reputation);
            stmt.executeUpdate();
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }
}
