package FallingDown.request.vote;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.logger.VoteLogger;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.voxnucleus.nucleus.vote.IncorrectVoteNucleus;


/**
 *
 * @author victork
 */
public class vote extends HttpServlet {

    private String username;
    private String post_id;
    
    private int result_vote=0;

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

        VoteNucleusAnswer vote_answer=null;
        VoteLogger vlogger = VoteLogger.getVoteLogger();
        Pass votePass = Pass.getPass(request);
        votePass.launchAuthentifiate();
        try {
            if (votePass.getIsAuthentified()) {
                get_parameters(request);
                vlogger.start();

                newVote vote = new newVote(votePass.getUsername(), post_id, result_vote);
                vote_answer = VoteNucleusAnswer.getInstance(true);

            } else {
                throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_NOT_CONNECTED, "Utilisateur "
                        + "non connecté.");
            }
        } catch (IncorrectVoteNucleus ex) {
            vote_answer = VoteNucleusAnswer.getInstance(false, IncorrectVoteNucleus.ERROR_NOT_CONNECTED, ex.get_reason());
        } finally {
            vlogger.stop();
        }

        Gson gson_convert = new Gson();
        Browser.sendJSONToBrowser(request, response, gson_convert.toJson(vote_answer));

    }

    /**
     * get parameters
     * @param request
     * @throws IncorrectVoteNucleus
     */

    private void get_parameters(HttpServletRequest request) throws IncorrectVoteNucleus {

        HashMap<String, String[]> parameter_map = new HashMap<String, String[]>(request.getParameterMap());
        if (parameter_map.get("postId") == null) {
            throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Erreur"
                    + " dans la requête.");
        } else {
            post_id = parameter_map.get("postId")[0];
        }
        if (parameter_map.get("vote") == null) {
            throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Erreur"
                    + " dans la requête.");
        } else {

            try {
                result_vote = Integer.parseInt(parameter_map.get("vote")[0]);
            } catch (NumberFormatException ex) {
                throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Erreur"
                        + " dans la requête.");
            }
            if (result_vote != 1 && result_vote != -1) {
                throw new IncorrectVoteNucleus(IncorrectVoteNucleus.ERROR_UNKNOWN, "Erreur"
                        + " dans la requête.");
            }
        }
    }
}
