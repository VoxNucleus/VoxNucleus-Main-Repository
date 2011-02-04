package FallingDown.request.comment;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.xml.HashToXML;
import me.FallingDownLib.CommonClasses.xml.SendToBrowser;

/**
 *
 * @author victork
 */
public class GetCommentByDate extends HttpServlet {

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

        String id= request.getParameter("postId");
        int beginning = Integer.parseInt(request.getParameter("begin"));
        int end= Integer.parseInt(request.getParameter("end"));

        CommentFetcherByDate comments = new CommentFetcherByDate(id,beginning,end);
        try{
            HashToXML xmlconvertor = new HashToXML();
            xmlconvertor.CommentHashMapToXML(comments.getHashMapComments());
            sendXMLToBrowser(xmlconvertor.getString(),response);
        }catch(Exception e){
            //TODO finer result
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
        String id= request.getParameter("postId");
        int beginning = Integer.parseInt(request.getParameter("begin"));
        int end= Integer.parseInt(request.getParameter("end"));
        CommentFetcherByDate comments = new CommentFetcherByDate(id,beginning,end);
        try{
            HashToXML xmlconvertor = new HashToXML();
            xmlconvertor.CommentHashMapToXML(comments.getHashMapComments());
            SendToBrowser.sendXMLToBrowser(xmlconvertor.getString(),response);
        }catch(Exception e){
            //TODO finer result
        }
    }

    protected void sendXMLToBrowser(String resultat, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        out.println(resultat);
    }

}
