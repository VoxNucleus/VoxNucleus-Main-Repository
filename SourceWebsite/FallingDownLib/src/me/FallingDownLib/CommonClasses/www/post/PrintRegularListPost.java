package me.FallingDownLib.CommonClasses.www.post;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class PrintRegularListPost implements ToCodeConverter {

    StringBuilder listBuilder;

    /**
     * Default constructor Cannot be accessed from the outside.
     */

    protected PrintRegularListPost(){
        listBuilder = new StringBuilder();
        listBuilder.append("<div id=\"posts\">").append("<div id=\"listposts\"> </div>");
    }

    /**
     * 
     * @param listPost : List of post ids
     */

    public PrintRegularListPost(ArrayList<String> listPost){
        this();
        insertPosts(listPost);
        listBuilder.append("</div>");
    }
    
    
    private void insertPosts(ArrayList<String> listPost){
        for(int index=0;index < listPost.size();index++){
            try {
                PostHash pHash = new PostHash(Post.getPostFromDatabase(listPost.get(index)));
                PrintPost builtPost = new PrintPost(pHash);
                listBuilder.append(builtPost.getHTMLCode());
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(PrintRegularListPost.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PostDoesNotExist ex) {
                Logger.getLogger(PrintRegularListPost.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                Logger.getLogger(PrintRegularListPost.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PrintRegularListPost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getHTMLCode() {
        return listBuilder.toString();
    }

}
