package me.FallingDownLib.CommonClasses.Exceptions;

/**
 * Exception that is thrown when the InputStream is not an image
 * @author victork
 */
public class FileIsNotAnImage extends Exception{
    private String fileName;

    /**
     * Constructor
     * @param file
     */

    public FileIsNotAnImage(String file){
        super();
        fileName=file;
    }

    /**
     *
     * @return the name of the source file
     */
    public String getImageName(){
        return fileName;
    }

}
