package FallingDown.viewUser;

import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CassandraConnection.connectors.LastPostsByUserConnector;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.www.post.PrintRegularListPost;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;

/**
 *
 * @author victork
 */
public class UserRightPanel implements ToCodeConverter{
    
    private StringBuilder rightPanelBuilder;
    private UserHash uHash;
    
    protected UserRightPanel(){
        rightPanelBuilder = new StringBuilder();
    }

    /**
     * 
     * @param uHash
     */
    protected UserRightPanel(UserHash uHash){
        this();
        this.uHash= uHash;
    }

    /**
     * 
     * @param uHash
     * @return
     */
    public static UserRightPanel getRightPanel(UserHash uHash){
        return new UserRightPanel(uHash);
    }

    public String getHTMLCode() {
        buildRightPanel();
        return rightPanelBuilder.toString();
    }

    private void buildRightPanel() {
        rightPanelBuilder.append("<li class=\"right_panel\">");
        buildUserCategories();
        insertPosts();
        rightPanelBuilder.append("</li>");
    }

    private void buildUserCategories() {
        rightPanelBuilder.append("<div class=\"user_categories\">");
        rightPanelBuilder.append("<ul>");

        rightPanelBuilder.append("<li class=\"selected\">");
        rightPanelBuilder.append("Noyaux créés");
        rightPanelBuilder.append("</li>");

        rightPanelBuilder.append("<li>");
        rightPanelBuilder.append("Noyaux commentés");
        rightPanelBuilder.append("</li>");

        rightPanelBuilder.append("<li>");
        rightPanelBuilder.append("Derniers votes");
        rightPanelBuilder.append("</li>");

        rightPanelBuilder.append("</ul>");
        rightPanelBuilder.append("</div>");
    }

    private void insertPosts() {
        LastPostsByUserConnector lastConnector = LastPostsByUserConnector.getInstance(uHash.getUsername(), 0, 10);
        List<Column> listCol = lastConnector.getGoodNumberOfColumns();
        ArrayList<String> listPostId= new ArrayList<String>();
        for(int index=0;index<listCol.size();index++){
            listPostId.add(StringUtils.string(listCol.get(index).getValue()));
        }

        PrintRegularListPost listPost = new PrintRegularListPost(listPostId);
        rightPanelBuilder.append(listPost.getHTMLCode());
    }


}
