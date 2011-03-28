package FallingDown.post.report;

import me.FallingDownLib.CommonClasses.post.PostReportFields;
import me.FallingDownLib.CommonClasses.www.PopUpElements;
import me.FallingDownLib.CommonClasses.www.SiteElements;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintReportPost implements ToCodeConverter {

    private String postId;
    private StringBuilder report_builder;

    private String title;
    private String content;
    private int report_type;


    public static final int REPORT_ERROR_LOGIN=0;
    public static final int REPORT_NORMAL=1;
    public static final int REPORT_SUCCESS=2;


    /**
     *
     * @param pId
     */
    public PrintReportPost(String pId){
        report_builder= new StringBuilder();
        postId=pId;
        
    }

    /**
     *
     * @param pId
     * @param type
     */

    public PrintReportPost(String pId, int type){
        report_builder= new StringBuilder();
        postId=pId;

    }

    public static PrintReportPost getInstance(String pId){
        return new PrintReportPost(pId);
    }


    private void buildReport(){
        report_builder.append("<html>");
        report_builder.append("<head>");
        insertJS();
        insertCSSStyle();
        report_builder.append(SiteElements.getDoctypeHTML());
        report_builder.append("<title>").append(title).append("</title>");

        report_builder.append("</head>");
        report_builder.append("<body>");
        report_builder.append("<div id=\"container\">");
        report_builder.append("<div id=\"content\">");
        report_builder.append(content);
        report_builder.append("</div>");
        report_builder.append("</div>");
        report_builder.append("</body>");
        report_builder.append("</html>");

    }


    private String buildNormalReport(){
        StringBuilder normalReport= new StringBuilder();
        normalReport.append("<div id=\"report_title\"> Vous êtes sur le point "
                + "de signaler du contenu, s.v.p lire ce qui est marqué en dessous </div>");
        normalReport.append("<div id=\"report_text\"> Vous vous engagez à ce "
                + "que le contenu que vous signalez soit effectivement <b>illégal</b>."
                + " Merci de ne pas signaler du contenu qui vous semble simplement"
                + "offensant. Les biais politques, culturels ne seront pas pris "
                + "en compte et votre demande sera rejetée. De plus si des abus"
                + " de ce système sont répétés vous vous exposez à une suppression de"
                + " votre compte.</div>");
        buildReportForm(normalReport);
        return normalReport.toString();
    }

    /**
     * build the report form
     */
    private void buildReportForm(StringBuilder normalReport){
        normalReport.append("<form method=\"POST\">");
        normalReport.append("<textarea rows=\"8\" name=\"").append(PostReportFields.HTTP_TEXTAREA).append("\" ></textarea>");
        normalReport.append("<input type=\"hidden\" name=\"").append(PostReportFields.HTTP_POSTID).append("\" value=\"").append(postId).append("\"></input>");
        normalReport.append("<p align=\"center\"><button type=\"submit\">Valider</button> </p>");
        normalReport.append("</form>");
    }


    private String buildThankyou(){
        StringBuilder thankyou_builder = new StringBuilder();
        thankyou_builder.append("<b>Merci !</b><br>");
        thankyou_builder.append("Nous vous confirmons que votre message a bien été"
                + "communiqué aux modérateurs. Nous étudierons sous peu votre "
                + "requête. Bonséjour sur notre site !");
        return thankyou_builder.toString();
    }

    private String buildNotConnected() {
        StringBuilder thankyou_builder = new StringBuilder();
        thankyou_builder.append("<b>Une erreur est survenue</b><br>");
        thankyou_builder.append("Nous sommes désolés mais pour accéder à cette "
                + "section il est obligatoire de posséder un compte pour signaler"
                + " du contenu. Connectez-vous ( <a href=\"/usermanagement/login.jsp\">"
                + "En cliquant ici </a> ) et rafraichissez cette page pour continuer.");
        return thankyou_builder.toString();
    }


    /**
     * Insert CSS
     */

    private void insertCSSStyle() {
        report_builder.append(PopUpElements.getGeneralCSS());
        report_builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/popup/reportPost.css\" >");
    }

    /**
     * get JS necessary for this
     */
    private void insertJS() {
        report_builder.append(SiteElements.getJqueryScript());
        report_builder.append("<script type=\"text/javascript\" src=\"/jsp/popup/reportPost.js\"></script>");
    }


    public void setReportType(int report_id){
        report_type=report_id;
    }

    private void setTitle(String in){
        title=in;
    }

    private void setContent(String in){
        content=in;
    }

    public String getHTMLCode() {
                switch(report_type){
            case REPORT_ERROR_LOGIN:
                setTitle("Erreur, non connecté");
                setContent(buildNotConnected());
                break;
            case REPORT_NORMAL:
                setTitle("Signaler du contenu");
                setContent(buildNormalReport());
                break;
            case REPORT_SUCCESS:
                setTitle("Merci de votre aide");
                setContent(buildThankyou());
                break;
        }


        buildReport();
        return report_builder.toString();
    }



}
