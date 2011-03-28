/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.cassandracrawler.document;

import me.FallingDownLib.CommonClasses.CommentFields;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import org.apache.solr.common.SolrInputDocument;


/**
 *
 * @author victork
 */
public class CommentToDocument {


    SolrInputDocument doc;
    CommentHash commentHash;
    byte[] uuid;
    public CommentToDocument(CommentHash commentHash,byte[] column){
        doc = new SolrInputDocument();
        uuid=column;
        this.commentHash=commentHash;
        putFields();
        setLowerPriority();

    }

    /**
     * Add all field to the document \n
     * Description : Not stored but fully searchable
     * author : Not stored but fully searchable
     */

    private void putFields() {
        
        doc.addField("doctype", "comment");
        doc.addField(CommentFields.INDEX_RELATED_POSTID, commentHash.getRelatedPostId());
        doc.addField(CommentFields.UUID, (EasyUUIDget.toUUID(uuid)).toString() );
        doc.addField(CommentFields.INDEX_TEXT, commentHash.getText());
        doc.addField(CommentFields.INDEX_TITLE, commentHash.getTitle());
        doc.addField(CommentFields.INDEX_AUTHOR, commentHash.getAuthor());
        doc.addField(CommentFields.INDEX_TIMESTAMP, commentHash.getTimestamp());
        //doc.postHash.getAuthor());
    }

    private void setLowerPriority(){
        doc.setDocumentBoost(0.9F);
    }

    /**
     *
     * @return Document created
     */
    public SolrInputDocument getDocument(){
        return doc;
    }

}
