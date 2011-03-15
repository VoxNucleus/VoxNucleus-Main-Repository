package me.FallingDownLib.functions.bestbydate;

import java.util.ArrayList;
import me.voxnucleus.sql.select.NucleusSELECTOperations;
import me.voxnucleus.time.TimeEquivalent;

/**
 * This class is dedicated to retrieve informations from the database.
 * It is called by the other BestInXXX
 * @author victork
 */
public class RetrieveBestByDate {
    private String chosen_key;
    private String category;
    private String sub_category;
    private int begin;
    private int number;

    private ArrayList<String> listKeys;

    /**
     * First constructor
     * @param key
     * @param beginning
     * @param numberToRetrieve
     * @param category
     * @param sub_category
     */

    public RetrieveBestByDate(String key,int beginning,int numberToRetrieve, String category, String sub_category){
        chosen_key=key;
        begin = beginning;
        number = numberToRetrieve;
        this.category=category;
        this.sub_category=sub_category;
    }

    /**
     * This method get a list of keys that need to be retrieved in order to have the top posts
     * Update : safer with finally
     * TODO integration of a shift
     * @return
     * @throws Exception
     */
    public ArrayList<String> getKeys() throws Exception{
        listKeys=new ArrayList<String>();
        NucleusSELECTOperations select_op=NucleusSELECTOperations.getInstance();
        listKeys=new ArrayList<String>(select_op.getBest(category, sub_category,
                TimeEquivalent.get_timestamp_from(chosen_key) , begin));
        return listKeys;
    }

}