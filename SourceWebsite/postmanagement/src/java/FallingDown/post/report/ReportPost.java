package FallingDown.post.report;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.PostReportConnector;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostReport;
import me.FallingDownLib.CommonClasses.PostReport;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.post.PostReportFields;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class ReportPost extends HttpServlet {
    private String postId="";
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
        Pass report_pass = Pass.getPass(request);
        report_pass.launchAuthentifiate();
        if (report_pass.getIsAuthentified()) {
            getGETParameters(request);
            if (!postId.isEmpty()) {
                PrintReportPost reportPost = PrintReportPost.getInstance(postId);
                reportPost.setReportType(PrintReportPost.REPORT_NORMAL);
                Browser.sendResponseToBrowser(request, response, reportPost.getHTMLCode());
            }
        }else{
            PrintReportPost reportPost = PrintReportPost.getInstance(postId);
            Browser.sendResponseToBrowser(request, response, reportPost.getHTMLCode());
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
        Pass report_pass = Pass.getPass(request);
        report_pass.launchAuthentifiate();
        if(report_pass.getIsAuthentified()){
            PostReport pReport =getPOSTParameters(request);
            try {
                pReport.validate();
                PostReportConnector report_connector = PostReportConnector
                        .getInsertInstance(EasyUUIDget.getByteUUID(),
                    pReport.getPostId(), pReport.getReason(), report_pass.getUsername());
                try {
                    report_connector.doSave();
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotFoundException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PoolExhaustedException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidRequestException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnavailableException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TimedOutException ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IncorrectPostReport ex) {
                Logger.getLogger(ReportPost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param request
     */

    private void getGETParameters(HttpServletRequest request){
        postId=request.getParameter("postid");
        if(postId==null)
            postId="";
    }

    /**
     *
     * @param request
     */
    private PostReport getPOSTParameters(HttpServletRequest request){
        String postid = request.getParameter(PostReportFields.HTTP_POSTID);
        String reason = request.getParameter(PostReportFields.HTTP_TEXTAREA);
        return PostReport.getNewReport(postid, reason);
    }


}
