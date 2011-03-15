package me.FallingDownLib.functions.similarpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.search.quicksearches.ORSearch;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class RetrieveSimilarPosts {

    private String postId;
    private int max_numberToFind;

    private ArrayList<String> result;

    private RetrieveSimilarPosts(){
         result = new ArrayList<String>();
    }


    private RetrieveSimilarPosts(String inPost, int maxNumber){
        this();
        postId=inPost;
        max_numberToFind=maxNumber;
       
    }


    public static RetrieveSimilarPosts getInstance(String postId,int maxNumber){
        return (new RetrieveSimilarPosts(postId,maxNumber));
    }

    public ArrayList<String> getKeys(){
        launchSearch();
        return result;

    }

    private void launchSearch() {
        try {
            PostHash pHash = new PostHash(Post.getPostFromDatabase(postId));
            String[] tagList =pHash.getTags().split(",", 10);
            HashMap<String,ArrayList<String>> hash= constructTagHashFromArray(tagList);
            ORSearch orSearch = new ORSearch(hash);
            orSearch.setStart(1);
            QueryResponse response = orSearch.getResponse();
            SimilarPostsAnalyser analyser = SimilarPostsAnalyser.getInstance(response,max_numberToFind);
            result = analyser.getKeys();
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(RetrieveSimilarPosts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PostDoesNotExist ex) {
            
        } catch (TException ex) {
            Logger.getLogger(RetrieveSimilarPosts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RetrieveSimilarPosts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Construct a HashMap of tags 
     * @param tagList
     * @return HashMap ready for searching
     */

    private HashMap<String,ArrayList<String>> constructTagHashFromArray(String[] tagList) {
        ArrayList<String> toInsert = new ArrayList<String>();
        for(int array_index=0;array_index<tagList.length;array_index++){
            if(tagList[array_index].length()>2){
                toInsert.add(tagList[array_index]);
            }
        }
        HashMap<String, ArrayList<String>> hashResult = new HashMap<String, ArrayList<String>>();
        if (toInsert.size() > 0) {
            hashResult.put(PostFields.INDEX_TAGS, toInsert);
        }
        return hashResult;
    }

}
