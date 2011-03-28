package databasesweeper.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author victork
 */
public class SweeperFormatter extends Formatter {

    public void setName(){
        
    }

    @Override
    public String format(LogRecord record) {
        StringBuffer buf = new StringBuffer(1000);
        buf.append(new java.util.Date());
        buf.append(' ');
        buf.append(record.getLevel());
        buf.append(' ');
        buf.append(formatMessage(record));
        buf.append("\n");
        return buf.toString();
    }

}
