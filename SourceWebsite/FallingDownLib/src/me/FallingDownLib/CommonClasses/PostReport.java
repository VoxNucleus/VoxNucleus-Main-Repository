package me.FallingDownLib.CommonClasses;

import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostReport;

/**
 *
 * @author victork
 */
public class PostReport {

    private String related_postId;
    private String related_Reason;

    private PostReport(String postId,String reason){
        related_postId=postId;
        related_Reason=reason;
    }

    public static PostReport getNewReport(String postId,String reason){
        return new PostReport(postId,reason);
    }

    /**
     * 
     * @return
     */

    public String getReason(){
        return related_Reason;
    }

    /**
     *
     * @return
     */
    public String getPostId(){
        return related_postId;
    }

    /**
     * Validation of the report. Just check if it is not to long
     * @return
     * @throws IncorrectPostReport
     */
    public boolean validate() throws IncorrectPostReport{
        //TODO Check if post exists
        if(related_Reason.length()>1000){
            throw new IncorrectPostReport("Le texte du rapport fait "+
                    related_Reason.length() +
                    " caractères, la limite est de 1000 caractères");
        }
        return true;
    }

}
