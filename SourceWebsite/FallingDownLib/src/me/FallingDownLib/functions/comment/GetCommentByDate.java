package me.FallingDownLib.functions.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CassandraConnection.common.SuperColumnIterator;
import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.SuperColumn;

/**
 *
 * @author victork
 */
public class GetCommentByDate {
    private String postId;
    /**
     * 
     * @param pid
     */
    protected GetCommentByDate(String pid){
        postId=pid;
    }

    /**
     *
     * @param pid
     * @return
     */
    public static GetCommentByDate getInstance(String pid){
        return new GetCommentByDate(pid);
    }
    /**
     *
     * @param supercolList
     * @return
     */

    private List<CommentHash> convertSuperColumnList(List<SuperColumn> supercolList){
        ArrayList<CommentHash> listComHash= new ArrayList<CommentHash>();
        for(int indexSuperCol=0;indexSuperCol<supercolList.size();indexSuperCol++){
            listComHash.add(convertSuperColumn(supercolList.get(indexSuperCol)));
        }
        return listComHash;
    }

    /**
     *
     * @param superCol
     * @return
     */
    public CommentHash convertSuperColumn(SuperColumn superCol) {
        List<Column> list = superCol.getColumns();
        HashMap<String, String> map = new HashMap<String, String>();
        for (int indexCol = list.size() - 1; indexCol >= 0; indexCol--) {
            if (StringUtils.string(list.get(indexCol).getName()).equals(CommentFields.UUID)) {
                map.put(StringUtils.string(list.get(indexCol).getName()),
                        EasyUUIDget.toUUID(list.get(indexCol).getValue()).toString());
            } else {
                map.put(StringUtils.string(list.get(indexCol).getName()),
                        StringUtils.string(list.get(indexCol).getValue()));
            }
        }
        CommentHash cHash = new CommentHash(map);
        return cHash;
    }

    /**
     *
     * @return
     */
    public List<CommentHash> getListComment(){

        SuperColumnIterator superIterator = SuperColumnIterator
                .getSupercolIterator(ColumnFamilies.DB_COMMENTS_BY_POSTS, postId, 50);
        List<CommentHash> listCom =convertSuperColumnList(superIterator.get(0, 49));
        return listCom;
    }

}
