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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.common.Tables;

/**
 * List of static methods to insert some things
 * @author victork
 */
public class VoxOperations {

    private static void insertStatement(String tableName,HashMap<String,String> col_val) throws SQLException,Exception{
        StringBuilder statement_text_builder = new StringBuilder();
        ArrayList<String> list_col_name = new ArrayList<String>();
        ArrayList<String> list_col_value = new ArrayList<String>();

        Iterator<String> col_iterator = col_val.keySet().iterator();
        while(col_iterator.hasNext()){
            String col_name = col_iterator.next();
            list_col_name.add(col_name);
            list_col_value.add(col_val.get(col_name));
        }

        statement_text_builder.append("INSERT INTO ").append(tableName);
        statement_text_builder.append("(");
        statement_text_builder.append(list_col_name.get(0));
        for(int index_col=1;index_col<list_col_name.size();index_col++){
            statement_text_builder.append(",").append(list_col_name.get(index_col));
        }

        statement_text_builder.append(")");
        statement_text_builder.append("VALUES");
        statement_text_builder.append("('");
        statement_text_builder.append(list_col_value.get(0)).append("'");
        for(int index_col=1;index_col<list_col_value.size();index_col++){
            statement_text_builder.append(",'").append(list_col_value.get(index_col));
            statement_text_builder.append("'");
        }
        statement_text_builder.append(")");
        
        Connection conn = null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            Statement stmt=conn.createStatement();
            stmt.executeUpdate(statement_text_builder.toString());
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }




    }

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

    public static void insertUser() throws SQLException,Exception{

        insertStatement(Tables.TABLE_USERS,null);
    }

}
