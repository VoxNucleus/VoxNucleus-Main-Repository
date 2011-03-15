package me.FallingDownLib.CommonClasses.action;

import java.util.HashMap;

/**
 * Abstract class of actions
 * @author victork
 */
public abstract class Action implements ActionInterface{

    public static final int ACTION_TYPE_VOTE=1;
    public static final int ACTION_TYPE_NEW_NUCLEUS=2;
    public static final int ACTION_TYPE_NEW_COMMENT=3;

    protected long timestampAction;
    protected int actionType;
    private String action_maker;


    /**
     * Constructor never called
     * @param maker
     */
    protected Action(String maker){
        action_maker=maker;
    }

    /**
     * Set the action
     * @param action
     */
    protected void setActionType(int action){
        actionType=action;
    }

    /**
     * 
     * @return hash parameter
     */

    public HashMap<String, String> getParameterHash() {
        HashMap<String,String> result = new HashMap<String,String>();
        result.put(ActionFields.ACTION_TYPE, Integer.toString(actionType));
        return result;
    }

    
}
 