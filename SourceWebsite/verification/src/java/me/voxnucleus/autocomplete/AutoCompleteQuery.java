package me.voxnucleus.autocomplete;

import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author victork
 */
public class AutoCompleteQuery {

    private String lookup_terms;
    private String sort_by="count";
    private int max_number_suggestions;
    private String query_string;

    private SolrQuery query;
    private SolrServer solr_server;
    ArrayList<String> list_auto_complete_terms;
    private boolean hasResult=false;

    /**
     * 
     */
    protected AutoCompleteQuery(){
        query = new SolrQuery();
    }
    /**
     *
     * @param server
     */
    protected AutoCompleteQuery(SolrServer server){
        this();
        solr_server=server;

    }
    /**
     *
     * @param server
     * @return
     */

    public AutoCompleteQuery getInstance(SolrServer server){
        return new AutoCompleteQuery(server);
    }

    /**
     * 
     * @return
     */
    public boolean hasResult(){
        return hasResult;
    }

    /**
     * 
     * @return
     */

    public ArrayList<String> getResult(){
        return list_auto_complete_terms;
    }

    /**
     *
     * @return
     */

    public String[] get_result_as_array(){
        String[] array_result= list_auto_complete_terms.toArray(new String[list_auto_complete_terms.size()]);
        return array_result;
        
    }

    /**
     *
     * @param terms
     * @return
     */

    public void launch_query() throws SolrServerException{
        build_query();
        QueryResponse rsp=solr_server.query(query);
        NamedList_to_list_result(rsp);
    }

    /**
     * 
     * @param number
     */
    public void set_max_number_suggestion(int number){
        max_number_suggestions=number;
    }

    /**
     * 
     * @param term
     */

    public void set_lookup_term(String term){
        lookup_terms=term;
    }

    /**
     * 
     * @param beginning
     */

    public void set_query_string(String beginning){
        query_string=beginning;
    }

    /**
     * Builds the query
     */
    private void build_query(){
        query.setQueryType("/terms");
        query.set("terms", true);
        query.set("terms.fl", lookup_terms);
        query.set("terms.prefix", query_string);
        query.set("terms.limit", max_number_suggestions);
        query.set("terms.sort",sort_by);
    }

    /**
     *
     * @param qr
     * @return
     */
    private List<String> NamedList_to_list_result(QueryResponse qr){
        list_auto_complete_terms = new ArrayList<String>(max_number_suggestions);
        qr.getSortValues();
        NamedList<Object> termsInfo=qr.getResponse();
        // we want the field "terms"
        NamedList<Object> list_by_term_fl=(NamedList<Object>) termsInfo.get("terms");
        //Then chose the field we look into
        NamedList<Object> list_terms=(NamedList<Object>) list_by_term_fl.get(lookup_terms);
        for(int j=0; j <list_terms.size();j++){

            list_auto_complete_terms.add(list_terms.getName(j));
        }
        hasResult=!list_auto_complete_terms.isEmpty();
        return list_auto_complete_terms;
    }

}
