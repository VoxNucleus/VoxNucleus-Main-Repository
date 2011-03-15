package me.FallingDownLib.CommonClasses;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectCommentException;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.util.ScriptDestroyer;
import me.prettyprint.cassandra.utils.StringUtils;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;


/**
 * Holds informations of a comment
 * @author victork
 */
public class Comment {

    public static final int MAX_TITLE_LENGTH=60;
    public static final int MAX_TEXTAREA_LENGTH=1500;

    // Not implemented at the moment
    byte[] responseToComment;
    
    public String title;
    public String author;
    public String nbVotes;
    public String score;
    public String text;
    public String relatedPostId;
    public long creation_timestamp;

    public Comment(HashMap<String, String[]> map, String author){
        Calendar cal = Calendar.getInstance();
        this.author=author;
        nbVotes =Integer.toString(0);
        ScriptDestroyer textCheck= new ScriptDestroyer(map.get("text")[0]);
        text=textCheck.getPurgedTextBack();
        relatedPostId=map.get("post_id")[0];
        title = map.get("title")[0];
        score="0";
        this.creation_timestamp=cal.getTimeInMillis();
    }

    /**
     * Each data is verified to ensure that nothing is broken.
     * @throws IncorrectCommentException
     */

    public void validateComment() throws IncorrectCommentException{

        if(title.length() > MAX_TITLE_LENGTH){
            throw new IncorrectCommentException("Titre du commentaire trop long");
        }
        if(text.length()>MAX_TEXTAREA_LENGTH){
            throw new IncorrectCommentException("Commentaire trop long. "
                    + "La limite est de 1500 caractères");
        }
        if(text.length()==0)
            throw new IncorrectCommentException("Commentaire vide.");
    }

    /**
     * Construct a HashMap of a comment.
     * Add some other informations such as "relatedPostId"
     * @param relatedPostId
     * @param timeUUID
     * @return HashMap of a comment
     * @throws PoolExhaustedException
     * @throws TException
     * @throws Exception
     */
    public static HashMap<String, String> getCommentFromDatabase(String relatedPostId,
            byte[] timeUUID) throws
            PoolExhaustedException, TException, Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("relatedPostId", relatedPostId);
        FallingDownConnector connector = null;
        try {
            connector = new FallingDownConnector();
            List<Column> list = connector.getSuperColumn(FallingDownConnector.DB_COMMENTS_BY_POSTS,
                    relatedPostId, timeUUID).getColumns();
            for (int indexCol = list.size() - 1; indexCol >= 0; indexCol--) {
                if (StringUtils.string(list.get(indexCol).getName()).equals(CommentFields.UUID)) {
                    map.put(StringUtils.string(list.get(indexCol).getName()),
                            EasyUUIDget.toUUID(list.get(indexCol).getValue()).toString());
                } else {
                    map.put(StringUtils.string(list.get(indexCol).getName()),
                            StringUtils.string(list.get(indexCol).getValue()));
                }
            }
        } finally {
            if (connector != null) {
                connector.release();
            }
        }
        return map;
    }



    /**
     * Method static that can be called from anywhere to know the number of comments
     * @param postId
     * @return Numbers of comments for the post with id postId
     */

    public static int getNumberComments(String postId) {
        FallingDownConnector connector = null;
        int nbCommentaires=0;
        try {
            connector = new FallingDownConnector();
            nbCommentaires = connector.getCount(FallingDownConnector.DB_COMMENTS_BY_POSTS, null, postId);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRequestException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connector != null) {
                connector.release();
            }

        }
        return nbCommentaires;
    }

    public String getRelatedPostId(){
        return relatedPostId;
    }

}
