package FallingDown.Image;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author victork
 */
public class ImageOpener {

    // for local public static String PATH_TO_IMAGE = "/home/victork/img/";
    public static String PATH_TO_IMAGE = "/opt/victor/images/";

    ImageOpener(String fileName, HttpServletResponse response) throws FileNotFoundException, IOException {

        Pattern nullIdentifier = Pattern.compile(".*null");
        Matcher nullMatcher = nullIdentifier.matcher(fileName);

        Pattern doubleDotIdentifier = Pattern.compile("\\.\\.");
        Matcher doubleDotMatcher = doubleDotIdentifier.matcher(fileName);


        if (nullMatcher.find() || doubleDotMatcher.find()) {
            getImageBack("website/post/imagepostdefault.png", response);
        } else {
            getImageBack(fileName, response);
        }
    }

    private void getImageBack(String fileName,HttpServletResponse response) throws FileNotFoundException, IOException{

        File file = new File(PATH_TO_IMAGE + fileName);
        response.setContentType(getMimeType(file));

        response.setContentLength((int) file.length());
        // Open the file and output streams
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        in.close();
        out.close();
    }


    private String getMimeType(File file){
        return new MimetypesFileTypeMap().getContentType(file);
    }
}
