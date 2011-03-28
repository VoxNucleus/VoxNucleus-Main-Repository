package FallingDown.viewUser;

import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author victork
 */
public class UserLeftPanel implements ToCodeConverter {

    private StringBuilder leftPanelBuilder;
    private UserHash uHash;

    protected UserLeftPanel(){
        leftPanelBuilder=new StringBuilder();
    }

    protected UserLeftPanel(UserHash uInfo){
        this();
        uHash=uInfo;
    }

    public static UserLeftPanel getLeftPanel(UserHash uInfo){
        return new UserLeftPanel(uInfo);
    }

    private void buildPanel(){
        leftPanelBuilder.append("<li class=\"left_panel\">");
        buildHead();
        buildInformations();

        leftPanelBuilder.append("</li>");
    }


    public String getHTMLCode() {
        buildPanel();
        return leftPanelBuilder.toString();
    }

    private void buildHead() {
        leftPanelBuilder.append("<div class=\"head\">");
        leftPanelBuilder.append(SiteElements.insertUserAvatar(uHash));
        leftPanelBuilder.append("<h1>").append(uHash.getUsername()).append("</h1>");
        leftPanelBuilder.append("<div id=\"member_since\"><b>Membre depuis le : </b>")
                .append(uHash.getSubscribeDate())
                .append("</div>");
        leftPanelBuilder.append("<div id=\"nucleus_number\"><b>Noyaux postés : </b>")
                .append(uHash.getNbPosts()).append("</div>");
        leftPanelBuilder.append("<div id=\"comment_number\"><b>Commentaires : </b>")
                .append(uHash.getNbComments()).append("</div>");
        leftPanelBuilder.append("</div>");

    }

    private void buildInformations() {
        leftPanelBuilder.append("<div class=\"more_information\">");
        leftPanelBuilder.append(StringEscapeUtils.escapeHtml(uHash.getDescription()));
        leftPanelBuilder.append("</div>");
    }

}
