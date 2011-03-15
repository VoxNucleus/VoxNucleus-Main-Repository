package me.FallingDownLib.CommonClasses.www.boxes;

import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.SubCategories;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class ExplanationBox implements ToCodeConverter {

    StringBuilder box_builder = new StringBuilder();

    String asked_category;
    String asked_sub_category;

    /**
     * private constructor
     * @param category
     * @param sub_category
     */
    private ExplanationBox(String category, String sub_category){
        asked_category=category;
        asked_sub_category=sub_category;
        box_builder = new StringBuilder();
    }

    /**
     *
     * @param category
     * @param sub_category
     * @return instance of an explanation box
     */

    public static ExplanationBox getInstance(String category, String sub_category){
        return new ExplanationBox(category,sub_category);
    }

    /**
     * build box HTML code
     */

    private void buildBox(){
        box_builder.append("<div id=\"explanation_box\">");
        box_builder.append("<div class=\"explanation_expand\">")
                .append("[+] Cliquez pour des explications").append("</div>");
        box_builder.append("<div id=\"explanation_text\">");
        int catNumber=Categories.getCategory(asked_category);
        box_builder.append(SubCategories.SUB_CATEGORIES_EXPLANATIONS[catNumber]
                [SubCategories.getSubcategory(catNumber, asked_sub_category)]);
        box_builder.append("</div>");
        box_builder.append("<div id=\"twitter_join\">");
        box_builder.append("Notre Twitter :<a target=\"_blank\""
                + " href=\"http://www.twitter.com/VoxNucleus\"><img alt=\"twit_logo\" src=\"/image/website/logo/twitter_logo.png\"></a>");
        box_builder.append("</div>");

                box_builder.append("<div id=\"facebook_join\">");
        box_builder.append("Notre Facebook :<a target=\"_blank\""
                + " href=\"http://www.facebook.com/pages/VoxNucleus/171812056163883\"><img alt=\"facebook_logo\" src=\"/image/website/logo/facebook_logo.png\"></a>");
        box_builder.append("</div>");
        box_builder.append("</div>");
    }


    /**
     *
     * @return html code for the explanation box
     */
    public String getHTMLCode() {
        buildBox();

        return box_builder.toString();
    }


}
