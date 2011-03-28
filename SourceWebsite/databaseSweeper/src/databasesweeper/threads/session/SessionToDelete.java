package databasesweeper.threads.session;

import java.util.ArrayList;
import me.FallingDownLib.CassandraConnection.connectors.SessionConnector;


/**
 * This class contains a set of keys that has to be deleted.
 * It delete by Batch, which is faster
 * @author victork
 */
public class SessionToDelete {

    private final int MAX_NUMBER_KEYS = 50;
    private ArrayList<String> keysToDelete;

    public SessionToDelete() {
        keysToDelete = new ArrayList<String>();
    }

    /**
     * Add one key to the list
     * @param keyToDelete
     */


    public void addKey(String keyToDelete) {
        if (keysToDelete.size() > MAX_NUMBER_KEYS) {
            keysToDelete.add(keyToDelete);
            deleteKeys();
        } else {
            keysToDelete.add(keyToDelete);
        }
    }

    /**
     * Call it before ending everything
     */
    public void flushList()  {
        if(!keysToDelete.isEmpty())
            deleteKeys();
    }

    /**
     * TODO : Batch delete
     */
    private void deleteKeys() {
        for(int index = 0 ;index <keysToDelete.size();index++){
            SessionConnector sToDelete = SessionConnector.getConnector(keysToDelete.get(index));
            sToDelete.deleteSession();
        }

    }
}
