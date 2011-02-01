package FallingDown.request.topbydate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.xml.HashToXML;
import me.FallingDownLib.CommonClasses.xml.SendToBrowser;
import me.FallingDownLib.functions.bestbydate.RetrieveBestByDate;

/**
 *
 * @author victork
 */
public class BestIn1Month extends HttpServlet {

    private static String PARAMETER_CATEGORY = "category";
    private static String PARAMETER_SUB_CATEGORY = "sub_category";
    private static String PARAMETER_PAGE_NUMBER="page_number";
    
    private static final int NB_ENTRIES_TO_RETRIEVE = 10;

    private int offset;
    private String asked_category;
    private String asked_sub_category;

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
        getParameters(request);
        RetrieveBestByDate bestByDate = new RetrieveBestByDate(FallingDownConnector.KEY_POSTS_1MONTH, offset, 10,
                asked_category, asked_sub_category);

        try {
            HashMap<String, HashMap<String, String>> map = BestInPeriod.getResultFromDatabase(bestByDate.getKeys(), FallingDownConnector.KEY_POSTS_24H, asked_category, asked_sub_category);
            HashToXML convertor = new HashToXML();
            convertor.PostHashMapToXML(map);
            SendToBrowser.sendXMLToBrowser(convertor.getString(), response);

        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.print(e.toString());
        }

    }

    private void getParameters(HttpServletRequest request) {
        int page_number=0;
        try {
            page_number = Integer.parseInt(request.getParameter(PARAMETER_PAGE_NUMBER));
        } catch (NumberFormatException ex) {
            page_number = 0;
        }
        offset=page_number*NB_ENTRIES_TO_RETRIEVE;
        asked_category = request.getParameter(PARAMETER_CATEGORY);
        asked_sub_category = request.getParameter(PARAMETER_SUB_CATEGORY);
    }
}
