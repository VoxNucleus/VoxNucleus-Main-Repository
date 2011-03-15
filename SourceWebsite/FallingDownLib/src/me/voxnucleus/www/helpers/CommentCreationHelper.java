package me.voxnucleus.www.helpers;

import me.FallingDownLib.CommonClasses.CommentFields;

/**
 *
 * @author victork
 */
public class CommentCreationHelper extends Helper {

    protected CommentCreationHelper(){
        super();
        super.addHelp(CommentFields.HTTP_COMMENT_TEXT, "Ce que vous souhaitez dire "
                + "à tout le monde.");
        super.addHelp(CommentFields.HTTP_COMMENT_TITLE, "Le titre de votre "
                + "commentaire (facultatif).");
        
    }

    public static CommentCreationHelper getHelper(){
        return new CommentCreationHelper();
    }

}
