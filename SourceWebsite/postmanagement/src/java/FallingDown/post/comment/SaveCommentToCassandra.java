package FallingDown.post.comment;

import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.Comment;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
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
public class SaveCommentToCassandra {

    Comment commentToSave;
    byte[] UUID_as_byte;

    /**
     * Constructor of the SaveCommentToCassandra.
     * @param newComment
     * @throws PoolExhaustedException
     * @throws Exception
     */
    public SaveCommentToCassandra(Comment newComment) throws PoolExhaustedException, Exception {
        commentToSave = newComment;
        UUID_as_byte = EasyUUIDget.getByteUUID();
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            insertInCommentByPostDB(connector);
            updateUser(connector);
            insertToCreatedNotIndexed(connector);

        } finally {
            connector.release();
        }

    }

    /**
     * Insert all informations into database.
     * Key is : commentToSave.relatedPostId
     * SuperColumn is a TimeUUID generated
     * @throws PoolExhaustedException
     * @throws Exception
     */
    private void insertInCommentByPostDB(FallingDownConnector connector) throws PoolExhaustedException, Exception {

        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_AUTHOR, commentToSave.author,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_TEXT, commentToSave.text,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_NBVOTES, commentToSave.nbVotes,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_TIMESTAMP, Long.toString(commentToSave.creation_timestamp),
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_TITLE, commentToSave.title,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId,
                UUID_as_byte, CommentFields.DB_SCORE, commentToSave.score,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);
        connector.insertInfoSuperColumnWithKey(commentToSave.relatedPostId, UUID_as_byte,
                StringUtils.bytes(CommentFields.UUID), UUID_as_byte,
                FallingDownConnector.DB_COMMENTS_BY_POSTS);

    }

    /**
     * Add one to the nbComment field.
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */
    private void updateUser(FallingDownConnector connector) throws
            PoolExhaustedException, TException, Exception {
        String number = StringUtils.string(connector.getInfoColumnWithkey(commentToSave.author,
                "nbComments", FallingDownConnector.DB_USERS));
        connector.insertInfoColumnWithkey(commentToSave.author, "nbComments",
                Integer.toString(Integer.parseInt(number) + 1), FallingDownConnector.DB_USERS);
    }

    private void insertToCreatedNotIndexed(FallingDownConnector connector) throws
            InvalidRequestException, NotFoundException, UnavailableException, Exception {
        connector.insertInfoColumnWithkey("Comments", UUID_as_byte,
                commentToSave.relatedPostId, FallingDownConnector.DB_INDEX_CREATED);
    }
}
