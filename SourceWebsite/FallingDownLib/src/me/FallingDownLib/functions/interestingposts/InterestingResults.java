package me.FallingDownLib.functions.interestingposts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author victork
 */
public class InterestingResults {

    public static HashMap<String, HashMap<String, ArrayList<String>>> interestingLists;
    public static HashMap<String, HashMap<String, Long>> refreshTimestamp;
    private String category;
    private String sub_category;
    private ArrayList<String> selected_List;
    public static final long REFRESH_INTERVAL = 20l * 60l * 1000l;

    /**
     *
     * @param cat
     * @param sub_cat
     * @return
     */
    public static InterestingResults getList(String cat, String sub_cat) {
        return new InterestingResults(cat, sub_cat);
    }

    /**
     *
     */
    private InterestingResults() {
        if (interestingLists == null || refreshTimestamp == null) {
            interestingLists = new HashMap<String, HashMap<String, ArrayList<String>>>();
            refreshTimestamp = new HashMap<String, HashMap<String, Long>>();
        }

    }

    /**
     *
     * @param category
     * @param sub_category
     */
    private InterestingResults(String category, String sub_category) {
        this();
        if (!interestingLists.containsKey(category)) {
            HashMap<String, ArrayList<String>> sub_catHash = new HashMap<String, ArrayList<String>>();
            HashMap<String, Long> sub_catRefresh = new HashMap<String, Long>();
            sub_catRefresh.put(sub_category, Calendar.getInstance().getTimeInMillis());
            selected_List = InterestingPostList.getResultFromCassandra(category, sub_category);
            sub_catHash.put(sub_category, selected_List);
            refreshTimestamp.put(category, sub_catRefresh);
            interestingLists.put(category, sub_catHash);
        } else {
            if (!interestingLists.get(category).containsKey(sub_category)) {
                selected_List = InterestingPostList.getResultFromCassandra(category, sub_category);
                interestingLists.get(category).put(sub_category, selected_List);
                refreshTimestamp.get(category).put(sub_category, Calendar.getInstance().getTimeInMillis());
            } else {
                if (Calendar.getInstance().getTimeInMillis() > refreshTimestamp.get(category).get(sub_category) + REFRESH_INTERVAL) {
                    selected_List = InterestingPostList.getResultFromCassandra(category, sub_category);
                    interestingLists.get(category).put(sub_category, InterestingPostList.getResultFromCassandra(category, sub_category));
                    refreshTimestamp.get(category).put(sub_category, Calendar.getInstance().getTimeInMillis());
                } else {
                    selected_List = interestingLists.get(category).get(sub_category);
                }
            }
        }
    }

    /**
     * Return truncated list, won't
     * @param begin
     * @param end
     * @return truncated list
     */
    public ArrayList<String> getList(int begin, int end) {
        ArrayList<String> result = new ArrayList<String>();
        int begin_index = Math.min(begin, selected_List.size());
        int end_index = Math.min(end, selected_List.size()) - 1;
        for (int index = end_index; index >= begin_index; index--) {
            result.add(selected_List.get(index));
        }
        return result;
    }
}
