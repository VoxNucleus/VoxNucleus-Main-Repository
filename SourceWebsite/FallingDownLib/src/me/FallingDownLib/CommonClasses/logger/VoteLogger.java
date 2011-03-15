package me.FallingDownLib.CommonClasses.logger;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 *
 * @author victork
 */
public class VoteLogger {

    private static int limit = 1000000; // 1 Mb limit
    private static int numLogFiles = 3;
    private static boolean append = true;
    public static Logger infoLogger = null;
    private static final String PATH_TO_LOG = "/opt/log/";
    private String logType = "vote";
    protected static  VoteLogger instance=null;
    private long start;
    private long stop;

    /**
     *
     */
    protected VoteLogger() {
        try {
            FileHandler fileInfoHandler = new FileHandler(PATH_TO_LOG
                    + logType + "/" + "infoLog%g.log", limit, numLogFiles, append);
            fileInfoHandler.setFormatter(new InfoFormatter());
            infoLogger = Logger.getLogger("VoteLogger");
            infoLogger.addHandler(fileInfoHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static VoteLogger getVoteLogger() {
        if (instance == null) {
            instance=new VoteLogger();
            return instance;
        } else {
            return instance;
        }
    }

    public void start(){
        start = Calendar.getInstance().getTimeInMillis();
    }

    public void stop(){
        stop = Calendar.getInstance().getTimeInMillis();
        infoLogger.info("Le vote a duré : " +Long.toString(stop-start)+ "ms");
        
    }
}
