package me.FallingDownLib.CommonClasses.action;

/**
 *
 * @author victork
 */
public class NewCommentAction extends Action {

    private String relativePostId;
    private byte[] commentUUID;

    public NewCommentAction(String maker){
        super(maker);
    }

    public void setCommentUUID(byte[] UUID){
        commentUUID=UUID;
    }

    public void setRelPostId(String pId){
        relativePostId= pId;
    }
}
