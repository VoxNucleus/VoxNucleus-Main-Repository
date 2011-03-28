package FallingDown.cassandracrawler.logger;


import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 *
 * @author victork
 */
public class CrawlerFormatter extends Formatter {


    @Override
    public String format(LogRecord record) {
        StringBuilder buf = new StringBuilder(1000);
        buf.append(new java.util.Date());
        buf.append(' ');
        buf.append(record.getLevel());
        buf.append(' ');
        buf.append(formatMessage(record));
        buf.append("\n");
        return buf.toString();
    }

}
