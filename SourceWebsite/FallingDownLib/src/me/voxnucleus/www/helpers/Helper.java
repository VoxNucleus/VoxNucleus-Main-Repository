package me.voxnucleus.www.helpers;

import java.util.HashMap;

/**
 * Basic helper class. It is just a hashMap of string pointing to string.
 * Each topic is a key and the String contained by this key is the help
 * This class must be extended
 * @author victork
 */
public class Helper {

    private HashMap<String,String> help_text;

    /**
     * 
     */
    protected Helper(){
        help_text= new HashMap<String,String>();
    }

    public void addHelp(String topic,String help_content){
        help_text.put(topic, help_content);
    }

    /**
     * 
     * @return helper. It is never used
     */

    public static Helper getHelper(){
        return new Helper();
    }

    /**
     *
     * @param topic
     * @return HTML code for the explanation + tooltip trigger
     */
    public String getHelpTooltip(String topic){
        StringBuilder help_builder = new StringBuilder();

        if (help_text.containsKey(topic)) {
            help_builder.append("<span style=\"display:none;margin-left:10px\" "
                    + "class=\"get_help\">"
                    + "<img src=\"/image/website/help/help_trigger.png\" "
                    + "alt=\" ? \"></span>");
            help_builder.append("<div style=\"display:none;\" class=\"tooltip\">");
            help_builder.append(help_text.get(topic));
            help_builder.append("</div>");
        }
        return help_builder.toString();
    }

}
