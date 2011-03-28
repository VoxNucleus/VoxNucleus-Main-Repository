package FallingDown.post.newpost;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.PostFields;

/**
 * This class is attached to a newPost (New Nucleus) and enables one to
 * @author victork
 */
public class PostFiller {

    private HttpServletRequest associated_request;
    private String url="";
    private String title="";
    private String category="";
    private String sub_category="";
    private String tags="";
    private String remote_file="";
    private String lang="";
    private String description="";
    private boolean isPOSTMethod=false;

    protected PostFiller(){
        
    }

    protected PostFiller(HttpServletRequest request){
        this();
        associated_request=request;
    }

    /**
     *
     * @param request
     * @return an instance
     */
    public static PostFiller getInstance(HttpServletRequest request){
        return new PostFiller(request);
    }

    /**
     *
     */

    public void launchParameterRerieve() throws UnsupportedEncodingException{
        HashMap<String,String[]> parameterMap = new HashMap<String,String[]>(associated_request.getParameterMap());
        url = java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_LINK)),"UTF-8");
        tags=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_TAGS)),"UTF-8");
        title=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_TITLE)),"UTF-8");
        description=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_DESCRIPTION)),"UTF-8");
        remote_file=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_REMOTE_FILE)),"UTF-8");
        category=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_CATEGORY)),"UTF-8");
        sub_category=java.net.URLDecoder.decode(getFirstParameterOrBlank(parameterMap.get(
                PostFields.HTTP_SUB_CATEGORY)),"UTF-8");

    }

    /**
     * 
     * @param param_list
     * @return
     */
    private String getFirstParameterOrBlank(String[] param_list){
        String result="";
        if (param_list == null || param_list.length < 1) {
            result="";
        } else {
            if (isPOSTMethod) {
                result= param_list[0];
            } else {
                try {
                    result= java.net.URLDecoder.decode(param_list[0], "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    result="/!\\ Erreur dans ce que vous avez mis";
                    Logger.getLogger(PostFiller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public void setIsPOST(boolean isPost){
        isPOSTMethod=isPost;
    }

    /**
     *
     * @return title of nucleus
     */

    public String getTitle(){
        return title;
    }

    /**
     *
     * @return url of nucleus
     */

    public String getURL(){
        return url;
    }

    /**
     *
     * @return category
     */
    public String getCategory(){
        return category;
    }


    /**
     *
     * @return sub_category
     */
    public String getSubCategory(){
        return sub_category;
    }

    /**
     *
     * @return tags
     */
    public String getTags(){
        return tags;
    }

    /**
     *
     * @return url of the image for a nucleus
     */
    public String getURLImage(){
        return remote_file;

    }

    /**
     *
     * @return lang of the nucleus
     */
    public String getLang(){
        return lang;
    }


    /**
     *
     * @return description
     */
    public String getDescription(){
        return description;
    }

}
