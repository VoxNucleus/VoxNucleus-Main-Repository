package me.FallingDownLib.CommonClasses.www;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.FallingDownLib.interfaces.www.ToHTML2Converter;

/**
 *
 * @author victork
 */
public class Browser {

    public static void sendHTMLToBrowser2(HttpServletRequest request,
            HttpServletResponse response, ToHTML2Converter htmlConverter2) throws IOException{
        PrintWriter out = response.getWriter();
        try {
            out.print(htmlConverter2.toHTML_GetHead());
            out.print(htmlConverter2.toHTML_GetBody());
        } finally {
            out.close();
        }
    }


    /**
     * Send HTML to the browser but 
     * Using HTML as String
     * @param request
     * @param response
     * @param resultToSend
     * @throws IOException
     */

    public static void sendResponseToBrowser(HttpServletRequest request,
            HttpServletResponse response, String resultToSend) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(resultToSend);
        } finally {
            out.close();
        }
    }

    /**
     * Send HTML to the code but 
     * Using ToCodeConverter interface
     * @param request
     * @param response
     * @param codeConverter
     * @throws IOException
     */

    public static void sendResponseToBrowser(HttpServletRequest request,
            HttpServletResponse response, ToCodeConverter codeConverter) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(codeConverter.getHTMLCode());
        } finally {
            out.close();
        }
    }

    public static void sendJSONToBrowser(HttpServletRequest request,
            HttpServletResponse response, ToCodeConverter codeConverter) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(codeConverter.getHTMLCode());
        } finally {
            out.close();
        }
    }


    public static void sendJSONToBrowser(HttpServletRequest request,
            HttpServletResponse response, String resultToSend) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(resultToSend);
        } finally {
            out.close();
        }
    }
    /**
     * Send the JSONP with the right content type
     * @param request
     * @param response
     * @param messageToSend
     * @throws IOException
     */

    public static void sendJSONPToBrowser(HttpServletRequest request,
            HttpServletResponse response,String messageToSend) throws IOException {
        response.setContentType("text/javascript;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print(messageToSend);
        } finally {
            out.close();
        }
    }

}
