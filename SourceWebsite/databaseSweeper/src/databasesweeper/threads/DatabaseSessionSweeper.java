package databasesweeper.threads;

import databasesweeper.logger.SweepLogger;
import databasesweeper.threads.session.SessionToDelete;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.util.IterateOverKey;
import me.FallingDownLib.CommonClasses.FallingDownSessionFields;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;

/**
 *
 * @author victork
 */
public class DatabaseSessionSweeper extends Thread {

    private long sleepTime;
    private final int NB_SESSION_TO_RETRIEVE = 500;
    private SessionToDelete sessionsToDelete;
    private long nowTimestamp=0;
    private int nbDeletedSessions;

    public static long TIME_TO_SLEEP=1800*1000;
    public DatabaseSessionSweeper(long sleep, String name) {
        super(name);
        this.sleepTime = sleep;


    }

    @Override
    public void run() {
        for (;;) {
            destroyOldSessions();
            printSummary();
            goToSleep();
        }
    }

    /**
     * Enter cycle here
     */

    private void destroyOldSessions() {
        nbDeletedSessions=0;
        sessionsToDelete = new SessionToDelete();
        IterateOverKey keyIterator = new IterateOverKey(FallingDownConnector.DB_SESSIONS, NB_SESSION_TO_RETRIEVE);
        boolean hasKeyLeft=true;
        while (hasKeyLeft) {
            Date d= new Date();
            nowTimestamp=d.getTime();
            HashMap<String, List<Column>> sessionsToVerify = keyIterator.getNextSet(keyIterator.startingKey);
            if(sessionsToVerify.isEmpty())
                hasKeyLeft=false;
            else
                analyzeHashMap(sessionsToVerify);

        }
        sessionsToDelete.flushList();
    }

    private void analyzeHashMap(HashMap<String, List<Column>> sessionsToVerify) {
        Iterator<String> hashIterator = sessionsToVerify.keySet().iterator();
        while (hashIterator.hasNext()) {
            String key = hashIterator.next();
            analyseKey(key, sessionsToVerify.get(key));
        }
    }

    /**
     * Function that is made in order for the thread to go to sleep for a period of time (given in the
     * constructor of the class)
     */
    private void goToSleep() {
        try {
            DatabaseSessionSweeper.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(DatabaseSweeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param key
     * @param ColumnsOfKey
     */

    private void analyseKey(String key, List<Column> ColumnsOfKey) {
        long expirationTime=getExpirationTime(ColumnsOfKey);
        if(expirationTime<nowTimestamp){
            nbDeletedSessions++;
            sessionsToDelete.addKey(key);
        }
            
    }


    //TODO ERROR
    private long getExpirationTime(List<Column> columns) {
        int index = 0;
        long expirationDate = 0;
        while (index < columns.size()) {
            if (StringUtils.string(columns.get(index).name).equals(FallingDownSessionFields.DB_EXPIRATION_DATE)) {
                try {
                    expirationDate = Long.parseLong(StringUtils.string(columns.get(index).value));
                } catch (NumberFormatException ex) {
                    expirationDate = 0;
                }
            }
            index++;
        }
        return expirationDate;
    }

     /**
     * Print any easy to read summary of all operations that were done in one cycle
     *
     */
    private void printSummary() {
        SweepLogger.getInstance().insertInfo("During the cycle, Thread " + this.getName() + "has deleted "
                + this.nbDeletedSessions + " sessions into Database "+FallingDownConnector.DB_SESSIONS);
    }
}
