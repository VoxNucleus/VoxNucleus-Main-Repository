package FallingDown.post.modify.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.PostConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostInfo;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.post.TagsAnalyser;
import me.FallingDownLib.CommonClasses.util.FieldVerificator;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author victork
 */
public class PostModificator {

    private HttpServletRequest associatedRequest;
    private HttpServletResponse associatedResponse;
    private String postId;
    HashMap<String, String> modifyHash;
    private HashMap<String, String> formFields;


    private String description;
    private String tags;
    private String title;

    protected PostModificator(){
        modifyHash = new HashMap<String, String>();
        formFields = new HashMap<String, String>();
    }

    protected PostModificator(HttpServletRequest request,HttpServletResponse response,String pId){
        this();
        postId= pId;
        associatedRequest = request;
        associatedResponse = response;
    }

    public static PostModificator getInstance(HttpServletRequest request,
            HttpServletResponse response,String pId){
        return new PostModificator(request,response,pId);
    }

    public void saveModifications() throws IncorrectPostInfo, Exception {
        processRequest(associatedRequest);
        getParameters();
        validate();
        saveToCassandra();
    }

    /**
     * Fill formFields from map of parameters
     * @param associatedRequest
     */
    private void processRequest(HttpServletRequest associatedRequest) {

        HashMap<String,String[]> temp= new HashMap<String,String[]>(associatedRequest.getParameterMap());
        Iterator<String> iterator = temp.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            formFields.put(key,temp.get(key)[0]);
        }
    }

    private void validate() throws IncorrectPostInfo {
       addToInformationToSave(PostFields.DB_DESCRIPTION,description,
               FieldVerificator.post_verify_description(description));
       addToInformationToSave(PostFields.DB_TITLE,title,
               FieldVerificator.post_verify_title(title));
       addToInformationToSave(PostFields.DB_TAGS,tags,
               FieldVerificator.post_verify_tags(tags));
    }

    private void saveToCassandra() throws NotFoundException, Exception {
        PostConnector pConnector = PostConnector.getInstance(postId);
        ArrayList<Column> listCol = ColumnUtil.HashStringToArrayCol(modifyHash);
        pConnector.batchInsertFields(listCol);

    }

    /**
     * Retrieve parameters from map
     */
    private void getParameters() {
        description = StringEscapeUtils.escapeHtml(replaceNull(formFields.get(PostFields.HTTP_DESCRIPTION)));
        title = StringEscapeUtils.escapeHtml(replaceNull(formFields.get(PostFields.HTTP_TITLE)));
        TagsAnalyser analyser = new TagsAnalyser(formFields.get(PostFields.HTTP_TAGS));
        this.tags = analyser.getCleanedString();

    }

    /**
     * Insert a value if it is correct
     * @param field
     * @param value
     * @param isValid
     */
    private void addToInformationToSave(String field, String value, boolean isValid){
        modifyHash.put(field, value);
    }


     /**
     *
     * @param in
     * @return empty string instead of null
     */
    private String replaceNull(String in) {
        if (in == null) {
            return "";
        } else {
            return in;
        }

    }

}
