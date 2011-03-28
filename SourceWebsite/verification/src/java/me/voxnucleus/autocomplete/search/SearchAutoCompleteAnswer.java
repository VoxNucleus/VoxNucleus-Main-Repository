package me.voxnucleus.autocomplete.search;

/**
 *
 * @author victork
 */
public class SearchAutoCompleteAnswer {

    private boolean has_result;
    private String[] term_list;

    public SearchAutoCompleteAnswer(String[] list){
        has_result=true;
        term_list = list;
    }

}
