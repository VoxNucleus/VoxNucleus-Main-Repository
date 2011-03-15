package me.FallingDownLib.CommonClasses.xml;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author victork
 */
public class HashToXML {

    DocumentBuilderFactory dbfac;
    DocumentBuilder docBuilder = null;
    Document doc;
    /**
     * Constructor.
     * It initializes The documents that the will need later.
     */

    public HashToXML() {
        dbfac = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //Should never happen.
            //TODO low priority
        }
        doc = docBuilder.newDocument();
    }

    /**
     * Convert a HashMap of a Post to Document tree
     * @param map
     */

    public void PostHashMapToXML(HashMap<String, HashMap<String,String> > map) {

        Iterator<String> mainIt= map.keySet().iterator();
        Element posts = doc.createElement("posts");
        doc.appendChild(posts);
        
        while(mainIt.hasNext()){
            
            String id = mainIt.next();
            Element post = doc.createElement("post");
            post.setAttribute("id", id);
            posts.appendChild(post);
            Iterator<String> secondIt = map.get(id).keySet().iterator();

            while (secondIt.hasNext()) {
                String attributeName = secondIt.next();
                Element elAttr = doc.createElement(attributeName);
                post.appendChild(elAttr);
                Text attr = doc.createTextNode(map.get(id).get(attributeName));
                elAttr.appendChild(attr);
            }
        }
    }


    /**
     * Convert a HashMap of a comment into a XML tree
     * @param map
     */
    public void CommentHashMapToXML(HashMap<String, HashMap<String,String> > map) {

        Iterator<String> mainIt= map.keySet().iterator();
        Element posts = doc.createElement("comments");
        doc.appendChild(posts);

        while(mainIt.hasNext()){

            String id = mainIt.next();
            Element post = doc.createElement("comment");
            post.setAttribute("id", id);
            posts.appendChild(post);
            Iterator<String> secondIt = map.get(id).keySet().iterator();

            while (secondIt.hasNext()) {
                String attributeName = secondIt.next();
                Element elAttr = doc.createElement(attributeName);
                post.appendChild(elAttr);
                Text attr = doc.createTextNode(map.get(id).get(attributeName));
                elAttr.appendChild(attr);
            }
        }
    }



    /**
     * Output a String
     * //TODO change trans properties
     * @return
     */

    public String getString() {

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans=null;
        try {
            trans = transfac.newTransformer();
        } catch (TransformerConfigurationException ex) {
            //Should not happen
            //TODO Priority low
        }
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        //create string from xml tree
       ByteArrayOutputStream out = new ByteArrayOutputStream();


        StreamResult result = new StreamResult(out);
        DOMSource source = new DOMSource(doc);
        try {

            trans.transform(source, result);
        } catch (TransformerException ex) {
            //Should not happen
            //TODO Priority low
        }
        byte[] charData = out.toByteArray();
        String str=null;
        try {
            //str = new String(charData, "ISO-8859-1");
            str = new String(charData, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            //NOT GOING TOHAPPEN
            Logger.getLogger(HashToXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return str;
    }
}
