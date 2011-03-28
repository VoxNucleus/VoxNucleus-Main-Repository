package FallingDown.redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class LinkGrabber {

    String link;

    String id;
    /**
     * Constructor of the class.
     * Take the post Id in parameter
     */
    public LinkGrabber(String postId) throws PoolExhaustedException, TException,
            NotFoundException, InvalidRequestException, UnavailableException, Exception {
        analyseId(postId);
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            link = StringUtils.string(connector.getInfoColumnWithkey(id, "link", FallingDownConnector.DB_POSTS));
            changeCounter(connector);
            connector.release();

        } finally {
            if (connector != null) {
                connector.release();
            }
        }
    }

    /**
     * Add one to the counter. In case of NotFoundException the variable is initialized.
     * @param connector
     * @throws InvalidRequestException
     * @throws UnavailableException
     * @throws Exception
     */

    private void changeCounter(FallingDownConnector connector) throws InvalidRequestException,
            UnavailableException, Exception {
        try {
            long nbViews = Long.parseLong(
                    StringUtils.string(
                    connector.getInfoColumnWithkey(id, "nbRedirect", FallingDownConnector.DB_POSTS)));
            nbViews++;
            connector.insertInfoColumnWithkey(id, "nbRedirect",
                    Long.toString(nbViews), FallingDownConnector.DB_POSTS);
        } catch (NotFoundException e) {
            connector.insertInfoColumnWithkey(id, "nbRedirect",
                    "1", FallingDownConnector.DB_POSTS);
        }
    }

    /**
     *
     * @return link of the post
     */
    public String getLink() {
        if (link.equals("null")) {
            return "/post/" + id;
        } else {
            return link;
        }
    }


    /**
     *
     * @param postId
     */
    private void analyseId(String postId) {
        Pattern forbiddenSymbols = Pattern.compile("\\W");
        Matcher sweeper = forbiddenSymbols.matcher(postId);
        StringBuffer purgedText = new StringBuffer();
        while(sweeper.find()){
            sweeper.appendReplacement(purgedText, "");
        }
        sweeper.appendTail(purgedText);
        this.id=purgedText.toString();
    }

}
