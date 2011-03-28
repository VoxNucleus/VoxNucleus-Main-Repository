package FallingDown.user.modify;

import FallingDown.user.login.Identification;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.FallingDownLib.CassandraConnection.connectors.UserConnector;
import me.FallingDownLib.CassandraConnection.util.ColumnUtil;
import me.FallingDownLib.CommonClasses.Exceptions.FileIsNotAnImage;
import me.FallingDownLib.CommonClasses.Exceptions.FileToBigException;
import me.FallingDownLib.CommonClasses.Exceptions.IncorrectUserInfo;
import me.FallingDownLib.CommonClasses.UserFields;
import me.FallingDownLib.CommonClasses.identification.PasswordChanger;
import me.FallingDownLib.CommonClasses.util.FieldVerificator;
import me.FallingDownLib.CommonClasses.util.ScriptDestroyer;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.thrift.TException;

/**
 * This class verifies the validity and save the modifcations
 * @author victork
 */
public class UserModificator {

    private HttpServletRequest associatedRequest;
    private HttpServletResponse associatedResponse;
    HashMap<String, String> modifyHash;

    private SaveImageUser imageToDisk;
    private String associatedUser;
    private String firstName;
    private String lastName;
    private String oldPassword;
    private String newPassword;
    private String newPassword_confirm;
    private String description;
    private String center_of_interest;
    private String website;
    private String email;
    private String email_confirm;
    private String language;
    private String birthDate;
    private HashMap<String, String> formFields;

    /**
     *
     * @param user
     * @param request
     * @param response
     */
    private UserModificator(String user, HttpServletRequest request, HttpServletResponse response) {
        associatedRequest = request;
        associatedResponse = response;
        associatedUser = user;
    }

    /**
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    public static UserModificator getInstance(String user, HttpServletRequest request, HttpServletResponse response) {
        return new UserModificator(user, request, response);
    }

    /**
     * Extract informations from the <form> and put it in map and SaveImage
     * @param request
     * @param formFields
     * @param imageToDisk
     * @throws FileUploadException
     * @throws UnsupportedEncodingException
     */
    private void processRequest(HttpServletRequest request) throws FileUploadException,
            UnsupportedEncodingException, FileToBigException, IOException, FileIsNotAnImage {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items = upload.parseRequest(request);
        formFields = new HashMap<String, String>();
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            String name = item.getFieldName();
            if (item.isFormField()) {
                formFields.put(name, item.getString("UTF-8"));
            } else {
                if (item.getSize() > 0) {
                    imageToDisk = new SaveImageUser(item);
                }
            }
        }
    }


    /**
     *
     */
    private void getParameters() {
        language = replaceNull(formFields.get(UserFields.HTTP_LANGUAGE));
        email = replaceNull(formFields.get(UserFields.HTTP_EMAIL));
        email_confirm = replaceNull(formFields.get(UserFields.HTTP_EMAIL_CONFIRM));
        firstName = replaceNull(formFields.get(UserFields.HTTP_FIRSTNAME));
        lastName = replaceNull(formFields.get(UserFields.HTTP_LASTNAME));
        birthDate = replaceNull(formFields.get(UserFields.HTTP_BIRTHDATE));
        website = replaceNull(formFields.get(UserFields.HTTP_SITE_WEB));
        center_of_interest = replaceNull(formFields.get(UserFields.HTTP_CENTER_INTERESTS));
        description = replaceNull(formFields.get(UserFields.HTTP_DESCRIPTION));
        oldPassword = replaceNull(formFields.get(UserFields.HTTP_OLD_PASSWORD));
        newPassword = replaceNull(formFields.get(UserFields.HTTP_PASSWORD));
        newPassword_confirm = replaceNull(formFields.get(UserFields.HTTP_PASSWORD_CONFIRM));
    }

    /**
     *  Validate and save Informations
     * @throws IncorrectUserInfo
     */
    private void validate() throws IncorrectUserInfo {
        modifyHash = new HashMap<String, String>();
        addToInformationToSave(UserFields.DB_LANGUAGE, language, FieldVerificator.user_verify_language(language));
        addToInformationToSave(UserFields.DB_EMAIL, email, FieldVerificator.user_verify_email(email, email_confirm));
        addToInformationToSave(UserFields.DB_FIRSTNAME, firstName, FieldVerificator.user_verify_firstname(firstName));
        addToInformationToSave(UserFields.DB_NAME, lastName, FieldVerificator.user_verify_lastname(lastName));
        addToInformationToSave(UserFields.DB_DATE_BIRTH, birthDate, FieldVerificator.user_verify_birthdate(birthDate));
        addToInformationToSave(UserFields.DB_SITE_WEB, website, FieldVerificator.user_verify_website(website));
        addToInformationToSave(UserFields.DB_CENTER_INTERESTS, center_of_interest, FieldVerificator.user_verify_center_of_interest(center_of_interest));
        ScriptDestroyer script_clean = new ScriptDestroyer(description);
        String purgedText=script_clean.getPurgedTextBack();
        addToInformationToSave(UserFields.DB_DESCRIPTION, purgedText, FieldVerificator.user_verify_description(description));
        if (imageToDisk != null) {
            modifyHash.put(UserFields.DB_FILENAME, imageToDisk.getFileName());
        }
    }

    /**
     * Insert a value if it is correct
     * @param field
     * @param value
     * @param isValid
     */
    private void addToInformationToSave(String field, String value, boolean isValid){
        modifyHash.put(field, value);
    }

    /**
     *
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */
    private void saveToCassandra() throws IllegalArgumentException, NotFoundException,
            TException, IllegalStateException, PoolExhaustedException, Exception {
        ArrayList<Column> listCol = ColumnUtil.HashStringToArrayCol(modifyHash);
        UserConnector uConnector = UserConnector.getInstance(associatedUser);
        uConnector.batchSetField(listCol);
    }

    /**
     * 
     * @throws IncorrectUserInfo
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws TException
     * @throws IllegalStateException
     * @throws PoolExhaustedException
     * @throws Exception
     */

    public void saveModifications() throws IncorrectUserInfo, IllegalArgumentException,
            NotFoundException, TException, IllegalStateException,FileIsNotAnImage, PoolExhaustedException,
            Exception {
        processRequest(associatedRequest);
        getParameters();
        validate();
        if(!oldPassword.isEmpty()){
            verifyPassword();
        }
        if(imageToDisk!=null){
            imageToDisk.doSave(associatedUser);
        }
        saveToCassandra();
    }

    /**
     * If user wants to set a new password
     */
    private boolean verifyPassword() throws IncorrectUserInfo {
        Identification identif = new Identification(associatedUser,
                oldPassword);
        PasswordChanger passChange = PasswordChanger.getChanger(associatedUser,
                newPassword);
        if (identif.isValid()) {
            if (FieldVerificator.user_verify_password(newPassword, newPassword_confirm)) {
                passChange.doChange();
            }
        }
        return passChange.getIsChangeSuccessful();
    }

    /**
     *
     * @param in
     * @return empty string instead of null
     */
    private String replaceNull(String in){
        if(in==null)
            return "";
        else
            return in;

    }
}
