package FallingDown.post.modify.post;

import java.util.ArrayList;
import java.util.HashMap;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import org.apache.cassandra.thrift.Column;

/**
 * Save modifications after validation
 * @author victork
 */
public class SaveModificationPost {

    private String postId;
    private HashMap<String, String> mapOfModification;

    protected SaveModificationPost(String pId){
        pId=postId;
    }

    public static SaveModificationPost getInstance(String pId){
        return new SaveModificationPost(pId);
    }

    public void setHashMapModification(HashMap<String,String> mapModification){
        mapOfModification=mapModification;
    }

    private ArrayList<Column> buildHashMap(){
        return ColumnUtil.HashStringToArrayCol(mapOfModification);
    }

    
    public void doSave() {
        buildHashMap();

    }
}
