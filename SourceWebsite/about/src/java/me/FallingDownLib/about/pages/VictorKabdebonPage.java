package me.FallingDownLib.about.pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.about.AboutPage;
import me.FallingDownLib.about.pages.www.PrintVictorKabdebon;

/**
 *
 * @author victork
 */
public class VictorKabdebonPage extends HttpServlet {
  

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
        AboutPage indexAbout = AboutPage.getInstance();
        indexAbout.setPageTeam(true);
        Metadata index_meta = Metadata.getInstance();
        index_meta.setAdditionnalKeywords(" victor kabdebon, webmaster, à propos ");
        indexAbout.setTitle("Victor Kabdebon");
        indexAbout.setIn_Page_Title("VoxNucleus : Victor Kabdebon");
        indexAbout.setMetadata(index_meta);
        PrintVictorKabdebon victorKPrint = PrintVictorKabdebon.getInstance();
        indexAbout.setBodyContent(victorKPrint);
        Browser.sendResponseToBrowser(request, response, indexAbout);
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

}
