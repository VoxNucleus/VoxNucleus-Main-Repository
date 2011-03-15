package me.FallingDownLib.CommonClasses.action;

import java.util.HashMap;

/**
 *
 * @author victork
 */
public class VoteAction extends Action implements ActionInterface {

    private String relatedPostId;

    /**
     *
     * @param maker
     */
    public VoteAction(String maker){
        super(maker);
        super.setActionType(Action.ACTION_TYPE_VOTE);
        
    }

    /**
     * 
     * @return
     */
    public String getRelatedPostId(){
        return relatedPostId;
    }


    /**
     * 
     * @return
     */

    @Override
    public HashMap<String, String> getParameterHash() {
        HashMap<String,String> result = super.getParameterHash();
        return result;
    }

}
