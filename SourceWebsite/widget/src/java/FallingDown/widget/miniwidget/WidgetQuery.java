package FallingDown.widget.miniwidget;

import FallingDown.widget.miniwidget.exception.WidgetTypeUnrecognized;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import me.FallingDownLib.CassandraConnection.connectors.WidgetConnector;

/**
 *
 * @author victork
 */
public class WidgetQuery {

    public static final String SOURCE_PAGE="wherefrom";
    public static final String BUTTON_TYPE="buttontype";

    public static final int WIDGET_ICON=0;
    public static final String STRING_WIDGET_ICON="icone";
    public static final int WIDGET_SMALL=0;


    private int widget_type;
    private String s_widget_type;
    private String url;
    private WidgetResponse w_response;
    private final HashMap<String, String[]> query_param;
    private ArrayList<String> list_postid;


    protected WidgetQuery(HashMap<String,String[]> parameterMap){
        query_param=parameterMap;
    }

    /**
     *
     * @param parameterMap
     * @return instance
     */
    public static WidgetQuery getInstance(HashMap<String,String[]> parameterMap){
        return new WidgetQuery(parameterMap);
    }


    /**
     * 
     * @return
     * @throws WidgetTypeUnrecognized
     * @throws Exception
     */
    public WidgetResponse retrieveResponse() throws WidgetTypeUnrecognized, Exception{
        getParameters();
        searchDatabase();
        w_response=buildResponse();
        return w_response;
    }

    /**
     *
     * @return true if the send query is valid, false if not
     */
    public boolean isValidQuery(){
        return verifyParameters();
    }

    private void searchDatabase() throws Exception {
        WidgetConnector w_connector = WidgetConnector.getInstance(url);
        list_postid = w_connector.getFirstTwoFromDatabase();
    }

    /**
     * The two values need to be present
     * @return true if both values are present false if not
     */

    private boolean verifyParameters() {
        if(query_param.containsKey(SOURCE_PAGE) && query_param.containsKey(BUTTON_TYPE))
            return true;
        else
            return false;
    }

    /**
     * 
     */

    private void getParameters() throws WidgetTypeUnrecognized, UnsupportedEncodingException {
        url = java.net.URLDecoder.decode(query_param.get(SOURCE_PAGE)[0],"UTF-8");
        s_widget_type=STRING_WIDGET_ICON;//query_param.get(BUTTON_TYPE)[0];
        if(s_widget_type.equals(STRING_WIDGET_ICON)){
            widget_type=WIDGET_ICON;
        }else{
            throw new WidgetTypeUnrecognized();
        }
    }

    private WidgetResponse buildResponse() {
        if (list_postid.isEmpty()) {
            return new WidgetResponse(false, 0, "none");
        } else {
            return new WidgetResponse(true, 0, list_postid.get(0));
        }
    }

    
}
