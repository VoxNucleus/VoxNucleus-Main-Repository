package me.FallingDownLib.CommonClasses.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * list of static methods
 * @author victork
 */
public class MyPattern {

    public static final String NEW_LINE = "\\n";
    public static final String NOT_ALPHANUMERICAL = "\\W";
    public static final String BIRTHDATE = "[\\p{Digit}]{2}/[\\p{Digit}]{2}/[\\p{Digit}]{4}";
    public static final String POST_DATE_PATTERN = "'Le' dd'/'MM'/'yy 'à' kk':'mm 'par '";
    public static final String COMMENT_DATE_PATTERN = "dd/MM/yy 'à' kk:mm 'et' ss 'secondes'";
    public static final String HTTP_LINK_PATTERN = "\\Ahttp\\://";
    public static final String TAG_DELIMITER = "[\\s,']";
    public static final String DOUBLE_DOT_REGEX="\\.\\.";

    public static final String HTTP_LINK_SHORTENER="\\Ahttp\\://[^/]*";

    /**
     *
     * @param in
     * @return text with new lines
     */

    public static String filterNewLine(String in) {
        Pattern pattern_NewLine = Pattern.compile(MyPattern.NEW_LINE);
        Matcher newLineMatcher = pattern_NewLine.matcher(in);
        StringBuffer purgedText = new StringBuffer();
        while (newLineMatcher.find()) {
            newLineMatcher.appendReplacement(purgedText, "<br>");
        }
        newLineMatcher.appendTail(purgedText);
        return purgedText.toString();
    }


}
