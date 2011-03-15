package me.voxnucleus.sql.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author victork
 */
public class ResultSetUtil {

    /**
     * WARNING MUST BE ONLY USED IF KEY IS FIRST
     * @return
     */
    public static List<String> ResultSetToList(ResultSet set) throws SQLException{
        ArrayList<String> result = new ArrayList<String>();
        while(set.next()){
            result.add(set.getString(1));
        }


        return result;

    }
}
