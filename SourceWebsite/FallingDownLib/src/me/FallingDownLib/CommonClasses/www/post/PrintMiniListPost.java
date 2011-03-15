package me.FallingDownLib.CommonClasses.www.post;

import java.util.ArrayList;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.thrift.TException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victork
 */
public class PrintMiniListPost implements ToCodeConverter {

    StringBuilder mini_List_Builder;
    ArrayList<String> listPost;

    private PrintMiniListPost(){
        mini_List_Builder = new StringBuilder();
    }

    private PrintMiniListPost(ArrayList<String> list){
        this();
        listPost=list;
    }

    public static PrintMiniListPost getInstance(ArrayList<String> list){
        return (new PrintMiniListPost(list));
    }

    /**
     * Build the list
     */
    private void buildMiniList(){
        mini_List_Builder.append("<div id=\"best_mini_list\" >");
        getMiniPosts();
        mini_List_Builder.append("</div>");
    }


    /**
     *
     * @return HTML code for mini list
     */
    public String getHTMLCode() {
        buildMiniList();
        return mini_List_Builder.toString();
    }

    /**
     * Print post one by one
     */

    private void getMiniPosts() {
        for(int index=0;index < listPost.size();index++){
            try {
                PostHash pHash = new PostHash(Post.getPostFromDatabase(listPost.get(index)));
                PrintPostMini builtPost = PrintPostMini.getInstance(pHash.getKey(), pHash.getTitle(),pHash.getNbVotes(),pHash.getFilename());
                mini_List_Builder.append(builtPost.getHTMLCode());
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
}
