package me.voxnucleus.sql.select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CommonClasses.PostFields;
import me.voxnucleus.sql.VoxNucleusSQLConnection;
import me.voxnucleus.sql.util.ResultSetUtil;

/**
 *
 * @author victork
 */
public class NucleusSELECTOperations {
    private static final int NB_TO_RETRIEVE_STD=10;
    private int nb_to_retrieve;


    /**
     * Default constructor
     */

    protected NucleusSELECTOperations(){
        nb_to_retrieve= NB_TO_RETRIEVE_STD;
    }


    /**
     *
     * @return instance
     */
    public static NucleusSELECTOperations getInstance(){
        return new NucleusSELECTOperations();
    }

    /**
     * 
     * @param category
     * @param sub_category
     */

    public List<String> getBest(String category, String sub_category,
            long timestamp,int offset) throws SQLException,Exception{
        ArrayList<String> result= new ArrayList<String>();
        StringBuilder req_text = new StringBuilder();
        req_text.append("SELECT key FROM nucleus");
        req_text.append(" WHERE ");
        if(!category.equalsIgnoreCase("tout") || !sub_category.equalsIgnoreCase("tout")){
            
            if(!category.equalsIgnoreCase("tout") ){
                req_text.append("category='").append(category).append("'");
            }
            if(!category.equalsIgnoreCase("tout") && !sub_category.equalsIgnoreCase("tout")){
                 req_text.append(" AND ");
            }
            if(!sub_category.equalsIgnoreCase("tout")){
                req_text.append("sub_category='").append(sub_category).append("'");

            }
            req_text.append(" AND ");
        }
        req_text.append(PostFields.DB_TIMESTAMP_CREATED);
        req_text.append(">").append(" ? ");
        req_text.append(" ORDER BY ").append(PostFields.DB_NBVOTES).append(" LIMIT ").append(nb_to_retrieve);
        req_text.append(" OFFSET ").append(offset);
        req_text.append(";");
        Connection conn=null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            PreparedStatement stmt = conn.prepareStatement(req_text.toString());
            stmt.setTimestamp(1, new Timestamp(timestamp));
            ResultSet set = stmt.executeQuery();
             result=(ArrayList<String>) ResultSetUtil.ResultSetToList(set);
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }
        return result;
    }

    /**
     * Retrieve all new nucleus
     * @param category
     * @param sub_category
     */

    public List<String> getNew(String category,String sub_category,int offset) throws SQLException,Exception{
            StringBuilder req_text = new StringBuilder();
            ArrayList<String> result = new ArrayList<String>();
        req_text.append("SELECT key FROM nucleus");
        if(!category.equalsIgnoreCase("tout") || !sub_category.equalsIgnoreCase("tout")){
            req_text.append(" WHERE ");
            if(!category.isEmpty()){
                req_text.append("category='").append(category).append("'");
            }
            if(!category.equalsIgnoreCase("tout") && !sub_category.equalsIgnoreCase("tout")){
                 req_text.append(" AND ");
            }
            if(!sub_category.equalsIgnoreCase("tout")){
                req_text.append("sub_category='").append(sub_category).append("'");
            }
        }
        req_text.append(" ORDER BY ").append(PostFields.DB_TIMESTAMP_CREATED).append(" DESC ");
        req_text.append(" LIMIT ").append(nb_to_retrieve);
        req_text.append(" OFFSET ").append(offset);
        req_text.append(";");
        Connection conn=null;
        try {
            conn = VoxNucleusSQLConnection.openConnection();
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(req_text.toString());
             result=(ArrayList<String>) ResultSetUtil.ResultSetToList(set);
        } finally {
            VoxNucleusSQLConnection.closeConnection(conn);
        }
        return result;
    }

}
