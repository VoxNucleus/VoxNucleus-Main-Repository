package FallingDown.widget.miniwidget;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.www.Browser;

/**
 *
 * @author victork
 */
public class getInformation extends HttpServlet {

    private String JSONPMessage="";

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
        WidgetQuery w_query = WidgetQuery.getInstance(getQueryFromRequest(request));
        if (w_query.isValidQuery()) {
            try {
                Gson gson = new Gson();
                JSONtoJSONP(gson.toJson(w_query.retrieveResponse()));
                Browser.sendJSONPToBrowser(request, response, JSONPMessage);
            } catch (Exception ex) {
                Logger.getLogger(WidgetQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * @param JSON
     */
    private void JSONtoJSONP(String JSON){
        StringBuilder jsonp_builder = new StringBuilder("displayButtons(");
        jsonp_builder.append(JSON).append(");");
        JSONPMessage=jsonp_builder.toString();
    }

    /**
     *
     * @param request
     * @return HashMap containaings informations
     */

    private HashMap<String,String[]> getQueryFromRequest(HttpServletRequest request){
        return new HashMap<String,String[]>(request.getParameterMap());
    }


}
