package me.FallingDownLib.CassandraConnection.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.cassandra.thrift.Column;
import me.prettyprint.cassandra.utils.StringUtils;

/**
 * set of utils to manipulate columns
 * @author victork
 */
public class ColumnUtil {

    /**
     * Transform a HashMap of String into an arrayList of columns
     * @param in
     * @return an arrayList of column ready to be batch Inserted
     */

    public static ArrayList<Column> HashStringToArrayCol(HashMap<String,String> in){
        Iterator<String> iterator=in.keySet().iterator();
        HashMap<String,byte[]> transition=new HashMap<String,byte[]>();
        while(iterator.hasNext()){
            String key=iterator.next();
            transition.put(key, StringUtils.bytes(in.get(key)));
        }
        return HashToArrayCol(transition);
        
    }


    /**
     * Transform a HashMap of byte[] into an arrayList of columns
     * @param in
     * @return an arrayList of column ready to be batch Inserted
     */

    public static ArrayList<Column> HashToArrayCol(HashMap<String,byte[]> in){
        Calendar cal =Calendar.getInstance();
        ArrayList<Column> transformed = new ArrayList<Column>();
        Iterator<String> iterator = in.keySet().iterator();
        while(iterator.hasNext()){
            String name = iterator.next();
            transformed.add(new Column(
                    StringUtils.bytes(name),
                    in.get(name),
                    cal.getTimeInMillis()*1000l
                    ));
        }
        return transformed;
    }


    /**
     * Transformation of a list of columns to a HashMap of STRING
     * @param listCol
     * @return HashMap : Keys are column names and data of keys are values transformed to String
     */

    public static HashMap<String,String> ArrayToHashString(List<Column> listCol){
        HashMap<String,String> result= new HashMap<String,String>();
        for(int colIndex = 0; colIndex < listCol.size();colIndex++){
            result.put(StringUtils.string(listCol.get(colIndex).getName()),
                    StringUtils.string(listCol.get(colIndex).getValue()) );
        }
        return result;
    }


    /**
     * Transformation of a list of columns to a HashMap of raw byte
     * @param listCol
     * @return HashMap : Keys are column names and data of keys are values transformed to String
     */

    public static HashMap<String,byte[]> ArrayToHashByte(List<Column> listCol){
        HashMap<String,byte[]> result= new HashMap<String,byte[]>();
        for(int colIndex = 0; colIndex < listCol.size();colIndex++){
            result.put(StringUtils.string(listCol.get(colIndex).getName()),
                   listCol.get(colIndex).getValue());
        }
        return result;
    }
    
    /**
     * 
     * @param listCol
     * @return a list of post ids
     */

    public static List<String> listColTolistString_value(List<Column> listCol){
        ArrayList<String> result = new ArrayList<String>();
        for(int index=0;index<listCol.size();index++){
            result.add(StringUtils.string(listCol.get(index).getValue()));
        }
        return result;
    }
}
