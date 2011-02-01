package FallingDown.request.vote.popup;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.VoteByPostConnector;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;

import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;


/**
 *
 * @author victork
 */
public class VotePopUp extends HttpServlet {

    public String postKey;

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
        Pass votePass = Pass.getPass(request);
        votePass.launchAuthentifiate();
        getParameters(request);
        if (votePass.getIsAuthentified()) {
            PrintVotePopUp popUp = PrintVotePopUp.getPopUpInstance(votePass);
            try {
                VoteByPostConnector v_post_Connector = VoteByPostConnector.getConnector(postKey);
                PostHash pHash = new PostHash(Post.getPostFromDatabase(postKey));
                popUp.setKey(postKey);
                popUp.setPostInfo(pHash);
                if (!v_post_Connector.hasUserVotedForPost(votePass.getUsername())) {
                    popUp.setPopUpType(PrintVotePopUp.NORMAL_VOTEPOPUP);
                } else {
                    popUp.setPopUpType(PrintVotePopUp.ALREADYVOTED_VOTEPOPUP);
                }
            } catch (PostDoesNotExist ex) {
                popUp.setPopUpType(PrintVotePopUp.DOES_NOT_EXIST_VOTEPOPUP);
                Logger.getLogger(VotePopUp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                popUp.setPopUpType(PrintVotePopUp.SERVER_ERROR_VOTEPOPUP);
                popUp.setPopUpType(PrintVotePopUp.SERVER_ERROR_VOTEPOPUP);
                Logger.getLogger(VotePopUp.class.getName()).log(Level.SEVERE, null, ex);
            }
            Browser.sendResponseToBrowser(request, response, popUp);
        }else{
            PrintVotePopUp popUp = PrintVotePopUp.getPopUpInstance(votePass);
            popUp.setPopUpType(PrintVotePopUp.NOTCONNECTED_VOTEPOPUP);
            popUp.associateRequest(request);
            Browser.sendResponseToBrowser(request, response, popUp);
        }

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

    /**
     * 
     * @param request
     */
    private void getParameters(HttpServletRequest request) {
        HashMap<String, String[]> parameterMap = new HashMap<String, String[]>(request.getParameterMap());
        if (parameterMap.get("postId") != null) {
            postKey = parameterMap.get("postId")[0];
        }
    }
}
