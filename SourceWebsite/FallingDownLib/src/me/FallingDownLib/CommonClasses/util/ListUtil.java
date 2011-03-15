package me.FallingDownLib.CommonClasses.util;

import java.util.List;

/**
 *
 * @author victork
 */
public class ListUtil {

    public static List subList(List in,int begin,int end){
        int begin_index=Math.min(begin, in.size());
        int end_index=Math.min(in.size(), end);
        return in.subList(begin_index, end_index);
    }

}
