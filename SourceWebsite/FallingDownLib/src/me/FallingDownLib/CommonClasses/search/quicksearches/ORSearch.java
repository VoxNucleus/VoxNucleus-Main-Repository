package me.FallingDownLib.CommonClasses.search.quicksearches;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CommonClasses.search.ConnectionToSolR;
import me.FallingDownLib.interfaces.search.VoxNucleusSearch;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author victork
 */
public class ORSearch implements VoxNucleusSearch {
    private SolrQuery sQuery;
    private QueryResponse query_Response;
    private HashMap<String,ArrayList<String>> toSearch;
    private int start;


    /**
     *
     * @param toSearch HashMap of first the field that must be searched and then a list of arguments
     */

    public ORSearch(HashMap<String, ArrayList<String>> toSearch) {
        this.toSearch = toSearch;
        start=0;
    }

    /**
     *
     * @param toSearch
     * @return The query
     */

    private String buildQuery() {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = toSearch.keySet().iterator();
        boolean isFieldfirst = true;
        while (iterator.hasNext()) {
            String field = iterator.next();
            if (isFieldfirst) {
                builder.append(field).append(":(");
                isFieldfirst = false;
            } else {
                builder.append(" OR ").append(field).append(":(");
            }
            boolean isTermFirst = true;
            for (int index = 0; index < toSearch.get(field).size(); index++) {
                if (isTermFirst) {
                    builder.append(toSearch.get(field).get(index));
                    isTermFirst=false;
                } else {
                    builder.append(" OR ").append(toSearch.get(field).get(index));
                }
            }
            builder.append(") ");
        }
        return builder.toString();
    }

    /**
     * If executed the query is sent to the server
     */

    public void launchSearch() {
        setQuery(buildQuery());
        try {
            SolrServer server = ConnectionToSolR.openConnectionToSolR();
            query_Response = server.query( sQuery );
        } catch (MalformedURLException ex) {
            Logger.getLogger(ORSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SolrServerException ex) {
            Logger.getLogger(ORSearch.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * Is it really necessary
     * @param query
     */

    public void setQuery(String query) {
        sQuery = new SolrQuery();
        sQuery.setQuery(query);
        sQuery.setStart(start);
    }
    /**
     * Set the start
     * @param start
     */

    public void setStart(int begin){
        start=begin;
    }

    /**
     * return response
     * @return a QueryResponse containing a list of answers
     */

    public QueryResponse getResponse() {
        launchSearch();
        return query_Response;
    }

}
