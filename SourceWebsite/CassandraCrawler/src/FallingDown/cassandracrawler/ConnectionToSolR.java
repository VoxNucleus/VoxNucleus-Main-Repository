/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.cassandracrawler;

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

/**
 *
 * @author victork
 */
public class ConnectionToSolR {

    public static final String URL_MAIN_SERVER_SOLR="http://localhost:8983/solr";
    private static SolrServer server=null;

    /**Getting an instance of a SolrServer.
     * TODO : Verify that the syntax is good and that it follows recommandation at :
     * http://wiki.apache.org/solr/Solrj
     * @return Instance of the SolrServer
     */

    public static SolrServer openConnectionToSolR() throws MalformedURLException{
        if(server==null){
            server = new CommonsHttpSolrServer( URL_MAIN_SERVER_SOLR );
        }     
        return server;

    }

    public ConnectionToSolR(){

    }

}
