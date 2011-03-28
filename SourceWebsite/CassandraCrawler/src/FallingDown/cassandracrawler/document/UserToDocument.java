/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FallingDown.cassandracrawler.document;


import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.UserHash;
import org.apache.solr.common.SolrInputDocument;


/**
 * Class that takes a UserHash object and produces a SolRDocument
 * @author victork
 */
public class UserToDocument {

    SolrInputDocument doc;
    UserHash userHash;

    public UserToDocument(UserHash user) {
        doc = new SolrInputDocument();
        userHash = user;
        putFields();
    }

    /**
     * Set all fields of the document
     */

    private void putFields() {
        doc.addField("doctype", "user");
        doc.addField(UserFields.INDEX_USERNAME, userHash.getUsername());
        doc.addField(UserFields.INDEX_FIRSTNAME, userHash.getFirstname());
        doc.addField(UserFields.INDEX_NAME, userHash.getLastname());
        doc.addField(UserFields.UUID, userHash.getUUID());
        doc.addField(UserFields.INDEX_LANGUAGE, userHash.getLanguage());
        doc.addField(UserFields.INDEX_EMAIL, userHash.getEmail());

    }

    /**
     *
     * @return Document created
     */
    public SolrInputDocument getDocument() {
        return doc;
    }
}
