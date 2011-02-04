package FallingDown.request.interestingpost;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.xml.SendToBrowser;

/**
 *
 * @author victork
 */
public class InterestingEntries extends HttpServlet {

    private static String PARAMETER_CATEGORY="category";
    private static String PARAMETER_SUB_CATEGORY="sub_category";
    private static String PARAMETER_PAGE_NUMBER="page_number";

    private static final int NB_ENTRIES_TO_RETRIEVE = 10;
    private int offset;
    private String asked_category;
    private String asked_sub_category;
   

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
        getParameters(request);
        String result = null;
        try {
            GetInterestingEntries lastEntries = new GetInterestingEntries(offset, offset + NB_ENTRIES_TO_RETRIEVE,
                    asked_category, asked_sub_category);
            result = lastEntries.convertPostsToXML();
                    SendToBrowser.sendXMLToBrowser(result,response);
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.println(e.toString());
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

    protected void sendXMLToBrowser(String resultat, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(resultat);
    }

}
