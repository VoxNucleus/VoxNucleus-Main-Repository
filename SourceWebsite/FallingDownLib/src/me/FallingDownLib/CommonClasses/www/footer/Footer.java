package me.FallingDownLib.CommonClasses.www.footer;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class Footer implements ToCodeConverter {
    private Pass pass;
    private StringBuilder footer_Builder;
    private HttpServletRequest associated_request=null;

    private Footer(){
        footer_Builder=new StringBuilder();
    }

    public static Footer getInstance(Pass in){
        return new Footer(in);
    }

    public static Footer getInstance(Pass in,HttpServletRequest request){
        return new Footer(in,request);
    }

    private Footer(Pass in) {
        this();

        pass=in;
    }

    private Footer(Pass in,HttpServletRequest request) {
        this();
        associated_request=request;
        pass=in;
    }

    public String getHTMLCode() {
        buildFooter();
        return footer_Builder.toString();
    }

    /**
     * Build the code for the footer
     */
    private void buildFooter() {
        footer_Builder.append("<div id=\"footer\" >");
        ElectronBar bar = ElectronBar.getInstance();
        footer_Builder.append(bar.getHTMLCode());
        footer_Builder.append("<div id=\"user_bar\">");
        if(pass.getIsAuthentified()){
                footer_Builder.append("<a href=\"/\" > Page Principale </a> | <a href=\"/user/")
                        .append(pass.getUsername()).append("\"> Connecté, ")
                        .append(pass.getUsername()).append("</a>");
        }else{
            footer_Builder.append("<a href=\"/\" > Page Principale </a> | "
                    + "<a href=\"/usermanagement/login.jsp");
            try {
                if (associated_request != null) {
                    String url = associated_request.getRequestURI();
                    if (associated_request.getQueryString() != null) {
                        url += "?" + associated_request.getQueryString();
                    }
                    footer_Builder.append("?wherefrom=");
                    footer_Builder.append(java.net.URLEncoder.encode(url, "UTF-8"));
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Footer.class.getName()).log(Level.SEVERE, null, ex);
            }
            footer_Builder.append("\" > Se connecter </a>");
        }
        footer_Builder.append("</div>");
        footer_Builder.append("</div>");
    }
}
