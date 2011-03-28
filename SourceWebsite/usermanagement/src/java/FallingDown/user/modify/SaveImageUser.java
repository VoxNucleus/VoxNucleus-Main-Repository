package FallingDown.user.modify;

/**
 *
 * @author victork
 */
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.util.regex.*;
import javax.imageio.ImageIO;
import me.FallingDownLib.CommonClasses.Exceptions.FileIsNotAnImage;
import me.FallingDownLib.CommonClasses.Exceptions.FileToBigException;
import me.FallingDownLib.disk.FoldersOnDisk;
import org.apache.commons.fileupload.FileItem;

/**
 *This class enables manipulation of image send by servlet
 * Ultimately this class will be able to resize image to
 * @author victork
 */
public class SaveImageUser {

    private FileItem itemToSave = null;
    private String fileName = null;
    private String directory = null;
    private String itemName = null;
    private static final double BIGIMAGE_SIZE_WIDTH = 80;
    private static final double BIGIMAGE_SIZE_HEIGHT = 80;
    private static final double SMALLIMAGE_SIZE_WIDTH = 25;
    private static final double SMALLIMAGE_SIZE_HEIGHT = 25;
    // 3 Mo maximum file
    private static final long MAXIMUM_IMAGE_FILESIZE = 1024 * 1024 * 3;

    /**
     * Resize the image to IMAGE_SIZE_WIDTH x IMAGE_SIZE_HEIGHT
     */
    public BufferedImage reshapeImage(BufferedImage imageIn, double asked_Width, double asked_Height) {
        double scale_width = asked_Width / imageIn.getWidth();
        double scale_height = asked_Height / imageIn.getHeight();
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
    public SaveImageUser(FileItem item) throws FileToBigException {
        this.itemToSave = item;
        itemName = item.getName();
        if (itemName.length() > 0) {
            String reg = "[^a-zA-Z0-9]";
            String replacingtext = "_";
            if (item.getSize() > MAXIMUM_IMAGE_FILESIZE) {
                throw new FileToBigException("Le fichier fait : "
                        + item.getSize() + "octets au lieu de "
                        + MAXIMUM_IMAGE_FILESIZE + " octets au maximum");
            }
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(itemName);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buffer, replacingtext);
            }
            int indexOf = itemName.indexOf(".");
            this.fileName = (itemName.substring(Math.max(indexOf - 5, 0), indexOf));
        } else {
            fileName = "null";
        }
    }

    /**
     *
     * @param dirName
     * @return true if the directory has been successfuly made or if
     */
    private boolean makeDirectoryFromString(String dirName) {
        File directoryToCreate = new File(FoldersOnDisk.USER_IMAGES + dirName);
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
    public void doSave(String directoryName) throws IOException, FileIsNotAnImage {
        if (!fileName.equals("null")) {
            directory = directoryName;
            if (makeDirectoryFromString(directory)) {
                //Read image from item
                InputStream fileInputStream = itemToSave.getInputStream();
                BufferedImage imageInBuffer = ImageIO.read(fileInputStream);
                if (fileInputStream != null && imageInBuffer != null) {
                    doSaveBig(imageInBuffer);
                    doSaveMini(imageInBuffer);
                } else {
                    throw new FileIsNotAnImage(itemName);
                }
            }
        }
    }

    /**
     * Transform the buffered image into the big image (80px * 80px)
     * @param imageInBuffer
     * @throws IOException
     */
    private void doSaveBig(BufferedImage imageInBuffer) throws IOException {
        File bigFile = new File(FoldersOnDisk.USER_IMAGES + directory + "/" + fileName + ".png");
        BufferedImage reshapedBigImage = reshapeImage(imageInBuffer,
                BIGIMAGE_SIZE_WIDTH, BIGIMAGE_SIZE_HEIGHT);
        saveToDisk(reshapedBigImage, bigFile);
    }

    /**
     * Transform the buffered image into small image (25px * 25px )
     * @param imageInBuffer
     * @throws IOException
     */
    private void doSaveMini(BufferedImage imageInBuffer) throws IOException {
        File miniFile = new File(FoldersOnDisk.USER_IMAGES + directory + "/" + fileName + "_small" + ".png");

        BufferedImage reshapedSmallImage = reshapeImage(imageInBuffer,
                SMALLIMAGE_SIZE_WIDTH, SMALLIMAGE_SIZE_HEIGHT);
        saveToDisk(reshapedSmallImage, miniFile);
    }

    /**
     * Saves the images on the disk as a PNG
     * @param buffIm
     * @param fileToSave
     * @throws IOException
     */
    private void saveToDisk(BufferedImage buffIm, File fileToSave) throws IOException {
        ImageIO.write(buffIm, "png", fileToSave);
    }

    /**
     * Added .png at the end since it is not decided until the end
     * @return the filename of the big image
     */
    public String getFileName() {
        return fileName + ".png";
    }
}
