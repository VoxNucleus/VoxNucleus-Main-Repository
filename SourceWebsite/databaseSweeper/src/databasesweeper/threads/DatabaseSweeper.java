package databasesweeper.threads;

import databasesweeper.logger.SweepLogger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CassandraConnection.connectors.PostConnector;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.CommonClasses.util.TimeIdentifier;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.Column;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class DatabaseSweeper extends Thread {

    private String chosen_DB;
    private long sleepTime;
    private long nowTimeStamp;
    private long lifeTime;
    private String nextDatabase;
    private int movedNumber;

    private String category;
    private String sub_category;

    /**
     * Set up of the thread
     * @param threadName
     * @param chosen_DB
     * @param duree
     */

    public DatabaseSweeper(String threadName, String chosen_DB, long duree) {
        super(threadName);
        this.chosen_DB = chosen_DB;
        this.sleepTime = duree;
        setLifeTime();
        setNextDatabase();
    }

    /**
     * Infinite loop on an operation, then go to sleep for a certain amount of time.
     */
    @Override
    public void run() {
        for (;;) {
            this.movedNumber = 0;
            launchProcess();
            printSummary();
            goToSleep();
        }
    }

    /**
     * 
     */

    private void launchProcess(){
        for(int category_index=0;category_index < Categories.MAIN_CATEGORIES.length;category_index++){
            category=Categories.MAIN_CATEGORIES[category_index];
            for(int sub_category_index=0;sub_category_index < SubCategories.SUB_CATEGORIES[category_index].length;sub_category_index++){
                sub_category = SubCategories.SUB_CATEGORIES[category_index][sub_category_index];
                openDBConnection();
            }
        }
    }


    private void openDBConnection() {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            List<SuperColumn> list = connector.getSuperSliceWithPredicate(FallingDownConnector.DB_BEST_POSTS_BY_DATE, null, chosen_DB + category + sub_category);
            verifyListBySuperColumns(list, connector);

        } catch (Exception ex) {
            Logger.getLogger(DatabaseSweeper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    /**
     * Small iteration through all the superColumns
     * @param list
     * @param connector
     */
    private void verifyListBySuperColumns(List<SuperColumn> list, FallingDownConnector connector) {
        for (int index = 0; index < list.size(); index++) {
            verifyListByColumns(list.get(index).getColumns(), list.get(index).getName(), connector);
        }
    }

    /**
     * For each column inside a SuperColumn, check the Timestamp.
     * If post is too old is it shifted to the next Table
     * @param listColumns
     */
    private void verifyListByColumns(List<Column> listColumns, byte[] bSuperColumnName, FallingDownConnector connector) {
        Date d = new Date();
        nowTimeStamp = d.getTime();
        for (int indexCol = 0; indexCol < listColumns.size(); indexCol++) {
            if (checkTimestamp(StringUtils.string(listColumns.get(indexCol).value))) {
                moveIntoNextDatabase(listColumns.get(indexCol), bSuperColumnName, connector);
            }

        }
    }

    /**
     *Move a column col from SuperColumn into the this.nextdatabase;
     * Print a small statement if it works
     * Print the exception if it fails
     * @param col
     * @param bSuperColumnName
     * @param connector
     */
    private void moveIntoNextDatabase(Column col, byte[] bSuperColumnName, FallingDownConnector connector) {
        try {
            connector.deleteColumnInSuperColumn(FallingDownConnector.DB_BEST_POSTS_BY_DATE, chosen_DB+category+sub_category, bSuperColumnName, col.getName());
            connector.insertInfoSuperColumnWithKey(this.nextDatabase+category+sub_category,
                    bSuperColumnName, StringUtils.string(col.name),
                    StringUtils.string(col.value), FallingDownConnector.DB_BEST_POSTS_BY_DATE);
            this.movedNumber++;
        } catch (Exception e) {
            SweepLogger.getInstance().insertError("ERROR key not present, " + e.toString());
        }

    }

    /**
     * Verify age of timeStamp
     * @param stampToCheck
     * @return
     */
    private boolean checkTimestamp(long stampToCheck) {
        if ((this.nowTimeStamp*1000 - stampToCheck) > lifeTime) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkTimestamp(String postId) {

        PostConnector pConnector=PostConnector.getInstance(postId);
        long stampToCheck;
        try {
            stampToCheck = Long.parseLong(StringUtils.string(pConnector.getField(StringUtils.bytes(PostFields.DB_TIMESTAMP_CREATED))));
            if ((this.nowTimeStamp - stampToCheck) > lifeTime) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        } catch (NotFoundException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        } catch (TException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        } catch (IllegalStateException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        } catch (PoolExhaustedException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        } catch (Exception ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        }

        return false;
    }


    /**
     * Print any easy to read summary of all operations that were done in one cycle
     *
     */
    private void printSummary() {
        SweepLogger.getInstance().insertInfo("During the cycle, Thread " + this.getName() + " has moved "
                + this.movedNumber + " posts into"
                + this.nextDatabase);
    }

    /**
     * Function that is made in order for the thread to go to sleep for a period of time (given in the
     * constructor of the class)
     */
    private void goToSleep() {
        try {
            DatabaseSweeper.sleep(sleepTime);
        } catch (InterruptedException ex) {
            SweepLogger.getInstance().insertError(ex.toString());
        }
    }

    /**
     * This function determine the LifeTime necessary.
     * Warning because of the timestamp set to zero, the lifeTime of the different posts have to be set carefuly
     *
     */
    private void setLifeTime() {
        this.lifeTime=TimeIdentifier.getLifeTimeFromDatabase(chosen_DB);
    }

    private void setNextDatabase() {

        if (chosen_DB.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_24H)) {
            this.nextDatabase = FallingDownConnector.KEY_POSTS_1WEEK;
        }
        if (chosen_DB.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1WEEK)) {
            this.nextDatabase = FallingDownConnector.KEY_POSTS_1MONTH;
        }
        if (chosen_DB.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1MONTH)) {
            this.nextDatabase = FallingDownConnector.KEY_POSTS_1YEAR;
        }
        if (chosen_DB.equalsIgnoreCase(FallingDownConnector.KEY_POSTS_1YEAR)) {
            //TODO
            //this.nextDatabase=FallingDownConnector.KEY_POSTS_1YEAR;
        }

    }
}
