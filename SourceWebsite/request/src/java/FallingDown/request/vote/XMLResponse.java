/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.request.vote;

import java.lang.StringBuffer;

/**
 * This class provides the XML builds a XML response to the user
 * @author victork
 */
public class XMLResponse {

    static public final int VOTE_OK=1;
    static public final int VOTE_FAILED=2;
    static public final int ALREADY_VOTED=3;
    static public final int UNKNOWN_USER=4;

    private int vote_status;
    private StringBuffer response;

    public XMLResponse(int status){
        this.vote_status=status;
        buildXML();
    }

    /**
     * Retrieve XML response with that
     * @return
     */
    public String getXML(){
        return response.toString();
        
    }

    private void buildXML() {
        response = new StringBuffer();
        response.append("<response>");
        response.append("<status>");
        switch(vote_status){
            case VOTE_OK:
                response.append("ok");
                break;
            case VOTE_FAILED :
                response.append("fail");
                break;
            case ALREADY_VOTED :
                response.append("already");
                break;
            case UNKNOWN_USER :
                response.append("unknown");
                break;
            default :
                response.append("error");
                break;
        }
        response.append("</status>");
        response.append("</response>");
    }

}
