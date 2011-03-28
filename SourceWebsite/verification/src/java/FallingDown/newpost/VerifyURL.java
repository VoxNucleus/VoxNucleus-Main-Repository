package FallingDown.newpost;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.www.Browser;

/**
 *
 * @author victork
 */
public class VerifyURL extends HttpServlet {
    private String JsonString;
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
        JsonString = getJSONArg(request, response);
        Gson gson = new Gson();
        PostToVerify verify = (PostToVerify) gson.fromJson(JsonString, PostToVerify.class);
        if (!verify.getURL().isEmpty()) {
            URLVerificator uVerificator = URLVerificator.getInstance(verify.getURL());
            PostToVerifyAnswer pAnswer = new PostToVerifyAnswer();
            pAnswer.setResponse(uVerificator.getList());
            Browser.sendResponseToBrowser(request, response, gson.toJson(pAnswer));
        }
    }

    private String getJSONArg(HttpServletRequest request, HttpServletResponse response){
        HashMap<String,String[]> map = new HashMap<String,String[]>(request.getParameterMap());
        String fields=request.getParameter("fields");
        return (fields==null) ? "" : fields;
    }

}
