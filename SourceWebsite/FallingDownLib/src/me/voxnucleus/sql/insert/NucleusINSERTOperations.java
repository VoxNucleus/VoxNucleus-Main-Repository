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
import java.sql.Timestamp;
import java.util.UUID;
import me.voxnucleus.sql.VoxNucleusSQLConnection;

/**
 *
 * @author victork
 */
public class NucleusINSERTOperations {

        /**
     * Way to insert a new Nucleus
     * @param key
     * @param title
     * @param author
     * @param timestamp
     * @param timeUUID
     * @param category
     * @param sub_category
     * @param classification
     * @param nbVotes
     * @throws SQLException
     *
     * "INSERT INTO posts(key,nbVotes,sub_category,timestamp_creation,classification,title,category,author)VALUES(key,Technologies,key,Autres,201000123,Autres,20,key)"
     */
    public static void insertNucleus(String key, String title, String author, long timestamp,
            UUID timeUUID, String category, String sub_category, String classification,
            int nbVotes) throws SQLException,Exception{
        Connection conn = null;
        //TODO Add UUID
        String statement_text="INSERT INTO "
                + "nucleus(key, title, author, timestamp_creation,timeuuid,category,sub_category,classification,nbVotes) "
                + "VALUES ( ? , ? , ? ,?, ?::uuid , ? , ? , ? , ?);";
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            PreparedStatement stmt=conn.prepareStatement(statement_text);
            stmt.setString(1, key);
            stmt.setString(2, title);
            stmt.setString(3, author);
            Timestamp ts = new Timestamp(timestamp);
            stmt.setTimestamp(4, ts);
            stmt.setString(5, timeUUID.toString());
            stmt.setString(6, category);
            stmt.setString(7, sub_category);
            stmt.setString(8, category);
            stmt.setInt(9, nbVotes);
            stmt.executeUpdate();
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }

    }

}
