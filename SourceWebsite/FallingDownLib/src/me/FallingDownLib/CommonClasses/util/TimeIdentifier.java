package me.FallingDownLib.CommonClasses.util;

import java.util.Calendar;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;

/**
 * Gives lifetme
 * WARNING NOW IN MICROSECOND
 * @author victork
 */
public class TimeIdentifier {

    //For timestamp created by Cassandra
    static long lifetimeIn24H=1000l * 1000l * 3600l * 24l;
    static long lifetimeIn1Week=1000l*1000l* 3600l * 24l * (7l - 1l);
    static long lifetimeIn1Month=1000l * 1000l * 3600l * 24l * (31l - 7l);
    static long lifetimeIn1Year= 1000l * 1000l * 3600l * 24l * (365l - 24l);

    //For timestamp created by program
    static long lifetimeIn24HFromDatabase= 1000l * 3600l * 24l;
    static long lifetimeIn1WeekFromDatabase=1000l* 3600l * 24l * (7l);
    static long lifetimeIn1MonthFromDatabase= 1000l * 3600l * 24l * (31l);
    static long lifetimeIn1YearFromDatabase= 1000l * 3600l * 24l * (365l);



    public static String getKeyWithTime(long time) {
        if (time < lifetimeIn24H) {
            return FallingDownConnector.KEY_POSTS_24H;
        } else {
            if (time < lifetimeIn1Week) {
                return FallingDownConnector.KEY_POSTS_1WEEK;
            } else {
                if (time < lifetimeIn1Month) {
                    return FallingDownConnector.KEY_POSTS_1MONTH;
                } else {
                    if (time < lifetimeIn1Year) {
                        return FallingDownConnector.KEY_POSTS_1YEAR;
                    } else {
                        //TODO
                    }
                }
            }
        }
        return "ERROR";
    }
    /**
     *
     * @param key
     * @return
     */

    public static long getLifeTime(String key){
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_24H)) {
            return lifetimeIn24H;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1WEEK)) {
            return lifetimeIn1Week;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1MONTH)) {
            return lifetimeIn1Month;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1YEAR)) {
            return lifetimeIn1Year;
        }
        return 0;
    }

        /**
     *
     * @param key
     * @return
     */

    public static long getLifeTimeFromDatabase(String key){
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_24H)) {
            return lifetimeIn24HFromDatabase;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1WEEK)) {
            return lifetimeIn1WeekFromDatabase;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1MONTH)) {
            return lifetimeIn1MonthFromDatabase;
        }
        // I change to 6 days because the timestamp when inserted / deleted is remade
        if (key.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1YEAR)) {
            return lifetimeIn1YearFromDatabase;
        }
        return 0;
    }



     /**
     *
     * @param time
     * @return
     */
    public static String getKeyWithTimeFromDatabase(long timestamp) {
        Calendar cal = Calendar.getInstance();
        long time = cal.getTimeInMillis() - timestamp;
        if (time < lifetimeIn24HFromDatabase) {
            return FallingDownConnector.KEY_POSTS_24H;
        } else {
            if (time < lifetimeIn1WeekFromDatabase) {
                return FallingDownConnector.KEY_POSTS_1WEEK;
            } else {
                if (time < lifetimeIn1MonthFromDatabase) {
                    return FallingDownConnector.KEY_POSTS_1MONTH;
                } else {
                    if (time < lifetimeIn1YearFromDatabase) {
                        return FallingDownConnector.KEY_POSTS_1YEAR;
                    } else {
                        //TODO
                    }
                }
            }
        }
        return "ERROR";
    }


    /**
     *
     * @param time
     * @return
     */
    public static String getKeyWithTimeFromDatabase(String stringTime) {
        return getKeyWithTimeFromDatabase(Long.parseLong(stringTime));
        
    }

}
