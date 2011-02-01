/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.request.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.xml.HashToXML;
import me.FallingDownLib.CommonClasses.xml.SendToBrowser;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class getPostByUser extends HttpServlet {
   

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

        String id= request.getParameter("userId");
        int beginning = Integer.parseInt(request.getParameter("begin"));
        int end= Integer.parseInt(request.getParameter("end"));
        PostByUserFetcher posts= new PostByUserFetcher(id,beginning,end);
        try {
            HashMap<String, HashMap<String,String>>map =posts.getResultFromDatabase();
            HashToXML convertor = new HashToXML();
            convertor.PostHashMapToXML(map);
            SendToBrowser.sendXMLToBrowser(convertor.getString(),response);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(getPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(getPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(getPostByUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

}
