package me.FallingDownLib.CommonClasses.util;

import java.util.HashMap;

/**
 * A path parser takes an array of String for each argument it is able to detect.
 * One must set the a String to be analyzed and get the result
 * @author victork
 */
public class PathParser {

    private String[] arguments;
    private String path;
    private String[] defaultvalues;

    /**
     *
     * @param args
     * @param defaultvalue
     */

    public PathParser(String[] args, String[] defaultvalues){
        arguments = args;
        this.defaultvalues=defaultvalues;
    }

    /**
     *
     * @param in
     */

    public void setPath(String in){
        path=in;
    }

    /**
     *
     * @return get HashMap of arguments
     */

    public HashMap<String,String> getArguments(){
        HashMap<String,String> result = new HashMap<String,String>();
        int founditems= 0;
        if (path != null) {
            String[] arguments_array = path.split("/", arguments.length + 1);
            for (int index = 1; index < arguments_array.length && founditems < arguments.length; index++) {
                if (arguments_array[index].length() > 0) {
                    result.put(arguments[index - 1], arguments_array[index]);
                    founditems++;
                }
            }
        }
        for (int missing_index = founditems; missing_index < arguments.length; missing_index++) {
            result.put(arguments[missing_index], defaultvalues[missing_index]);
        }
        return result;
    }

}
