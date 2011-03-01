package FallingDown.search;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author victork
 */
public class DoSearch {
    private SolrServer server;
    private String textQuery;
    private SolrDocumentList docs;
    Integer q_start;
    private SearchOptions options;


    

    /**
     *
     * @param server
     * @param query
     * @throws SolrServerException
     */
    public DoSearch(SolrServer server, String query) throws SolrServerException{
        this.server=server;
        this.textQuery=query;
        q_start =Integer.valueOf(0);
        
    }

    private void searchOptionsToParameters(){
        q_start= (options.getPageNumber()-1)*10;
    }

    /**
     *
     */
    private void buildQuery() {
        textQuery.replaceAll(" ", "+");
    }

    /**
     *
     * @throws SolrServerException
     */
    public void launchSearch() throws SolrServerException {
        searchOptionsToParameters();
        SolrQuery query = new SolrQuery();
        query.setQuery("p_description:" + textQuery + " OR p_title:" + textQuery + " OR c_text:" + textQuery + " OR c_title:" + textQuery);
        query.setStart(q_start);
        QueryResponse rsp = server.query(query);
        docs = rsp.getResults();
    }

    /**
     * 
     * @param search_options
     */
    public void attachSearchOptions(SearchOptions search_options){
        options=search_options;
    }

    /**
     *
     * @return
     */
    public SolrDocumentList getResults() {
        return docs;
    }
}
