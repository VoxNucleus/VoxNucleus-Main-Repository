package FallingDown.post.viewPost.elements;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.www.comment.PrintRegularListComment;
import me.FallingDownLib.functions.comment.GetCommentByDate;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class NucleusComments implements ToCodeConverter {

    private StringBuilder nuc_comment_builder;
    private String postId;
    private boolean isStaticComment;
    private HttpServletRequest request;

    protected NucleusComments(){
        nuc_comment_builder = new StringBuilder();
    }

    protected NucleusComments(String pId){
        this();
        postId=pId;
    }

    public static NucleusComments getInstance(String pId){
        return new NucleusComments(pId);
    }

    public void setIsStaticComment(boolean static_comment){
        isStaticComment = static_comment;
    }

    public String getHTMLCode() {
        buildComments();
        return nuc_comment_builder.toString();
    }

    public void setHttpRequest(HttpServletRequest in_request){
        request = in_request;
    }

    private void buildComments() {
        nuc_comment_builder.append("<div class=\"listcomments\" >");
        nuc_comment_builder.append("<div id=\"comment_title\">Commentaires : </div>");
        if (isStaticComment) {
            insertStaticComment();
        } else {
            nuc_comment_builder.append("<div id=\"lastpost\" style=\"display:none\">")
                    .append(0).append("</div>");
            nuc_comment_builder.append("<div id=\"load_more\"> <a href=\"#\" onClick=\"retrieveComments('")
                    .append(postId).append("');\" >[+] Charger plus de commentaires </a> </div>");
            nuc_comment_builder.append("<div id=\"javascript_error\"> <b> Votre ordinateur n'a pas les capacités."
                    + "<a href=\"").append(request.getRequestURL().append("?static=true"))
                    .append("\"> Cliquez ici pour voir la nuc_comment_builder en version statique</a></b> </div>");
        }
        nuc_comment_builder.append("</div>");
    }

    private void insertStaticComment(){
        GetCommentByDate getByDate = GetCommentByDate.getInstance(postId);
        List<CommentHash> listComment=getByDate.getListComment();
        PrintRegularListComment regularList = PrintRegularListComment.getListComment(listComment);
        nuc_comment_builder.append(regularList.getHTMLCode());
    }

}
