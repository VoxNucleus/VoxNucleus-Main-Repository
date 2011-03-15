package me.FallingDownLib.CommonClasses.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides an easy way to get rid of any attempt to inject code via <script>
 * or any other <>
 * @author victork
 */
public class ScriptDestroyer {

    StringBuffer purgedText;
    Pattern scriptPattern;

    /**
     * Constructor.
     * @param text
     */

    public ScriptDestroyer(String text){
        scriptPattern = Pattern.compile("(<.*>.*<.*>)|(<.*>)", Pattern.CASE_INSENSITIVE);
        launchEviction(text);
    }

    /**
     * Private : search threw the String for patterns
     * @param text
     */

    private void launchEviction(String text){
        Matcher matcherAntiScript = scriptPattern.matcher(text);
        purgedText = new StringBuffer();
        while(matcherAntiScript.find()){
            matcherAntiScript.appendReplacement(purgedText, "");
        }
        matcherAntiScript.appendTail(purgedText);
        
    }

    /**
     * Methods which enables to set new text without creating a new class.
     * @param text
     */

    public void setNewText(String text){
        launchEviction(text);
    }

    /**
     * 
     * @return the text after all scripts are removed
     */

    public String getPurgedTextBack(){
        return purgedText.toString();
    }

}
