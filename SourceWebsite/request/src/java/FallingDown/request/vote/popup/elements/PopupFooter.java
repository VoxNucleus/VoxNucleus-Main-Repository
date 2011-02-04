package FallingDown.request.vote.popup.elements;

import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PopupFooter implements ToCodeConverter {

    private Pass uPass;
    private StringBuilder pop_footer_build;

    protected PopupFooter(Pass pass){
        pop_footer_build = new StringBuilder();
        uPass=pass;
    }

    /**
     *
     * @param pass
     * @return instance of the code
     */

    public static PopupFooter getInstance(Pass pass){
        return new PopupFooter(pass);
    }

    public String getHTMLCode() {
        buildFooter();
        return pop_footer_build.toString();
    }

    private void buildFooter() {
        pop_footer_build.append("<ul id=\"popup_footer\">");
        if (uPass.getIsAuthentified()) {
            pop_footer_build.append("<li>");
            pop_footer_build.append(buildLink("Connecté sous " +uPass.getUsername(),"/user/"+uPass.getUsername())).append("</li>");
        }else{
            pop_footer_build.append("<li>")
                    .append(buildLink("Non connecté","/usermanagement/subscribe.jsp"))
                    .append("</li>");
        }
        pop_footer_build.append("<li>").append(buildLink("Page Principale","/")).append("</li>");
        pop_footer_build.append("<li>").append(buildLink("A propos","/about/")).append("</li>");
        pop_footer_build.append("</ul>");
    }

    /**
     * 
     * @param text
     * @param target
     * @return construct link with new
     */

    private String buildLink(String text,String target){
        return "<a href=\""+target+"\" target=\"blank\">"+text+"</a>";
    }

}
