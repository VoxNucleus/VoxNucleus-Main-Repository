package me.FallingDownLib.CommonClasses.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 *
 * @author victork
 */
public class InfoLogger {

    private static int limit = 1000000; // 1 Mb limit
    private static int numLogFiles = 3;
    private static boolean append = true;
    public static Logger infoLogger = null;
    private static final String PATH_TO_LOG = "/opt/victor/log/";
    private String logType = "";

    /**
     * 
     */
    protected InfoLogger(String loggerName) {
        logType=loggerName;
        if (infoLogger == null) {
            try {
                FileHandler fileInfoHandler = new FileHandler(PATH_TO_LOG+
                        logType+"/"+"infoLog%g.log", limit, numLogFiles, append);
                fileInfoHandler.setFormatter(new InfoFormatter());
                infoLogger = Logger.getLogger(loggerName);
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
    public static InfoLogger getInstance(String loggerName) {
        return new InfoLogger(loggerName);
    }

    /**
     *
     * @param error
     */
    public void insertInfo(String error) {
        infoLogger.info(error);
    }
    
    /**
     * Set the logtype (
     * @param type
     */
    public void setLogType(String type){
        logType=type;
    }
}
