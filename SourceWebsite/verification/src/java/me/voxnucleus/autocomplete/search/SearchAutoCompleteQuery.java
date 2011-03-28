package me.voxnucleus.autocomplete.search;

import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CommonClasses.PostFields;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author victork
 */
public class SearchAutoCompleteQuery {

    private static final int NUMBER_MAX_SUGGESTIONS=6;
    //The words will be search in this 
    private static final String LOOKUP_FIELD=PostFields.INDEX_DESCRIPTION;
    private static final String CASE_INSENSITIVE="case_insensitive";
    private static final String SORT_TYPE="count"; //Set to count to have the most
            //popular results first, can be set "index"

    private SolrQuery query;
    private SolrServer solr_server;
    private String query_string;
    ArrayList<String> list_auto_complete_terms;
    private boolean hasResult=false;

    /**
     * 
     */
    protected SearchAutoCompleteQuery(){
        query = new SolrQuery();
    }

    /**
     *
     * @param server
     */
    protected SearchAutoCompleteQuery(SolrServer server){
        this();
        solr_server=server;
    }

    /**
     *
     * @param server
     * @param q
     */
    private SearchAutoCompleteQuery(SolrServer server, String q) {
        this(server);
        query_string=q;
    }

    /**
     *
     * @param server
     * @return instance of this query
     */

    public static SearchAutoCompleteQuery getInstance(SolrServer server){
        return new SearchAutoCompleteQuery(server);
    }
    /**
     * 
     * @param server
     * @param q
     * @return instance of this query
     */
    public static SearchAutoCompleteQuery getInstance(SolrServer server, String q){
        return new SearchAutoCompleteQuery(server,q);
    }

    /**
     *
     * @param terms
     * @return
     */

    public void launchQuery() throws SolrServerException{
        buildQuery();
        QueryResponse rsp=solr_server.query(query);
        NamedListToListResult(rsp);
        
        
    }

    /**
     * Builds the query
     */
    private void buildQuery(){
        query.setQueryType("/terms");
        query.set("terms", true);
        query.set("terms.fl", PostFields.INDEX_DESCRIPTION);
        query.set("terms.prefix", query_string);
        query.set("terms.limit", Integer.toString(NUMBER_MAX_SUGGESTIONS));
    }

    /**
     * 
     * @param qr
     * @return
     */
    private List<String> NamedListToListResult(QueryResponse qr){
        list_auto_complete_terms = new ArrayList<String>(NUMBER_MAX_SUGGESTIONS);

        qr.getSortValues();
        NamedList<Object> termsInfo=qr.getResponse();
        // we want the field "terms"
        NamedList<Object> list_by_term_fl=(NamedList<Object>) termsInfo.get("terms");
        //Then chose the field we look into
        NamedList<Object> list_terms=(NamedList<Object>) list_by_term_fl.get(LOOKUP_FIELD);

        for(int j=0; j <list_terms.size();j++){
            
            list_auto_complete_terms.add(list_terms.getName(j));
        }
        hasResult=!list_auto_complete_terms.isEmpty();
        return list_auto_complete_terms;
    }

    public boolean hasResult(){
        return hasResult;
    }

    public ArrayList<String> getResult(){
        return list_auto_complete_terms;
    }

}
