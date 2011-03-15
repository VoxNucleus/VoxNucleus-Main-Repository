package me.FallingDownLib.CommonClasses.xml;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that allows one to send informations to the browser, passed as a String
 * @author victork
 */
public class SendToBrowser {

    public static void sendXMLToBrowser(String resultat, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        try {
            out.println(resultat);
        } finally {
            out.close();
        }
    }

}
