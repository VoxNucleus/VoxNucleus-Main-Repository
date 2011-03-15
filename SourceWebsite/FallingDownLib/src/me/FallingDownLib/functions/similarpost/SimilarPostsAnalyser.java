package me.FallingDownLib.functions.similarpost;

import java.util.ArrayList;
import java.util.Collections;
import me.FallingDownLib.CommonClasses.PostFields;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author victork
 */
public class SimilarPostsAnalyser {

    private ArrayList<String> listKeys;
    QueryResponse responseToAnalyse;
    private int maxNumberToFind;

    /**
     * Default constructor
     */

    private SimilarPostsAnalyser(){
        listKeys = new ArrayList<String>();
    }
    /**
     * Constructor 
     * @param response
     */

    private SimilarPostsAnalyser(QueryResponse response, int maxNumber){
        this();
        responseToAnalyse=response;
        maxNumberToFind=maxNumber;
    }

    /**
     * Set new QueryResponse to the object. It will replace the other one
     * @param response
     */

    public void setNewQueryResponse(QueryResponse response){
        responseToAnalyse=response;
    }

    /**
     *
     * @param response
     * @return an instance of a SearchSimilarPosts
     */
    public static SimilarPostsAnalyser getInstance(QueryResponse response, int maxNumber){
        return (new SimilarPostsAnalyser(response,maxNumber));
    }

    private void analyseResults(){
        SolrDocumentList list =responseToAnalyse.getResults();
        for(int solrIndex=0;solrIndex<list.size();solrIndex++){
            listKeys.add(0, (String)list.get(solrIndex).get(PostFields.INDEX_KEY));
        }
    }


    /**
     * the resulting list must be reversed !
     * @return set of post ids
     */

    public ArrayList<String> getKeys(){
        analyseResults();
        Collections.reverse(listKeys);
        return listKeys;
    }


}
