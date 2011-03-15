package me.FallingDownLib.interfaces.search;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * standard interface for any search made
 * @author victork
 */
public interface VoxNucleusSearch {

    public void launchSearch();
    public void setQuery(String query);
    public QueryResponse getResponse();
}
