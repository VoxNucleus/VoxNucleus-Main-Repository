/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.FallingDownLib.CommonClasses;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author victork
 */
public class SupportedLanguages {

    private static HashMap<String,String> listLanguages=null;

    /**
     * TODO : Add more launguages
     * List all avaible languages
     */

    private static void  constructHash(){
        listLanguages=new HashMap<String,String>();
        listLanguages.put("fr", "Français");
        listLanguages.put("en", "Anglais");
    }

    /**
     * Construct the hashmap in case it's not initialized yet
     */
    public static void  getInstance(){
        if(listLanguages==null)
            constructHash();
    }

    /**
     *
     * @return HashMap containing the avaible languages
     */

    public static HashMap<String,String> getHashLanguages(){

        return listLanguages;
    }

    /**
     *
     * @param preferedLanguage
     * @return a drop down list with the prefered language in first position
     */

    public static String getSELECT_HTML(String preferedLanguage){
        Iterator<String> hashIterator = listLanguages.keySet().iterator();
        StringBuilder select_builder= new StringBuilder("<select name=\"language\">");
        select_builder.append("<option  value=\"").append(preferedLanguage).append("\">").append(listLanguages.get(preferedLanguage)).append(" (Détecté) </option>");
        while(hashIterator.hasNext()){
            String temp = hashIterator.next();
            if(!temp.equals(preferedLanguage))
                select_builder.append("<option value=\"").append(temp).append("\" >").append(listLanguages.get(temp)).append("</option>");
        }

        return select_builder.append("</select>").toString();
    }

    /**
     *
     * @param language
     * @return true if the language is one of the supported language
     */

    public static boolean isSupportedLanguage(String language){
         SupportedLanguages.getInstance();
        if(listLanguages.containsKey(language))
            return true;
        else
            return false;

    }

}
