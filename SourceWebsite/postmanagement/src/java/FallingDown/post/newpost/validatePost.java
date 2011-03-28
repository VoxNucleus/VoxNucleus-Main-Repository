package FallingDown.post.newpost;

import me.FallingDownLib.CommonClasses.Exceptions.FileToBigException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CommonClasses.Exceptions.FileIsNotAnImage;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectPostInfo;
import me.FallingDownLib.CommonClasses.Exceptions.PostAlreadyExist;
import me.FallingDownLib.CommonClasses.Exceptions.UserSuspended;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostFields;
import me.FallingDownLib.CommonClasses.identification.Pass;
import me.FallingDownLib.CommonClasses.www.Browser;
import me.FallingDownLib.CommonClasses.www.StandardOneColumnPage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/**
 *
 * @author victork
 */
public class validatePost extends HttpServlet {

    HashMap<String, String> formFields = null;
    SaveImage imageToDisk = null;

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Pass createPostPass = Pass.getPass(request);
        createPostPass.launchAuthentifiate();


        //Checking the validity of the Post && if it is multipart
        if (createPostPass.getIsAuthentified() && ServletFileUpload.isMultipartContent(request)) {
            try {
                verifyUsernameSuspended(createPostPass.getUsername());
                processForms(request);
                Post newPost = new Post(formFields, createPostPass.getUsername());
                newPost.validatePost();
                newPost.setConnexionDetails(request.getRemoteAddr(), request.getRemoteHost());
                imageToDisk.doSave(newPost.key);
                newPost.setImageName(imageToDisk.getFileName());
                try {
                    SavePostToCassandra spost = new SavePostToCassandra(newPost);
                    printPostSuccessful(request,response,newPost.key);
                } catch (PostAlreadyExist e) {
                    printPostAlreadyExist(request, response, e);
                } catch (Exception e) {
                    //TO DO change that
                    PrintWriter out = response.getWriter();
                    out.println(e.toString());
                    out.close();
                }
                //TODO DESTROY THAT
            } catch (FileIsNotAnImage notImageException) {
                printFileIsNotAnImage(request, response, notImageException);
            } catch (UserSuspended ex) {
                printUserSuspended(request,response,ex);
            } catch (FileToBigException toBigException) {
                printFileToBigPage(request, response, toBigException);
            } catch (FileUploadException fException) {
                PrintWriter out = response.getWriter();
                    out.println(fException.toString());
                    out.close();
            } catch (IncorrectPostInfo ex) {
                printIncorrectPostInfo(request, response, ex);
            } catch (UnsupportedEncodingException e) {
                PrintWriter out = response.getWriter();
                out.println(e.toString());
                out.close();

            } catch (IOException ex) {
                PrintWriter out = response.getWriter();
                out.println(ex.toString());
                out.close();
            }
        } //If authentification fails
        else {
            response.sendRedirect("/usermanagement/login.jsp");
        }
    }

    /**
     * Extract informations from the <form> and put it in map and SaveImage
     * @param request
     * @param formFields
     * @param imageToDisk
     * @throws FileUploadException
     * @throws UnsupportedEncodingException
     */
    private void processForms(HttpServletRequest request)
            throws FileUploadException, UnsupportedEncodingException, FileToBigException {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        formFields = new HashMap<String, String>();
        List items = upload.parseRequest(request);
        Iterator iter = items.iterator();
        FileItem imageItem=null;
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            String name = item.getFieldName();
            if (item.isFormField()) {
                formFields.put(name, item.getString("UTF-8"));
            } else {
                imageItem=item;
            }
        }
        imageToDisk = new SaveImage(imageItem,formFields.get(PostFields.HTTP_REMOTE_FILE));
    }

    /**
     *
     * @param request
     * @param response
     * @param e
     * @throws IOException
     */

    private void printPostAlreadyExist(HttpServletRequest request, HttpServletResponse response,PostAlreadyExist e)
            throws IOException {
        StandardOneColumnPage pageFileToBig=StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Erreur, un post avec le même titre existe déjà...");
        pageFileToBig.setContent("Aïe ... Quelqu'un d'autre a posté un noyau avec"
                + "exactement le même titre que vous, ce n'est pas de chance.<br>"
                + "Vous pouvez revenir en arrière et changer votre titre pour"
                + "régler ce problème.<br>"
                + "Nous nous excusons pour cet inconvénient");
        
        Browser.sendResponseToBrowser(request, response, pageFileToBig.getHTMLCode());
    }

    /**
     *
     * @param request
     * @param response
     * @param toBigException
     * @throws IOException
     */

    private void printFileToBigPage(HttpServletRequest request, HttpServletResponse response, FileToBigException toBigException) throws IOException {
         response.setContentType("text/html;charset=UTF-8");
        StandardOneColumnPage pageFileToBig=StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Erreur, image associée trop volumineuse...");
        pageFileToBig.setContent("Nous sommes désolés mais le fichier "
                + "que vous avez tenté d'envoyé est trop volumineux. A cause des"
                + "limitations de nos serveurs la taille maximale admise est de "
                + "3Mb.<br>"
                + "Vous pouvez revenir en arrière et changer l'image (ou la "
                + "compresser)<br>"
                + "Nous nous excusons pour cet inconvénient");
        Browser.sendResponseToBrowser(request, response, pageFileToBig.getHTMLCode());
    }
    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */

    private void printFileUploadErrorPage(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

    }

    /**
     * 
     * @param request
     * @param response
     * @param ex
     * @throws IOException
     */
    private void printIncorrectPostInfo(HttpServletRequest request, HttpServletResponse response, IncorrectPostInfo ex) throws IOException {
                StandardOneColumnPage pageFileToBig=StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Erreur, des erreurs ont été trouvé "
                + "dans les informations données...");
        pageFileToBig.setContent("Nous sommes désolés mais les informations "
                + "que vous avez données semblent semblent erronnées. Le système "
                + "a renvoyé le message suivant :  <br>"
                + "<i>"+ex.toString()+"</i><br>"
                + "Nous nous excusons pour cet inconvénient");
        Browser.sendResponseToBrowser(request, response, pageFileToBig.getHTMLCode());
    }

    private void verifyUsernameSuspended(String user) throws UserSuspended{
        UserConnector uConnector = UserConnector.getInstance(user);
        if(uConnector.getSuspended())
            throw new UserSuspended("Le compte "+user+" est suspendu, veuillez "
                    + "contacter un administrateur pour résoudre ce problème");
    }

    /**
     * A page is sent to browser when a post is successfuly inserted
     * @param request
     * @param response
     * @param key
     * @throws IOException
     */

    private void printPostSuccessful(HttpServletRequest request, HttpServletResponse response, String key) throws IOException {
        StandardOneColumnPage pageFileToBig = StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Création du noyau réussie !");
        pageFileToBig.setContent("Super ! <br>"
                + "La création du nouveau noyau a réussi<br>"
                + "<a href=\"/post/" + key + "\"> Cliquer ici pour voir votre noyau...</a>");
        Browser.sendResponseToBrowser(request, response, pageFileToBig);
    }

    private void printFileIsNotAnImage(HttpServletRequest request, HttpServletResponse response, FileIsNotAnImage notImageException) throws IOException {
        StandardOneColumnPage pageFileToBig = StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Erreur le fichier n'est pas une image !");
        pageFileToBig.setContent(" Une erreur semble être survenue lors de la validation <br>"
                + "Désolé mais l'image \"" + notImageException.getImageName() + "\" "
                + "semble ne pas être une image valide.<br>"
                + "Veuillez vérifier que le fichier que vous avez sélectionné est "
                + "une image.");
        Browser.sendResponseToBrowser(request, response, pageFileToBig);
    }

    private void printUserSuspended(HttpServletRequest request, HttpServletResponse response, UserSuspended ex) throws IOException {
        StandardOneColumnPage pageFileToBig = StandardOneColumnPage.getInstance(request);
        pageFileToBig.setTitle("VoxNucleus : Votre compte est  !");
        pageFileToBig.setContent(" Une erreur semble être survenue lors de la validation <br>"
                + "Désolé mais \"" + ex.toString() + "\" ");
        Browser.sendResponseToBrowser(request, response, pageFileToBig);
    }
}
