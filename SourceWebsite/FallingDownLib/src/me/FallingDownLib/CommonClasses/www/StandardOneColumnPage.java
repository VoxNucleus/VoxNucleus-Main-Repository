package me.FallingDownLib.CommonClasses.www;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class StandardOneColumnPage implements ToCodeConverter {

    private ArrayList<String> listScripts;
    private ArrayList<String> listStyle;

    private StringBuilder page_builder;
    private String title;
    private String content;
    private HttpServletRequest request;
    private Pass pass=null;

    /**
     *
     * @param request
     * @return an instance of this object
     */

    public static StandardOneColumnPage getInstance(HttpServletRequest request) {
        return new StandardOneColumnPage(request);
    }

    /**
     * Set a pass
     * @param uPass
     */
    public void attachAuthentifiedPass(Pass uPass){
        pass=uPass;
    }

    /**
     * Standard constructor. It just initialise the StringBuilder & other elements
     * of the page
     */
    private StandardOneColumnPage() {

        listScripts = new ArrayList<String>();
        listStyle = new ArrayList<String>();
        page_builder = new StringBuilder();
    }

    private StandardOneColumnPage(HttpServletRequest request) {
        this();
        this.request=request;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the content of the page
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String getHTMLCode() {
        beginBuild();
        return page_builder.toString();
    }

    private void beginBuild() {
        if (pass == null) {
            pass = Pass.getPass(request);
            pass.launchAuthentifiate();
        }
        page_builder.append(SiteElements.getDoctypeHTML());
        page_builder.append("<html>");
        buildHead();
        buildMenu();
        buildBody();
        buildFooter();
        page_builder.append("</html>");
    }

    private void buildHead() {
        Metadata meta = Metadata.getInstance();
        page_builder.append("<head>");
        page_builder.append(meta.getHTMLCode());
        page_builder.append(SiteElements.getDoctypeHTML());
        page_builder.append(SiteDecorations.setFavIcon());
        setStyle();
         setScript();
        
        page_builder.append("<title>").append(title).append("</title>");
        page_builder.append("</head>");
    }

    /**
     *
     */
    private void setStyle() {
        page_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/general/layout_one_column.css\" >");
        page_builder.append(SiteElements.getCommonCSSStyle());
    }

    /**
     * 
     */
    private void setScript(){
        page_builder.append(SiteElements.getCommonScripts());
        for(int index=0;index< listScripts.size();index++){
            page_builder.append(listScripts.get(index));
        }
    }

    /**
     * 
     * @param style
     */
    public void addStyle(String style){
        listStyle.add(style);
        for(int index=0;index< listStyle.size();index++){
            page_builder.append(listStyle.get(index));
        }
    }

    /**
     *
     * @param script
     */
    public void addScript(String script){
        listScripts.add(script);
    }

    private void buildBody() {
        page_builder.append("<body>");
        page_builder.append("<div id=\"container\">");
        page_builder.append("<div id=\"content\">");
        page_builder.append(content);
        page_builder.append("</div>");
        page_builder.append("</div>");
        page_builder.append(GoogleAnalytics.getAnalyticsCode());
        page_builder.append("</body>");
    }

    private void buildMenu() {
        page_builder.append(SiteElements.displayBasicMenu(pass));
    }
    /**
     * Construct standard footer
     */

    private void buildFooter() {
        page_builder.append(SiteElements.displayFooter(pass));
    }
}
