package me.FallingDownLib.CommonClasses.post;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.FallingDownLib.CommonClasses.util.MyPattern;

/**
 * This class is made to output clean and formatted
 * @author victork
 */
public class TagsAnalyser {

    String rawTags;
    ArrayList<String> resultArray;
    private final int MAX_TAGS = 15;

    public TagsAnalyser(String tags) {
        this.rawTags = tags;
        tagsSplitter();
    }

    /**
     * Perform many operations at once :
     * First it cuts the String into parts separated by ","
     * Then it replaces all non alphanumerical characters by "" (so it destroys them. not very good for )
     * Finally it adds it to the ArrayList<String>.
     * 
     */
    private void tagsSplitter() {
        String[] result = rawTags.split(MyPattern.TAG_DELIMITER);
        Pattern forbiddenCharacters = Pattern.compile(MyPattern.NOT_ALPHANUMERICAL);
        int nbTags = 0;
        resultArray = new ArrayList<String>();
        for (int index = 0; (index < result.length) && (nbTags < MAX_TAGS); index++) {
            Matcher matcherForbiddenCharacters = forbiddenCharacters.matcher(result[index].toLowerCase());
            StringBuffer tempbuff = new StringBuffer();
            while (matcherForbiddenCharacters.find()) {
                matcherForbiddenCharacters.appendReplacement(tempbuff, "");
            }
            matcherForbiddenCharacters.appendTail(tempbuff);
            if ((tempbuff.length() > 1) && tempbuff.length() < 15) {
                resultArray.add(tempbuff.toString());
                nbTags++;
            }
        }
    }

    /**
     * Outputs an array of tags from the list 
     * @return
     */
    public ArrayList<String> getListTags() {
        return resultArray;
    }

    /**
     * Outputs a String with the cleaned tags.
     * This is what is going to be inserted
     * @return
     */
    public String getCleanedString() {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < resultArray.size(); index++) {
            result.append(resultArray.get(index));
            if (index != resultArray.size()) {
                result.append(",");
            }
        }
        return result.toString();
    }
}
