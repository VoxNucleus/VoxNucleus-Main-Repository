package me.FallingDownLib.CommonClasses.www;

import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * This class is the new 
 * @author victork
 */
public class CategoriesMenu2 implements ToCodeConverter {

    private String category;
    private String sub_category;
    private String filter;
    private int filter_number;

    private boolean isSelected = false;
    private StringBuilder menu_builder;
    


    public static final String[] TF_LIST={"none","latest","interesting",
    "Best24Hours","Best1Week","Best1Month","Best1Year","Archives"};
    public static final int TF_NONE=0;
    public static final int TF_LATEST=1;
    public static final int TF_INTERESTING=2;
    public static final int TF_BEST24HOURS=3;
    public static final int TF_BEST1WEEK=4;
    public static final int TF_BEST1MONTH=5;
    public static final int TF_BEST1YEAR=6;
    public static final int TF_ARCHIVES=7;

    public CategoriesMenu2() {
        category = "Tout";
        sub_category = "Tout";
        filter = "none";
        menu_builder = new StringBuilder();
    }

    public CategoriesMenu2(String category, String sub_category, String filter) {
        this.category = category;
        this.sub_category = sub_category;
        this.filter = filter;
        isSelected = true;
        menu_builder = new StringBuilder();
    }

    /**
     *
     * @return Code for the categories menu
     */
    private void getCategoriesMenu() {
        
        int selected_category = Categories.getCategory(category);
        menu_builder.append("<div class=\"categories\">");
        menu_builder.append(SiteDecorations.getBigLogo());
        for (int index = 0; index < Categories.MAIN_CATEGORIES.length; index++) {

            if (index == selected_category && isSelected) {
                menu_builder.append("<div class=\"category_select selected\"  >");
                menu_builder.append(getInsideOfCategorySelect(index, true));
                menu_builder.append("</div>");
            } else {
                menu_builder.append("<div class=\"category_select\" >");
                menu_builder.append(getInsideOfCategorySelect(index, false));
                menu_builder.append("</div>");
            }
        }
        createFilterMenu();
        menu_builder.append("</div>");
    }

    /**
     * 
     * @param category_index
     * @param isSelected
     * @return
     */
    private String getInsideOfCategorySelect(int category_index, boolean isSelected) {
        if (SubCategories.SUB_CATEGORIES[category_index].length > 1) {
            return Categories.MAIN_CATEGORIES[category_index] + createSubCategoriesMenu(category_index, isSelected);
        } else {
            return "<a href=\"/interesting/" + Categories.MAIN_CATEGORIES[category_index] + "/Tout/\" >" + Categories.MAIN_CATEGORIES[category_index] + "</a>";
        }
    }

    /**
     *
     * @param category_index
     * @return HTML code for sub_categories
     */
    private String createSubCategoriesMenu(int category_index, boolean isSelected) {
        StringBuilder sub_category_builder = new StringBuilder();
        int selected_index = SubCategories.getSubcategory(category_index, sub_category);
        for (int index = 0; index < SubCategories.SUB_CATEGORIES[category_index].length; index++) {
            if (index == 0) {
                sub_category_builder.append("<ul class=\"sub_categories\"").append("id=\"").append(Categories.MAIN_CATEGORIES[category_index]).append("\">");
            }
            if (isSelected && selected_index == index) {
                sub_category_builder.append("<li class=\"selected\">");
                sub_category_builder.append("<a href=\"/interesting/").append(Categories.MAIN_CATEGORIES[category_index]).append("/").append(SubCategories.SUB_CATEGORIES[category_index][index]).append("\">");
                sub_category_builder.append(SubCategories.SUB_CATEGORIES[category_index][index]).append("</a>");
                sub_category_builder.append("</li>");
            } else {
                sub_category_builder.append("<li>");
                sub_category_builder.append("<a href=\"/interesting/").append(Categories.MAIN_CATEGORIES[category_index]).append("/").append(SubCategories.SUB_CATEGORIES[category_index][index]).append("\">");
                sub_category_builder.append(SubCategories.SUB_CATEGORIES[category_index][index]).append("</a>");
                sub_category_builder.append("</li>");
            }
            if (index == SubCategories.SUB_CATEGORIES[category_index].length - 1) {
                sub_category_builder.append("</ul>");
            }
        }
        return sub_category_builder.toString();
    }

    /**
     * Create the filter menu
     * Need a way to knoz in which category we are
     */

    private void createFilterMenu() {
        getFilterNumber();
        menu_builder.append("<div id=\"category_filter\">");
        menu_builder.append("<ul class=\"filter\">");
        menu_builder.append("<li ").append(getClassSelected(TF_LATEST)).
                append(">").append(buildLatestLink()).append("</li>");
        menu_builder.append("<li ").append(getClassSelected(TF_INTERESTING))
                .append(">").append(buildInterestingLink()).append("</li>");
        menu_builder.append("<li ").append(getClassSelected(TF_BEST24HOURS))
                .append(getClassSelected(TF_BEST1WEEK))
                .append(getClassSelected(TF_BEST1MONTH))
                .append(getClassSelected(TF_BEST1YEAR))
                .append(getClassSelected(TF_ARCHIVES)).append("> <span class=\"text\"> Le meilleur</span>");
        menu_builder.append("<ul class=\"subnav\">");
        menu_builder.append(buildBestLinkList());
        menu_builder.append("</ul>");
        menu_builder.append("</li>");
        menu_builder.append("</ul>");
        menu_builder.append("</div>");
    }

    /**
     *
     * @return code for latest filter
     */

    private String buildLatestLink() {
        StringBuilder latestBuilder = new StringBuilder();
        latestBuilder.append("<a href=\"/lastest/").append(category).append("/").append(sub_category).append("\">");
        latestBuilder.append("<span class=\"text\"> Nouveaux </span>");
        latestBuilder.append("</a>");
        return latestBuilder.toString();
    }

    /**
     *
     * @return code for interesting filter
     */
    private String buildInterestingLink() {
        StringBuilder latestBuilder = new StringBuilder();
        latestBuilder.append("<a href=\"/interesting/").append(category).append("/").append(sub_category).append("\">");
        latestBuilder.append(" <span class=\"text\"> Intéressant </span> ");
        latestBuilder.append("</a>");
        return latestBuilder.toString();
    }

    /**
     *
     * @return code for best filter
     */

    private String buildBestLinkList() {
        StringBuilder bestLinkList = new StringBuilder();
        String beginning = "/best/" + category + "/" + sub_category + "/";
        bestLinkList.append("<li ").append(getClassSelected(TF_BEST24HOURS)).append(" > <a href=\"").append(beginning).append(FallingDownConnector.KEY_POSTS_24H).append("\">");
        bestLinkList.append("Depuis un jour").append("</a> </li>");
        bestLinkList.append("<li ").append(getClassSelected(TF_BEST1WEEK)).append(" > <a href=\"").append(beginning).append(FallingDownConnector.KEY_POSTS_1WEEK).append("\">");
        bestLinkList.append("Depuis une semaine").append("</a> </li>");
        bestLinkList.append("<li ").append(getClassSelected(TF_BEST1MONTH)).append(" > <a href=\"").append(beginning).append(FallingDownConnector.KEY_POSTS_1MONTH).append("\">");
        bestLinkList.append("Depuis un mois").append("</a> </li>");
        bestLinkList.append("<li ").append(getClassSelected(TF_BEST1MONTH)).append("> <a href=\"").append(beginning).append(FallingDownConnector.KEY_POSTS_1YEAR).append("\">");
        bestLinkList.append("Depuis un an").append("</a> </li>");
        return bestLinkList.toString();
    }

    private void getFilterNumber(){
        for (int index = 0; index < TF_LIST.length; index++) {
            if (TF_LIST[index].equals(filter)) {
                filter_number = index;
                break;
            }
        }
    }

    /**
     *
     * @return a class to insert
     */
    private String getClassSelected(int filter_code){
        if(filter_code==this.filter_number)
            return "class=\"selected\"";
        else
            return "";
    }

    public String getHTMLCode() {
        getCategoriesMenu();
        return menu_builder.toString();
    }
}
