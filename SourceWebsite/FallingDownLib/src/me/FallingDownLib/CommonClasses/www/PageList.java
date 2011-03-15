package me.FallingDownLib.CommonClasses.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PageList implements ToCodeConverter {


    private StringBuilder list_builder;

    //
    private String page;
    private int current_page;
    private int nb_pages_to_link=7;
    private int max_pages;

    /**
     * Default constructor (always called)
     */

    protected PageList(){
        list_builder = new StringBuilder();
    }

    /**
     * real constructor
     * @param in_page
     * @param in_current_page
     * @param in_nb_pages_to_link
     * @param in_max_pages
     */

    protected PageList(String in_page, int in_current_page,
            int in_nb_pages_to_link, int in_max_pages){
        this();
        page=in_page;
        current_page = in_current_page;
        nb_pages_to_link=in_nb_pages_to_link;
        max_pages=in_max_pages;
        
    }

    /**
     *
     * @param in_page
     * @param in_current_page
     * @param in_nb_pages_to_link
     * @param in_max_pages
     * @return an instance of the object
     */

    public static PageList getInstance(String in_page, int in_current_page,
            int in_nb_pages_to_link, int in_max_pages){
        return new PageList(in_page,  in_current_page,
            in_nb_pages_to_link, in_max_pages);
    }

    /**
     * 
     * @return the html code of the list
     */

    public String getHTMLCode() {
        list_builder.append("<div id=\"list_pages\">");
        list_builder.append("<ul class=\"list\">");
        buildPageList();
        list_builder.append("</ul>");
        list_builder.append("</div>");
        return list_builder.toString();
    }

    /**
     * build the list
     */

    private void buildPageList() {
        if (current_page <= nb_pages_to_link / 2) {
            int min = 1;
            int max = Math.min(max_pages, nb_pages_to_link);
            insertLIList(min, max);
        } else if (current_page < max_pages - nb_pages_to_link/2) {
            int min = current_page - nb_pages_to_link / 2;
            int max = current_page + nb_pages_to_link / 2;
            insertLIList(min, max);
        } else {
            int min = max_pages - nb_pages_to_link;
            int max = max_pages;
            insertLIList(Math.max(1, min), max);

        }
    }

    /**
     * Build li list
     * @param begin
     * @param end
     */

    private void insertLIList(int begin,int end){
        for(int index=begin;index<end+1;index++){
            insertLIelement(index);
        }
        
    }

    /**
     * insert li element
     * @param index
     */
    private void insertLIelement(int index){
        list_builder.append("<li><a href=\"").append(page).append(index).append("\">");
        list_builder.append(index).append("</a></li>");
    }

}
