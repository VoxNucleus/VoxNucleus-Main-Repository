package me.FallingDownLib.CommonClasses.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.FallingDownLib.CommonClasses.CommentFields;


/**
 *
 * @author victork
 */
public class HashFilterComment {

    HashMap<String, String> work;

    private String toFilter[] = {};

    public HashFilterComment(HashMap<String, String> in) {
        work = in;
    }

    /**
     * Filter the HashMap according to the toFilter array.
     */
    private void filter(){
        for(int index=0;index<toFilter.length;index++){
            work.remove(toFilter[index]);
        }
    }

    /**
     * Replace all the new lines
     */

    private void filterNewLine(){
        Pattern pattern_NewLine = Pattern.compile(MyPattern.NEW_LINE);
        Matcher newLineMatcher = pattern_NewLine.matcher(work.get(CommentFields.DB_TEXT));
        StringBuffer purgedText = new StringBuffer();
        while (newLineMatcher.find()) {
            newLineMatcher.appendReplacement(purgedText, "<br>");
        }
        newLineMatcher.appendTail(purgedText);
        work.put(CommentFields.DB_TEXT, purgedText.toString());
    }


    /**
     * Return the result
     * @return HashMap filtered
     */

    public HashMap<String, String> getHashBack() {
        filter();
        filterNewLine();
        return work;
    }

}
