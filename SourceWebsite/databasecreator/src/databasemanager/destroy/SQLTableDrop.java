package databasemanager.destroy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.voxnucleus.sql.VoxNucleusSQLConnection;

/**
 *
 * @author victork
 */
public class SQLTableDrop {

    protected void SQLTableDrop(){
        //Do nothing at the moment
    }

    public static SQLTableDrop getInstance(){
        return new SQLTableDrop();
    }

    public void drop() {
        Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openAdminConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE nucleus;");
            stmt.executeUpdate("DROP TABLE comments;");
            stmt.executeUpdate("DROP TABLE users;");
            stmt.executeUpdate("DROP TABLE votes;");
            
        } catch (SQLException ex) {
            Logger.getLogger(VoxNucleusSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }

}
