package me.FallingDownLib.CommonClasses.post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import me.FallingDownLib.CommonClasses.Exceptions.FileToBigException;
import me.FallingDownLib.CommonClasses.image.ImageFields;

/**
 *
 * @author victork
 */
public class RemoteImageGetter {

    private String image_location;
    private BufferedReader buffer;
    URLConnection conn;

    /**
     * Default constructor
     */
    protected RemoteImageGetter(){

    }

    /**
     *
     * @param url
     */
    protected RemoteImageGetter(String url){
        this();
        image_location=url;
    }

    /**
     *
     * @param url
     * @return an instance of a REmoteImageGetter
     */
    public static RemoteImageGetter getRemoteGetter(String url){
        return new RemoteImageGetter(url);
    }

    /**
     * 
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileToBigException
     */

    private void beginRetrieve() throws MalformedURLException, IOException, FileToBigException{
        URL url = new URL(image_location);
        conn = url.openConnection();
        verifySize(conn);
    }


    /**
     * Verifies that the remote file has a size inferior to 3Mb.
     * @param conn
     * @throws FileToBigException
     */

    private void verifySize(URLConnection conn) throws FileToBigException{
        if(conn.getContentLength()>ImageFields.MAXIMUM_IMAGE_FILESIZE_OCTET)
            throw new FileToBigException("Le fichier fait : "
                            + conn.getContentLength() + "octets au lieu de "
                            + ImageFields.MAXIMUM_IMAGE_FILESIZE_OCTET+" octets au maximum" );
    }


    /**
     *
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public InputStream getImageStream() throws MalformedURLException,
            IOException, FileToBigException{
        beginRetrieve();
        return conn.getInputStream();
    }

}
