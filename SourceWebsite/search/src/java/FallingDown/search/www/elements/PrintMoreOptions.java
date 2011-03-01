package FallingDown.search.www.elements;

import FallingDown.search.SearchOptions;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * TODO
 * @author victork
 */
public class PrintMoreOptions implements ToCodeConverter{

    private SearchOptions options;

    private StringBuilder more_option_build;

    protected PrintMoreOptions(SearchOptions in_options){
        options=in_options;
        more_option_build = new StringBuilder();
    }

    public static PrintMoreOptions getInstance(SearchOptions in_options){
        return new PrintMoreOptions(in_options);
    }

    public String getHTMLCode() {
        buildMoreOptions();
        return more_option_build.toString();
    }

    private void buildMoreOptions() {
        more_option_build.append("<div id=\"more_search_options\">");
        buildHeadMoreOptions();
        buildCorpseMoreOptions();
        more_option_build.append("</div");
    }

    private void buildHeadMoreOptions() {
        more_option_build.append("<div id=\"head_more_search_options\">");
        more_option_build.append("<span class=\"clickable\">[+] Cliquez pour "
                + "afficher plus d'options de recherche</span>");
        more_option_build.append("</div>");
    }

    private void buildCorpseMoreOptions() {
        more_option_build.append("<div id=\"corpse_more_search_options\">");
        more_option_build.append("Pas encore implémenté");
        more_option_build.append("</div>");

    }

}
