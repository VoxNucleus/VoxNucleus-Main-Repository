package FallingDown.request.relatedPosts;

import java.util.ArrayList;
import me.FallingDownLib.CommonClasses.PostFields;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author victork
 */
public class RelatedPostAnalyser {

    QueryResponse response;

    public RelatedPostAnalyser(QueryResponse response){
        this.response=response;
    }



    /**
     *
     * @return list of post ids
     */

    public ArrayList<String> getListId(){
        ArrayList<String> result = new ArrayList<String>();
        SolrDocumentList listResponses = response.getResults();
        for(int index=0;index<listResponses.size();index++){
            result.add((String)listResponses.get(index).get(PostFields.INDEX_KEY));
        }
        return result;
    }

}
