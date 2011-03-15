package me.FallingDownLib.CommonClasses.www.comment;

import java.util.ArrayList;
import java.util.List;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintRegularListComment implements ToCodeConverter {

    private StringBuilder listcomment_builder;
    private String postId;
    private ArrayList<CommentHash> listComment;

    /**
     *
     */
    protected PrintRegularListComment(){
        listcomment_builder = new StringBuilder();
    }

    /**
     * 
     * @param pid
     */
    
    protected PrintRegularListComment(List<CommentHash> list_comment){
        this();
        listComment = new ArrayList<CommentHash>(list_comment);
    }

    /**
     *
     * @param postId
     * @return
     */
    public static PrintRegularListComment getListComment(List<CommentHash> listComment){
        return new PrintRegularListComment(listComment);
    }

    /**
     * 
     * @param list
     */

    public void setListComment(ArrayList<CommentHash> list){
        listComment =list;
    }

    /**
     *
     */
    private void buildList(){
        for(int index=0;index<listComment.size();index++){
            PrintComment print_comment = new PrintComment(listComment.get(index));
            listcomment_builder.append(print_comment.getHTMLCode());
        }
    }

    /**
     * 
     * @return
     */

    public String getHTMLCode() {
        buildList();
        return listcomment_builder.toString();
    }
}
