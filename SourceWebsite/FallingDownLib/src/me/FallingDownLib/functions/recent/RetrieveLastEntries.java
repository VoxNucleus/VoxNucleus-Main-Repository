package me.FallingDownLib.functions.recent;

import java.util.ArrayList;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.voxnucleus.sql.select.NucleusSELECTOperations;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * Class that retrieve the last Entries
 * @author victork
 */
public class RetrieveLastEntries {

    private static final int MAX_NUMBER_TO_RETRIEVE=10;

    private int number_to_retrieve;
    private int offset;
    private String category;
    private String sub_category;

    public RetrieveLastEntries(byte[] lastTimeUUID){
        
    }

    /**
     * Retrieve the numberToRetrieve first posts, with an offset of beginning items
     * @param beginning
     * @param numberToRetrieve
     * @throws PoolExhaustedException
     * @throws TException
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws TimedOutException
     * @throws Exception
     */

    public RetrieveLastEntries(int beginning, int numberToRetrieve,String category,String sub_category) throws Exception {
        numberToRetrieve = Math.min(numberToRetrieve, MAX_NUMBER_TO_RETRIEVE);
        this.number_to_retrieve=numberToRetrieve;
        this.offset=beginning;
        this.category=category;
        this.sub_category=sub_category;

        
    }

    /**
     *        LatestConnector connector = LatestConnector.getInstance(offset+numberToRetrieve, category, sub_category);
        latestCol=new ArrayList<Column>(connector.getLatestColumns());
     * @return ArrayList of post ids
     */

    public ArrayList<String> returnResult() throws Exception {
 
        ArrayList<String> result = new ArrayList<String>();

        NucleusSELECTOperations sel_op= NucleusSELECTOperations.getInstance();
        sel_op.getNew(category, sub_category, offset);
        return result;
    }

}
