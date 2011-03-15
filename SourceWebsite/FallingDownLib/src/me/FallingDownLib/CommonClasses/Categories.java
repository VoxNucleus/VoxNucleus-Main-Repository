package me.FallingDownLib.CommonClasses;

/**
 * List all categories and subcategories of the website.
 * @author victork
 */
public class Categories {

    public static final int CATEGORY_GENERAL = 0;
    public static final int CATEGORY_TECHNOLOGY = 1;
    public static final int CATEGORY_NEWS = 2;
    public static final int CATEGORY_SCIENCE = 3;
    public static final int CATEGORY_SPORT = 4;
    public static final int CATEGORY_OPINION = 5;
    public static final int CATEGORY_RANDOM = 6;

    public static final String[] MAIN_CATEGORIES={"Tout","Technologie","Actualités", "Sciences", "Sport","Opinion","Autres"};


    public static final String[] MAIN_CATEGORIES_EXPLANATIONS ={
        "",
        "Vous trouverez des informations relatives aux technologies",
        "Suivez les meilleurs actualités",
        "",
        "Tous les sports, 24 heures sur 24",
        "Les opinions ",
        ""};

    /**
     *
     * @return select form to choose category
     */

    public static String getSELECT_HTML(){
        StringBuilder builder = new StringBuilder();
        builder.append("<select id=\"category_select\" required=\"required\" name=\"categories\">");
        builder.append("<option value=\"\">Choisissez...</option>");
        for(int index=1;index<MAIN_CATEGORIES.length;index++){
            builder.append("<option value=\"").append(MAIN_CATEGORIES[index]).append("\">").append(MAIN_CATEGORIES[index]).append("</option>");
        }
        builder.append("</select>");
        return builder.toString();
    }

    /**
     * 
     * @return the category number if it exists in the list, otherwise it returns a negative number.
     */

    public static int getCategory(String category){
        for(int index=0;index<MAIN_CATEGORIES.length;index++){
            if(MAIN_CATEGORIES[index].equals(category))
                return index;
        }
        return -1;
    }
}
