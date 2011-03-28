package FallingDown.cassandracrawler.document;

import FallingDown.cassandracrawler.logger.CrawlerLogger;
import java.io.UnsupportedEncodingException;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.PostHash;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author victork
 */
public class PostToDocument {


    SolrInputDocument doc;
    PostHash postHash;
    public PostToDocument(PostHash postHash){
        doc = new SolrInputDocument();
        this.postHash=postHash;
        putFields();
    }

    /**
     * Add all field to the document \n
     * Description : Not stored but fully searchable
     * author : Not stored but fully searchable
     */

    private void putFields() {
        doc.addField("doctype", "post");
        doc.addField(PostFields.INDEX_DESCRIPTION, postHash.getDescription());
        doc.addField(PostFields.INDEX_AUTHOR, postHash.getAuthor());
        doc.addField(PostFields.INDEX_KEY, postHash.getKey());
        doc.addField(PostFields.INDEX_TITLE, postHash.getTitle());
        doc.addField(PostFields.INDEX_TAGS, postHash.getTags());
        doc.addField(PostFields.INDEX_TIMESTAMP_CREATED,postHash.getTimestamp());
        doc.addField(PostFields.INDEX_LANGUAGE, postHash.getLanguage());
        doc.addField(PostFields.UUID, postHash.getUUID());
        doc.addField(PostFields.INDEX_CATEGORY, postHash.getCategory());
        String not_encoded_URL=postHash.getLink();
        String encoded_URL="";
        if(!not_encoded_URL.isEmpty()){
            try {
                encoded_URL=java.net.URLEncoder.encode(not_encoded_URL, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                encoded_URL="";
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
        }
        doc.addField(PostFields.INDEX_LINK, not_encoded_URL);
        doc.addField(PostFields.INDEX_SUB_CATEGORY, postHash.getSubcategory());

    }

    /**
     *
     * @return Document created
     */
    public SolrInputDocument getDocument(){
        return doc;
    }

}
