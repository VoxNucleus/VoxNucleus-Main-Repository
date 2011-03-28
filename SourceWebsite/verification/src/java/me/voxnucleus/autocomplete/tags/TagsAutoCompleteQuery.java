package me.voxnucleus.autocomplete.tags;

import me.FallingDownLib.CommonClasses.PostFields;
import me.voxnucleus.autocomplete.AutoCompleteQuery;
import org.apache.solr.client.solrj.SolrServer;

/**
 * Extended class that sets all the needed results
 * @author victork
 */
public class TagsAutoCompleteQuery extends AutoCompleteQuery {


    /**
     * 
     * @param server
     * @param query_s
     */
    protected TagsAutoCompleteQuery(SolrServer server,String query_s){
        super(server);
        super.set_lookup_term(PostFields.INDEX_TAGS);
        super.set_max_number_suggestion(5);
        super.set_query_string(query_s);
        
        
    }

    /**
     * 
     * @param server
     * @param query_s
     * @return
     */
    public static TagsAutoCompleteQuery getInstance(SolrServer server,String query_s){
        return new TagsAutoCompleteQuery(server,query_s);
    }

}
