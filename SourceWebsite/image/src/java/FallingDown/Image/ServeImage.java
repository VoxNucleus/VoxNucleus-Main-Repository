package FallingDown.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author victork
 */
public class ServeImage extends HttpServlet {


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
        try {
            ImageOpener imOpener = new ImageOpener(request.getPathInfo(), response);
        } catch (FileNotFoundException e) {
          response.sendRedirect("/image404.jsp");
        } catch (IOException e) {
            printUnknownError(request.getPathInfo(),response);

        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }


    private void printUnknownError(String pathInfo, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            out.println("Erreur athentification");
        } finally {
            out.close();
        }
            
    }

    private void printFileNotFound(String pathInfo, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Posté</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Not Found error</h1>");
            out.println("</body>");
            out.println("</html>");
        }finally {
            out.close();
        }
        
    }
}
