package FallingDown.search;

import FallingDown.search.exception.InvalidSearchRequestException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author victork
 */
public class SearchOptions {

    
    private HttpServletRequest request;
    private String searchRequest;

    //Options :
    private int page_number;

    //TODO
    public static SearchOptions getInstance(HttpServletRequest request){
        return new SearchOptions(request);
    }

    protected SearchOptions(HttpServletRequest in_request) {
        request= in_request;
    }

    public void processRequest() throws InvalidSearchRequestException{
        processPageNumber();
        processRequestText();
    }


    public String getSearchRequest(){
       return searchRequest;
    }

    /**
     *
     * @return 1 for page 0 !
     */
    public int getPageNumber(){
        return page_number;
    }


    private void processRequestText() throws InvalidSearchRequestException{

        searchRequest = (request.getParameterValues("q")==null)?"":
            request.getParameterValues("q")[0];
        if (searchRequest.length() < 2) {
            throw new InvalidSearchRequestException("La recherche est trop courte."
                    + " Pour être pertinente la recherche doit faire au moins deux "
                    + "caractères.");
        }
    }


    /**
     * Get the page number of the page requested
     * @throws InvalidSearchRequestException
     */

    private void processPageNumber() throws InvalidSearchRequestException {
        if (request.getParameter("page") != null) {

            try {
                page_number = Integer.parseInt(request.getParameter("page"));
                if (page_number < 0) {
                    throw new InvalidSearchRequestException("La page demandée est"
                            + " négative. Merci de reformuler votre recherche.");
                }
            } catch (NumberFormatException ex) {
                throw new InvalidSearchRequestException("Vous avez demandé la page"
                        + " numéro  "
                        + "\"" + request.getParameter("page") + "\". Cela ne correspond "
                        + " pas à un numéro de page valide. <br>"
                        + "Merci de reformuler");
            }
        } else {
            page_number = 1;
        }
    }




}
