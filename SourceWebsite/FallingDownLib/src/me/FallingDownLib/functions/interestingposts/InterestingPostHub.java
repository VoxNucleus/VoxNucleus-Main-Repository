package me.FallingDownLib.functions.interestingposts;

import java.util.HashMap;
import me.FallingDownLib.CommonClasses.Categories;
import me.FallingDownLib.CommonClasses.SubCategories;


/**
 * This object is the hub where the votes are done.
 * @author victork
 */
public class InterestingPostHub{

    private HashMap<String, HashMap<String, InterestingPostList>> categoryMap;
    private static InterestingPostHub instance = null;

    /**
     * Constructor of the class, only called if there is no avaible instances
     */

    protected InterestingPostHub(){
        categoryMap= new HashMap<String,HashMap<String, InterestingPostList>>();
    }

    /**
     *
     * @return an instance of the object
     */

    public static InterestingPostHub getInstance(){
        if(instance==null)
            instance = new InterestingPostHub();
       return instance;

    }


    /**
     *
     * @param category
     * @param sub_category
     * @return list asked
     */

    public InterestingPostList getList(String category, String sub_category){
        InterestingPostList result=null;
        if(categoryMap.containsKey(category)){
            if(!categoryMap.get(category).containsKey(sub_category)){
                result =InterestingPostList.getInstance(category, sub_category);
                categoryMap.get(category).put(sub_category,result );
            }
        }else{
            result = InterestingPostList.getInstance(category, sub_category);
            HashMap<String, InterestingPostList> toInsert = new HashMap<String, InterestingPostList>();
            toInsert.put(sub_category, result);
            categoryMap.put(category, toInsert);
        }
        result = categoryMap.get(category).get(sub_category);
        return result;
    }


    /**
     * Voting system
     * WARNING : for each vote 2 to 3 votes must be done
     * @param category
     * @param sub_category
     */
    public void addVote(String category, String sub_category, String postId) {
        int category_number = Categories.getCategory(category);
        if (!sub_category.equals("none")) {
            int sub_category_number = SubCategories.getSubcategory(category_number, sub_category);
            getList(Categories.MAIN_CATEGORIES[category_number], SubCategories.SUB_CATEGORIES[category_number][sub_category_number]).addVote(postId);
        }
        getList(Categories.MAIN_CATEGORIES[category_number], SubCategories.SUB_CATEGORIES[category_number][0]).addVote(postId);
        getList(Categories.MAIN_CATEGORIES[Categories.CATEGORY_GENERAL], SubCategories.SUB_CATEGORIES[Categories.CATEGORY_GENERAL][0]).addVote(postId);

    }

}
