package me.voxnucleus.autocomplete.tags;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.search.ConnectionToSolR;
import me.FallingDownLib.CommonClasses.www.Browser;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author victork
 */
public class TagsAutoComplete extends HttpServlet {
    public static final String QUERY_PARAM="autocomplete_text";

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        try{
            SolrServer solr_server = ConnectionToSolR.openConnectionToSolR();
            TagsAutoCompleteQuery autocomplete_tags=TagsAutoCompleteQuery.getInstance(solr_server, getParameter(request));
            autocomplete_tags.launch_query();
            TagsAutoCompleteAnswer ans= new TagsAutoCompleteAnswer(autocomplete_tags.get_result_as_array());
            Gson gson = new Gson();
            Browser.sendJSONToBrowser(request, response, gson.toJson(ans));
        }catch(MalformedURLException ex){
            Logger.getLogger(TagsAutoComplete.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SolrServerException ex){
           Logger.getLogger(TagsAutoComplete.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    /**
     *
     * @param request
     * @return an empty string if parameter does not exist or else
     */

    private String getParameter(HttpServletRequest request){
        HashMap<String,String[]> response_map=new HashMap<String,String[]>(request.getParameterMap());
        if(response_map.containsKey(QUERY_PARAM)){
            return (response_map.get(QUERY_PARAM)[0].length()>2)?response_map.get(QUERY_PARAM)[0]:"";
        }else{
            return "";
        }

    }

}
