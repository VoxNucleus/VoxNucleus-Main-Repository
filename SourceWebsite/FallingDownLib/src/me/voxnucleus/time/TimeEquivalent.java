package me.voxnucleus.time;

import java.util.Calendar;

/**
 *
 * @author victork
 */
public class TimeEquivalent {

    public final static String POSTS_24H = "Best24Hours";
    public static final String POSTS_1WEEK = "Best1Week";
    public static final String POSTS_1MONTH = "Best1Month";
    public static final String POSTS_1YEAR = "Best1Year";


    public static long get_timestamp_from(String time){
        long timestamp_then = Calendar.getInstance().getTimeInMillis();

        if(time.equalsIgnoreCase(POSTS_24H)){
            timestamp_then-=24l*3600l*1000l;
        }else if(time.equalsIgnoreCase(POSTS_1WEEK)){
            timestamp_then-=7l*24l*3600l*1000l;
        }else if(time.equalsIgnoreCase(POSTS_1MONTH)){
            timestamp_then-=30l*24l*3600l*1000l;
        }else if(time.equalsIgnoreCase(POSTS_1YEAR)){
            timestamp_then-=365l*24l*3600l*1000l;
        }else{
            timestamp_then-=3l*365l*24l*3600l*1000l;
        }
        return timestamp_then;
    }
}
