package FallingDown.request.vote.popup;

import FallingDown.request.vote.popup.elements.PopupAlreadyVoted;
import FallingDown.request.vote.popup.elements.PopupFooter;
import FallingDown.request.vote.popup.elements.PopupLogin;
import FallingDown.request.vote.popup.elements.PopupVote;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Metadata;
import me.FallingDownLib.CommonClasses.www.PopUpElements;
import me.FallingDownLib.CommonClasses.www.SiteDecorations;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * 
 * @author victork
 */
public class PrintVotePopUp implements ToCodeConverter {

    private StringBuilder votePopUpBuilder;
    private String postId;
    private Pass pass=null;
    private PostHash postInfo;
    private int popup_type=0;
    private String popup_title;
    private String popup_content;
    private HttpServletRequest associated_request;

    public static final int SERVER_ERROR_VOTEPOPUP=0;
    public static final int NORMAL_VOTEPOPUP=1;
    public static final int NOTCONNECTED_VOTEPOPUP=2;
    public static final int ALREADYVOTED_VOTEPOPUP=3;
    public static final int DOES_NOT_EXIST_VOTEPOPUP=4;

    /**
     * Default constructor of the popup
     */
    protected PrintVotePopUp(){
        votePopUpBuilder = new StringBuilder();
    }

    /**
     * 
     * @param uPass
     */
    protected PrintVotePopUp(Pass uPass){
        this();
        pass=uPass;
    }

    /**
     * Set the popup type
     * @param type
     */

    public void setPopUpType(int type){
        popup_type=type;
    }

    public void setPostInfo(PostHash pHash){
        postInfo=pHash;
    }

    public void associateRequest(HttpServletRequest request){
        associated_request=request;
    }

    /**
     * 
     * @param uPass
     * @return an instance
     */
    public static PrintVotePopUp getPopUpInstance(Pass uPass){
        return new PrintVotePopUp(uPass);
    }

    /**
     * 
     * @param key
     */
    public void setKey(String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new Exception("");
        } else {
            postId = key;
        }
    }

    /**
     * Build general html of the popup
     */

    private void buildPopUp() {
        votePopUpBuilder.append("<html>");
        buildHead();
        buildBody();
        
        votePopUpBuilder.append("</html>");
    }




    /**
     *
     * @return HTML code
     */
    public String getHTMLCode() {
        switch(popup_type){
            case SERVER_ERROR_VOTEPOPUP:
                buildErrorVotePopUp();
                break;
            case NORMAL_VOTEPOPUP:
                buildNormalVotePopUp();
                break;
            case NOTCONNECTED_VOTEPOPUP:
                buildLoginPopUp();
                break;
            case ALREADYVOTED_VOTEPOPUP:
                buildAlreadyVoted();
                break;
            case DOES_NOT_EXIST_VOTEPOPUP:
                buildPostDoesNotExists();
                break;
        }
        buildPopUp();
        return votePopUpBuilder.toString();
    }

    /**
     *
     */
    private void buildHead() {
        Metadata votepopup_meta = Metadata.getInstance();
        votepopup_meta.setAdditionnalKeywords("popup, vote, voxnucleus");
        votepopup_meta.setDescription("Votez en quelques secondes pour n'importe "
                + "quelle page, "
                + "depuis "
                + "n'importe où grâce à la fenêtre de vote VoxNucleus");
        votePopUpBuilder.append("<head>");
        insertJS();
        insertCSS();
        votePopUpBuilder.append(SiteDecorations.setFavIcon());
        votePopUpBuilder.append(SiteDecorations.getRankZoneStyle());
        votePopUpBuilder.append("<title>").append(popup_title).append("</title>");
        votePopUpBuilder.append(votepopup_meta.getHTMLCode());
        votePopUpBuilder.append("</head>");

    }

    /**
     * 
     */

    private void buildBody() {
        votePopUpBuilder.append("<body>");
        votePopUpBuilder.append("<div id=\"container\">");
        votePopUpBuilder.append("<div id=\"content\">");
        votePopUpBuilder.append(popup_content);
        votePopUpBuilder.append("</div>");
        votePopUpBuilder.append("</div>");
        PopupFooter popFooter = PopupFooter.getInstance(pass);
        votePopUpBuilder.append(popFooter.getHTMLCode());
        votePopUpBuilder.append(GoogleAnalytics.getAnalyticsCode());
        votePopUpBuilder.append("</body>");
    }

    private void insertJS() {
        votePopUpBuilder.append(SiteElements.getCommonScripts());
        votePopUpBuilder.append("<script type=\"text/javascript\""
                + " src=\"/jsp/popup/votePopUp.js\"></script>");

    }

    private void insertCSS() {
        votePopUpBuilder.append(PopUpElements.getGeneralCSS());
        votePopUpBuilder.append("<link rel=\"stylesheet\" type=\"text/css\""
                + " href=\"/css/popup/vote.css\" >");

    }

    private void buildLoginPopUp() {
        popup_title = "VoxNucleus : Se connecter";
        PopupLogin popup_login = PopupLogin.getInstance(associated_request);
        popup_content = popup_login.getHTMLCode();
    }

    private void buildNormalVotePopUp() {
        popup_title = "VoxNucleus : Voter pour un noyau";
        PopupVote vote_popup= PopupVote.getInstance(postInfo);
        popup_content = vote_popup.getHTMLCode();
    }
    
    private void buildPostDoesNotExists() {
        popup_title = "VoxNucleus : Erreur - Noyau non existant";
        popup_content= buildDoesNotExistZone();
    }

    private void buildAlreadyVoted() {
        popup_title = "VoxNucleus : Vous avez déjà voté pour ce noyau";
        PopupAlreadyVoted pop_already = PopupAlreadyVoted.getInstance(postInfo);
        popup_content = pop_already.getHTMLCode();
    }

    private String buildDoesNotExistZone() {
        StringBuilder doesnotexist = new StringBuilder();
        doesnotexist.append("Nous sommes désolés, mais il semble que le noyau "
                + "pour lequel vous voulez voter n'existe pas dans notre base de "
                + "données.");
        return doesnotexist.toString();
    }

    private void buildErrorVotePopUp() {
        popup_title = "VoxNucleus : Erreur";
        popup_content = buildErrorZone();
    }

    private String buildErrorZone() {
        StringBuilder error_builder = new StringBuilder();
        error_builder.append("Nous sommes désolés, mais le serveur semble avoir "
                + "rencontré "
                + "une erreur qui l'empêche de continuer la requête.");
        return error_builder.toString();
    }

}
