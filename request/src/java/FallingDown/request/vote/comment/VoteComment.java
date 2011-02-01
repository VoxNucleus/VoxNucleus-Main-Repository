package FallingDown.request.vote.comment;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.voxnucleus.comment.vote.IncorrectVoteComment;

/**
 *
 * @author victork
 */
public class VoteComment extends HttpServlet {

    public static final String PARAM_COMMENT_UUID="comment_id";
    public static final String PARAM_POST_ID="post_id";
    public static final String PARAM_ADD_SCORE="add_to_score";


    private String post_id;
    private String comment_uuid;
    private int add_to_score=0;

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
        Pass user_pass = Pass.getPass(request);
        user_pass.launchAuthentifiate();
        VoteCommentAnswer vote_answer = null;
        try {

            if (user_pass.getIsAuthentified()) {
                get_parameters_vote(request);
                DoVoteComment do_vote_comment = new DoVoteComment(post_id,comment_uuid,add_to_score,user_pass);
                do_vote_comment.doVote();
                vote_answer = VoteCommentAnswer.getInstance(true);
            } else {
                throw new IncorrectVoteComment("Utilisateur non connecté",IncorrectVoteComment.ERROR_NOT_CONNECTED);
            }
        }catch(IncorrectVoteComment ex){
            vote_answer = VoteCommentAnswer.getInstance(false, ex.getErrorCode(), ex.getReason());
        }
        Gson gson_converter = new Gson();
        Browser.sendJSONToBrowser(request, response, gson_converter.toJson(vote_answer));
    }

/**
 * Get the parameters from the request and
 * @param request
 * @throws IncorrectVoteComment
 */
    private void get_parameters_vote(HttpServletRequest request) throws IncorrectVoteComment {

        HashMap<String, String[]> param_map = new HashMap<String, String[]>(request.getParameterMap());
        if (param_map.get(PARAM_POST_ID) == null) {
            throw new IncorrectVoteComment("La requête est malformée (noyau manquant)",IncorrectVoteComment.ERROR_UNKNOWN);
        } else {
            post_id = param_map.get(PARAM_POST_ID)[0];
        }
        if (param_map.get(PARAM_COMMENT_UUID) == null) {
            throw new IncorrectVoteComment("La requête est malformée (commentaire manquant)",IncorrectVoteComment.ERROR_UNKNOWN);
        }else  {
            comment_uuid = param_map.get(PARAM_COMMENT_UUID)[0];
        }
        if (param_map.get(PARAM_ADD_SCORE) == null) {
            throw new IncorrectVoteComment("La requête est malformée (vote manquant)",IncorrectVoteComment.ERROR_UNKNOWN);
        } else {
            String to_add = param_map.get(PARAM_ADD_SCORE)[0];
            try {
                if (Integer.parseInt(to_add) >= 0) {
                    add_to_score = 1;
                } else if (Integer.parseInt(to_add) < 0) {
                    add_to_score = -1;
                }
            } catch (NumberFormatException ex) {
                throw new IncorrectVoteComment("La requête est malformée",IncorrectVoteComment.ERROR_UNKNOWN);
            }
        }

        
        
    }


}
