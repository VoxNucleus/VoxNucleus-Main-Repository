package me.voxnucleus.dbcreator.sql;

import databasemanager.loadfile.TextFileLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.voxnucleus.configuration.admin.PRIVATE_INFORMATIONS;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 *
 * @author victork
 */
public class SQLTableCreate {
    String path="/opt/postgresql/sql/";

    protected SQLTableCreate(){
        //Nothing
    }

    public static SQLTableCreate getInstance(){
        return new SQLTableCreate();
    }

    public void create(){
         Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openAdminConnection();
            Statement stmt = conn.createStatement();
            createTableNucleus(stmt);
            createTableComments(stmt);
            createTableUsers(stmt);
            createTableVotes(stmt);
            grantprivileges(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(VoxNucleusSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            VoxNucleusSQLConnection.closeConnection(conn);
        }
    }

    /**
     * Create table of the nucleus
     * @param stmt
     * @throws SQLException
     */

    public void createTableNucleus(Statement stmt) throws SQLException{
        TextFileLoader file_load = TextFileLoader.getInstance(path+"table_nucleus.sql");
        stmt.executeUpdate(file_load.getFContent());
    }

    public void createTableComments(Statement stmt) throws SQLException{
        TextFileLoader file_load = TextFileLoader.getInstance(path+"table_comments.sql");
        stmt.executeUpdate(file_load.getFContent());
    }

    public void createTableUsers(Statement stmt) throws SQLException{
        TextFileLoader file_load = TextFileLoader.getInstance(path+"table_users.sql");
        stmt.executeUpdate(file_load.getFContent());
    }

    public void createTableVotes(Statement stmt) throws SQLException{
        TextFileLoader file_load = TextFileLoader.getInstance(path+"table_votes.sql");
        stmt.executeUpdate(file_load.getFContent());
    }

    private void grantprivileges(Statement stmt) throws SQLException {
        String privilege_grant = "GRANT SELECT, INSERT, UPDATE, DELETE "
                + " ON "+Tables.TABLE_COMMENTS+","+Tables.TABLE_NUCLEUS+","+
                Tables.TABLE_USERS+","+Tables.TABLE_VOTES
                + " TO "+PRIVATE_INFORMATIONS.PSQL_USER;
        stmt.executeUpdate(privilege_grant);
    }
        

}
