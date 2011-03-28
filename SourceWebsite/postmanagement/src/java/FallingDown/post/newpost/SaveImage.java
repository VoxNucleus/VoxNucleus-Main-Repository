package FallingDown.post.newpost;

import java.net.MalformedURLException;
import me.FallingDownLib.CommonClasses.Exceptions.FileToBigException;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import me.FallingDownLib.CommonClasses.Exceptions.FileIsNotAnImage;
import me.FallingDownLib.CommonClasses.image.ImageFields;
import me.FallingDownLib.CommonClasses.post.RemoteImageGetter;
import me.FallingDownLib.disk.FoldersOnDisk;
import org.apache.commons.fileupload.FileItem;

/**
 *This class enables manipulation of image send by servlet
 * Ultimately this class will be able to resize image to
 * @author victork
 */
public class SaveImage {

    public static String charArray="abcdefghijklmnopqrstuvwxyz0123456789";

    private FileItem itemToSave = null;
    private String fileName = null;
    private String directory = null;
    private String urlOfImage;
    InputStream fileInputStream;
    private String itemName = null;
    private Random randomGenerator;
    private final double IMAGE_SIZE_WIDTH = 100;
    private final double IMAGE_SIZE_HEIGHT = 100;

    /**
     * Resize the image to IMAGE_SIZE_WIDTH x IMAGE_SIZE_HEIGHT
     */
    public BufferedImage reshapeImage(BufferedImage imageIn) {

        double scale_width = this.IMAGE_SIZE_WIDTH / imageIn.getWidth();
        double scale_height = this.IMAGE_SIZE_HEIGHT / imageIn.getHeight();

        BufferedImageOp transfo = new AffineTransformOp(
                AffineTransform.getScaleInstance(scale_width, scale_height),
                new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC));
        return transfo.filter(imageIn, null);

    }

    /**
     * Saves an image to the disk creating its own directory "directoryName" with it
     * @param item
     * @param directoryName
     */
    public SaveImage(FileItem item, String url) {
        this.itemToSave = item;
        urlOfImage = url;
        randomGenerator= new Random();
    }

    /**
     *
     * @return random name
     */
    private String constructRandomName(){
        StringBuilder random_builder = new StringBuilder();
        for(int index =0; index<5;index++ ){
            random_builder.append(charArray.charAt(randomGenerator.nextInt(32)));
        }

        return random_builder.append(".png").toString();
    }


    /**
     *
     * @return a standard name with only alpha numerical characters
     */
    private String buildImageName() {
        String reg = "[^a-zA-Z0-9]";
        String replacingtext = "_";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(itemName);
        StringBuffer filename_buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(filename_buffer, replacingtext);
        }
        int indexOf = itemName.indexOf(".");
        filename_buffer.substring(Math.max(indexOf - 5, 0), indexOf);
        return filename_buffer.append(".png").toString();
    }

    private void constructBuffer() throws FileToBigException, MalformedURLException, IOException {
         if (itemToSave!=null && !itemToSave.getName().isEmpty()) {
             itemName = itemToSave.getName();
            if (itemToSave.getSize() > ImageFields.MAXIMUM_IMAGE_FILESIZE_OCTET) {
                throw new FileToBigException("Le fichier fait : "
                        + itemToSave.getSize() + "octets au lieu de "
                        + ImageFields.MAXIMUM_IMAGE_FILESIZE_OCTET + " octets au maximum");
            } else {
                fileInputStream = itemToSave.getInputStream();
            }
            fileName =buildImageName();
        } else if (urlOfImage !=null && !urlOfImage.isEmpty()) {
            RemoteImageGetter remoteImage = RemoteImageGetter.getRemoteGetter(urlOfImage);
            fileInputStream = remoteImage.getImageStream();
            fileName=constructRandomName();
        } else {
            fileName = "null";
        }
    }

    /**
     *
     * @param dirName
     * @return
     */
    private boolean makeDirectoryFromString(String dirName) {
        File directoryToCreate = new File(FoldersOnDisk.POST_IMAGES + dirName);
        if (!directoryToCreate.exists()) {
            return directoryToCreate.mkdir();
        } else {
            return true;
        }
    }

    /**
     * Method that is called to effectively save the image to the disk.
     * @param directoryName
     * @throws IOException
     */
    public void doSave(String directoryName) throws IOException, FileIsNotAnImage, FileToBigException {
        constructBuffer();
        if (!fileName.equals("null")) {
            this.directory = directoryName;
            if (makeDirectoryFromString(directory)) {
                File savedFile = new File(FoldersOnDisk.POST_IMAGES + directory + "/" + fileName);
                //Try with this
                BufferedImage imageInBuffer = ImageIO.read(fileInputStream);
                if (fileInputStream != null && imageInBuffer != null) {
                    BufferedImage reshapedImage = reshapeImage(imageInBuffer);
                    ImageIO.write(reshapedImage, "png", savedFile);
                } else {
                    throw new FileIsNotAnImage(itemName);
                }
            }
        }
        //Maybe it needs to be finished ?
    }

    /**
     *
     * @returnfilename
     */
    public String getFileName() {


        return fileName;
    }
}
