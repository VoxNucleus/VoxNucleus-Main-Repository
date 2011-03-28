package FallingDown.newpost;

import FallingDown.newpost.JSON.JSONres;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.common.ColumnIterator;
import me.FallingDownLib.CassandraConnection.connectors.PostConnector;
import me.FallingDownLib.CommonClasses.PostFields;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class URLVerificator {

    public final int MAX_NUMBER_TO_RETRIEVE=5;
    private PostToVerify verify;
    private String url;

    protected URLVerificator(String in_url){
        url=in_url;
    }
    public static URLVerificator getInstance(String in_url){
        return new URLVerificator(in_url);
    }

    public void associatePostToVerify(PostToVerify pVerify){
        verify=pVerify;
        url=pVerify.getURL();
    }


    public JSONres[] getList() {
        ColumnIterator colIterator = ColumnIterator.getColIterator(ColumnFamilies.DB_POST_BY_URL, url, MAX_NUMBER_TO_RETRIEVE + 1);
        List<Column> listCol = colIterator.get(0, MAX_NUMBER_TO_RETRIEVE);
        JSONres[] arraypostId = new JSONres[listCol.size()];
        for (int index = 0; index < listCol.size(); index++) {
            try {
                String postId = StringUtils.string(listCol.get(index).getValue());
                PostConnector pConnector = PostConnector.getInstance(postId);
                String title = StringUtils.string(pConnector.getField(StringUtils.bytes(PostFields.DB_TITLE)));
                JSONres jsonres = new JSONres(postId, title);
                arraypostId[index] = jsonres;
            } catch (Exception ex) {
                Logger.getLogger(URLVerificator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arraypostId;
    }
}
