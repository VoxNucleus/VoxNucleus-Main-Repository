package databasesweeper;
import databasesweeper.threads.DatabaseSessionSweeper;
import databasesweeper.threads.DatabaseSweeper;
import databasesweeper.threads.Last10KSweeper;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
/**
 *
 * @author victork
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatabaseSweeper sweep24H = new DatabaseSweeper("Sweeper24H",FallingDownConnector.KEY_POSTS_24H,1000*3600);
        DatabaseSweeper sweep1Week = new DatabaseSweeper("Sweeper1Week",FallingDownConnector.KEY_POSTS_1WEEK,1000*3600*12);
        DatabaseSweeper sweep1Month = new DatabaseSweeper("Sweeper1Month", FallingDownConnector.KEY_POSTS_1MONTH,1000*3600*24*2);
        Last10KSweeper last10K = new Last10KSweeper(0,"Last10KSweeper");
        DatabaseSessionSweeper sessionSweeper = new DatabaseSessionSweeper(DatabaseSessionSweeper.TIME_TO_SLEEP,"Database Session Sweeper");

        sweep24H.start();
        sweep1Week.start();
        sweep1Month.start();
        last10K.start();
        sessionSweeper.start();

    }

}
