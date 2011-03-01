package FallingDown.search.www;

import java.util.HashMap;
import me.FallingDownLib.CommonClasses.Comment;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.solr.common.SolrDocument;
import org.apache.thrift.TException;

/**
 * This class is the representation of a SolRDocument after it is processed.
 * It takes into account informations like the "doctype" and returns a formatted HTML response.
 * @author victork
 */
public class Answer {

    private static final int DOCTYPE_POST=0;
    private static final int DOCTYPE_COMMENT=1;
    private static final int DOCTYPE_USER=2;

    private SolrDocument sDoc;
    private int doctype=4;
    private StringBuffer temp;

    public Answer(SolrDocument doc){
        sDoc = doc;
        choseDoctype();
        temp = new StringBuffer("<div class=\"hit\">");
    }


    /**
     * Analyze and set doctype
     */
    private void choseDoctype() {

        if (sDoc.get("doctype").equals("post")) {
            doctype = DOCTYPE_POST;
        } else {
            if (sDoc.get("doctype").equals("comment")) {
                doctype = DOCTYPE_COMMENT;
            } else {
                doctype = DOCTYPE_USER;
            }
        }

    }

    /**
     * 
     * @return HTML code for a Post result
     * @throws PoolExhaustedException
     * @throws PostDoesNotExist
     * @throws TException
     * @throws Exception
     */
    private String addPostElement() throws PoolExhaustedException,
            PostDoesNotExist, TException, Exception {
        PostHash pHash = new PostHash(Post.getPostFromDatabase((String) sDoc.get(PostFields.INDEX_KEY)));
        temp.append(SiteElements.insertImage("post/"+pHash.getKey() + "/" + pHash.getFilename(), pHash.getKey()));
        temp.append("<div class=\"marker marker_post\"> Noyau </div>");
        temp.append("<a href=\"/post/").append(pHash.getKey()).append("\">").append(pHash.getTitle()).append("</a><br>");
        temp.append("<div class=\"description\"><em>").append(pHash.getDescription()).append("</em> </div>");
        temp.append(SiteElements.getVoteZone(Integer.parseInt(pHash.getNbVotes()),
                null, false));
        return temp.append("</div>").toString();
    }

    /**
     *
     * @return HTML codee for a User result
     * @throws PoolExhaustedException
     * @throws PostDoesNotExist
     * @throws TException
     * @throws Exception
     */

    private String addUserElement() throws PoolExhaustedException, PostDoesNotExist, TException, Exception{
        UserHash uHash = new UserHash(User.getUserFromDatabase((String)sDoc.get(UserFields.INDEX_USERNAME)));
        temp.append("<div  class=\"marker marker_user\"> Utilisateur </div>");
        temp.append("<a href=\"/user/").append(uHash.getUsername()).append("\">").append(uHash.getUsername()).append("</a>");
        return temp.append("</div>").toString();
    }

    /**
     *
     * @return HTML code for a comment result
     * @throws PoolExhaustedException
     * @throws PostDoesNotExist
     * @throws TException
     * @throws Exception
     */

    private String addCommentElement() throws PoolExhaustedException, PostDoesNotExist, TException, Exception{
        PostHash pHash = new PostHash(Post.getPostFromDatabase((String)sDoc.get(CommentFields.INDEX_RELATED_POSTID)));
        EasyUUIDget.asByteArray(java.util.UUID.fromString((String)sDoc.get(CommentFields.UUID)));
        HashMap<String,String> tempHash = Comment.getCommentFromDatabase(
                (String)sDoc.get(CommentFields.INDEX_RELATED_POSTID),
                EasyUUIDget.asByteArray(java.util.UUID.fromString(
                (String)sDoc.get(CommentFields.UUID))));
        CommentHash cHash= new CommentHash(tempHash);
        temp.append("<div class=\"marker comment_marker\"> Commentaire </div>");
        temp.append("<em>").append(cHash.getText()).append("</em> <br>");
        temp.append("Lien vers le post : <a href=\"/post/").append(pHash.getKey()).append("\">").append(pHash.getTitle()).append("</a>");
        return temp.append("</div>").toString();
    }


    /**
     *
     * @return return the whole HTML code for a result
     * @throws PoolExhaustedException
     * @throws PostDoesNotExist
     * @throws TException
     * @throws Exception
     */

    public String toHTML() throws PoolExhaustedException, PostDoesNotExist, TException, Exception{
        switch(doctype){
            case DOCTYPE_POST:
                return addPostElement();
            case DOCTYPE_USER:
                return addUserElement();
            case DOCTYPE_COMMENT:
                return addCommentElement();
            default:
                return "ERROR";
        }
    }

}
