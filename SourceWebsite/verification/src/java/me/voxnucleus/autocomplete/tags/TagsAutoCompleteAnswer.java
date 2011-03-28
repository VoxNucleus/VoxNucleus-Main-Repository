package me.voxnucleus.autocomplete.tags;

/**
 * This class will be converted to JSON using gson
 * @author victork
 */
public class TagsAutoCompleteAnswer {

    private boolean has_result;
    public String[] term_list;

    /**
     * Constructor
     * @param result
     */

    public TagsAutoCompleteAnswer(String[] result){
        has_result=true;
        term_list = result;
    }

}
