package me.FallingDownLib.CommonClasses.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * This class
 * @author victork
 */
public class Metadata implements ToCodeConverter {

    public static final String DEFAULT_VOX_NUCLEUS_GENERAL_DESCRIPTION="VoxNucleus est une communauté de"
            + " partage de liens où chacun peut poster et voter. Rejoignez-nous sans plus tarder !";
    public static final String DEFAULT_REVISIT_AFTER="1 day";
    public static final String DEFAULT_LANGUAGE="french";
    public static final String DEFAULT_ROBOTS_ORDERS="index,follow";
    public static final String MANDATORY_KEYWORDS="VoxNucleus,vote, actualités, news";

    private String contenttype;
    private String revisitAfter;
    private String description;
    private String language;
    private String robots_orders;
    private String additional_keywords;

    private StringBuilder meta_builder;

    private Metadata(){
        meta_builder= new StringBuilder();
    }

    /**
     * 
     * @param descrip
     * @param visitAfter
     * @param robot
     * @param more_keywords
     */
    private Metadata(String descrip,String visitAfter,String robot,String more_keywords){
        this();
        language=DEFAULT_LANGUAGE;
        contenttype="UTF-8";
        revisitAfter = visitAfter;
        description=descrip;
        robots_orders=robot;
        additional_keywords=more_keywords;
    }

    /**
     *
     * @return instance with default arguments
     */
    public static Metadata getInstance(){
        return new Metadata(DEFAULT_VOX_NUCLEUS_GENERAL_DESCRIPTION,DEFAULT_REVISIT_AFTER,DEFAULT_ROBOTS_ORDERS,"");

    }

    private void buildContentType(){
        meta_builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html charset=UTF-8\">");
    }

    /**
     * set the visit after factor
     */
    private void buildVisitAfter(){
        meta_builder.append("<meta name=\"revisit-after\" content=\"").append(revisitAfter).append("\">");
    }

    /**
     *
     */
    private void buildMetaDescription(){
        meta_builder.append("<meta name=\"description\" content=\" ").append(description).append("\">");
    }

    /**
     *
     */

    private void buildMetaLanguage() {
        meta_builder.append("<meta name=\"language\" content=\"").append(language).append("\">");

    }

    /**
     * build the meta for robot
     */
    private void buildMetaRobots() {
        meta_builder.append("<meta name=\"robots\" content=\" ").append(robots_orders).append("\">");

    }

    /**
     * set the keywords metadata
     */
    private void buildMetaKeywords() {
        meta_builder.append("<meta name=\"keywords\" content=\"" + MANDATORY_KEYWORDS);
        if (!additional_keywords.isEmpty()) {
            meta_builder.append(",").append(additional_keywords);
        }
        meta_builder.append("\">");
    }

    /**
     * 
     * @param more_keywords
     */

    public void setAdditionnalKeywords(String more_keywords){
        this.additional_keywords=more_keywords;
        
    }

    public void setDescription(String inDescription){
        description=inDescription;
    }


    private void buildMetadata(){
        buildMetaDescription();
        buildMetaRobots();
        buildMetaLanguage();
        buildVisitAfter();
        buildContentType();
        buildMetaKeywords();
    }




    public String getHTMLCode() {
        buildMetadata();
        return meta_builder.toString();
    }






}
