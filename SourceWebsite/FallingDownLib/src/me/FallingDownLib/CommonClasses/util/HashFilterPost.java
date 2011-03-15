package me.FallingDownLib.CommonClasses.util;

import java.util.HashMap;
import me.FallingDownLib.CommonClasses.PostFields;

/**
 * This class hides informations and delete misformed
 * @author victork
 */
public class HashFilterPost {

    HashMap<String, String> work;

    private String toFilter[] = {"TimeUUID",PostFields.UUID,PostFields.DB_POSTER_IP,PostFields.DB_POSTER_REMOTE_ADDRESS};

    public HashFilterPost(HashMap<String, String> in) {
        work = in;
        filter();

    }

    /**
     * Filter the HashMap according to the toFilter array.
     */
    private void filter(){
        for(int index=0;index<toFilter.length;index++){
            work.remove(toFilter[index]);
        }
    }


    /**
     * Return the result
     * @return HashMap filtered
     */

    public HashMap<String, String> getHashBack() {
        return work;
    }
}
