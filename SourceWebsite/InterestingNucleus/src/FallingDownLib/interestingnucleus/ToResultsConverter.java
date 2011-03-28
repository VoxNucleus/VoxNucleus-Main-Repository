package FallingDownLib.interestingnucleus;

import FallingDownLib.interestingnucleus.logger.InterestingLogger;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.DatabaseUtil;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.functions.interestingposts.InterestingPostEntry;
import me.FallingDownLib.functions.interestingposts.InterestingPostList;
import me.FallingDownLib.functions.interestingposts.postIdComparator;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class ToResultsConverter {

    private String category;
    private String sub_category;
    private static final String COLUMN_INTERESTING_POST_RESULT = "result";

    /**
     * 
     * @param cat
     * @param sub_cat
     */
    private ToResultsConverter(String cat, String sub_cat){
        category=cat;
        sub_category=sub_cat;
    }
    /**
     *
     * @param cat
     * @param sub_cat
     * @return
     */
    public static ToResultsConverter getInstance(String cat, String sub_cat){
        return new ToResultsConverter(cat,sub_cat);
    }
    /**
     *
     * @return
     */

    private InterestingPostList getInterestingPostList(){
        return InterestingPostList.getObjectFromCassandra(category, sub_category);
    }

    /**
     * 
     */
    public void exportToCassandra(){
        InterestingPostList interestingList = getInterestingPostList();
        ArrayList<InterestingPostEntry> list = new ArrayList<InterestingPostEntry>();
        if (interestingList != null) {
            Iterator<String> iterator = interestingList.keySet().iterator();
            while (iterator.hasNext()) {
                list.add(interestingList.get(iterator.next()));
            }
            Collections.sort(list, new postIdComparator());
        }
        ArrayList<String> sortedStringList = convertList(list);
        doSave(sortedStringList);
    }

    /**
     * 
     * @param sortedStringList
     */
    private void doSave(ArrayList<String> sortedStringList){
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            ByteArrayOutputStream bstreamout = new ByteArrayOutputStream();
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bstreamout);
            out.writeObject(sortedStringList);
            connector.insertInfoColumnWithkey(InterestingPostList.KEY_INTERESTING_POST_LIST+category+sub_category,
                    StringUtils.bytes(COLUMN_INTERESTING_POST_RESULT),
                    bstreamout.toByteArray(), DatabaseUtil.DB_SERIALIZABLE);
            InterestingLogger.getInstance().insertInfo(sortedStringList.size()
                    + " noyaux intéressants dans la catégorie : "+category
                    + " sous-cat "+sub_category );
        } catch (PoolExhaustedException ex) {
            InterestingLogger.getInstance().insertError(ex.toString());
        } catch (TException ex) {
            InterestingLogger.getInstance().insertError(ex.toString());
        } catch (Exception ex) {
            InterestingLogger.getInstance().insertError(ex.toString());
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    /**
     * 
     * @param list
     * @return
     */

    private ArrayList<String> convertList(ArrayList<InterestingPostEntry> list) {
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < list.size(); index++) {

            result.add(list.get(index).getPostId());
        }
        return result;
    }
}
