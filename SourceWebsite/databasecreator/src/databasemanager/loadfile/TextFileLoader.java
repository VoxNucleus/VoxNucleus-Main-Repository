package databasemanager.loadfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victork
 */
public class TextFileLoader {
    private String file_text;
    private String filename;
    private StringBuilder file_content;


    protected TextFileLoader(String name){
        file_content = new StringBuilder();
        filename=name;
    }

    public static TextFileLoader getInstance(String name){
        return new TextFileLoader(name);
    }

    /**
     *
     * @return content of the file
     */

    public String getFContent(){
        loadContent();
        return file_content.toString();
    }

    /**
     * Load content of the file into the StringBuilder
     */
    private void loadContent() {
        File file_to_read = new File(filename);
        BufferedReader content_reader = null;
        try {
            content_reader = new BufferedReader(new FileReader(file_to_read));
            String text = null;
            while ((text = content_reader.readLine()) != null) {
                //We append a space instead of a new line
                file_content.append(text).append("");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (content_reader != null) {
                    content_reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(TextFileLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
