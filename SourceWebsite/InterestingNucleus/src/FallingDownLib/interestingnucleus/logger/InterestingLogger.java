package FallingDownLib.interestingnucleus.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


/**
 *
 * @author victork
 */
public class InterestingLogger {


    private static int limit = 1000000; // 1 Mb limit
    private static int numLogFiles = 3;
    private static boolean append = true;
    public static Logger errorLogger=null;
    public static Logger infoLogger=null;



    protected InterestingLogger() {
        if (errorLogger == null || infoLogger == null) {
            try {
                FileHandler fileErrorHandler = new FileHandler("errorLog%g.log", limit, numLogFiles, append);
                fileErrorHandler.setFormatter(new InterestingFormatter());
                FileHandler fileInfoHandler = new FileHandler("infoLog%g.log", limit, numLogFiles, append);
                fileInfoHandler.setFormatter(new InterestingFormatter());
                errorLogger=Logger.getLogger("errorLoggerSweeper");
                errorLogger.addHandler(fileErrorHandler);
                infoLogger=Logger.getLogger("infoLoggerSweeper");
                infoLogger.addHandler(fileInfoHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *
     * @return Logger
     */

    public static InterestingLogger getInstance() {
        return new InterestingLogger();
    }

    /**
     *
     * @param error
     */
    public void insertInfo(String error){
        infoLogger.info(error);
    }

    /**
     *
     * @param info
     */
    public void insertError(String info){
        errorLogger.severe(info);
    }

}
