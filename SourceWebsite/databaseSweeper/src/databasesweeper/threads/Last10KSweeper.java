package databasesweeper.threads;

import databasesweeper.logger.SweepLogger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.SubCategories;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * In charge of deleting
 * @author victork
 */
public class Last10KSweeper extends Thread {

    static final int NB_MAX_POSTS = 10000;
    private long sleepTime = 1800 * 1000;
    private int deletedPostNumber;
    private String current_category;
    private String current_sub_category;

    /**
     * Constructor of the Last10KSweeper.
     * First parameter is the time (in millisecond) and the second is the name of the thread
     * @param time
     * @param name
     */
    public Last10KSweeper(long time, String name) {
        super(name);

    }

    @Override
    public void run() {

        for (;;) {
            deletedPostNumber = 0;
            checkLast10KDatabase();
            printSummary();
            goToSleep();

        }

    }

    /**
     * Connect to the database.
     * Catch error in case of a problem of connection
     */
    private void checkLast10KDatabase() {
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            goThrewDatabases(connector);

        } catch (Exception e) {
            SweepLogger.getInstance().insertError("Last10KSweeper : " + e.toString());
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    /**
     * Go across the categories and sub categories
     * @param connector
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */
    private void goThrewDatabases(FallingDownConnector connector) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException {
        for (int category_index = 0; category_index < Categories.MAIN_CATEGORIES.length; category_index++) {
            current_category = Categories.MAIN_CATEGORIES[category_index];
            for (int sub_category_index = 0; sub_category_index < SubCategories.SUB_CATEGORIES[category_index].length; sub_category_index++) {
                current_sub_category = SubCategories.SUB_CATEGORIES[category_index][sub_category_index];
                beginRetrieve(connector);
            }
        }
    }

    /**
     * Get the whole Column
     * @param connector
     * @throws InvalidRequestException
     * @throws NotFoundException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws TException
     */
    private void beginRetrieve(FallingDownConnector connector) throws InvalidRequestException,
            NotFoundException, UnavailableException, TimedOutException, TException {
        int columnNumber = connector.getCount(FallingDownConnector.DB_RECENT_POSTS, null, "Posts" + current_category + current_sub_category);
        if (columnNumber > NB_MAX_POSTS) {
            deletePosts(connector);
        }
    }

    /**
     * Delete all posts after the NB_MAX_POSTS th
     * @param list
     * @param connector
     */
    private void deletePosts(FallingDownConnector connector) {
        ArrayList<Column> list;
        try {
            list = new ArrayList<Column>(connector.getSlice(FallingDownConnector.DB_RECENT_POSTS, "Posts"+ current_category + current_sub_category));
            int numberToDelete = NB_MAX_POSTS/10;
            for (int i = (list.size() - numberToDelete); i < list.size(); i++) {
                try {
                    connector.deleteColumnInSuperColumn(FallingDownConnector.DB_RECENT_POSTS,
                            "Posts" + current_category + current_sub_category, null, list.get(i).getName());
                    deletedPostNumber++;
                } catch (Exception e) {
                    System.out.println("Post already deleted" + "Erreur :" + e.toString());
                }
            }
        } catch (InvalidRequestException ex) {
            Logger.getLogger(Last10KSweeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(Last10KSweeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(Last10KSweeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(Last10KSweeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(Last10KSweeper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Print any easy to read summary of all operations that were done in one cycle
     *
     */
    private void printSummary() {
        SweepLogger.getInstance().insertInfo("During the cycle, Thread " + 
                this.getName() + "has deleted " + this.deletedPostNumber
                + " posts into Database last posts");
    }

    /**
     * Function that is made in order for the thread to go to sleep for a period of time (given in the
     * constructor of the class)
     */
    private void goToSleep() {
        try {
            Last10KSweeper.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(DatabaseSweeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}