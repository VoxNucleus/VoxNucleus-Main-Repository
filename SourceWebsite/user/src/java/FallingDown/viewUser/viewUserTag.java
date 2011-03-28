/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FallingDown.viewUser;

import me.FallingDownLib.CommonClasses.User;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.cassandra.thrift.NotFoundException;

/**
 *
 * @author victork
 */
public class viewUserTag extends SimpleTagSupport {

    String userId;
    String sessionId;
    String username;


    public void setIdUser(String userId){
        this.userId=userId;
    }


    @Override
    public void doTag() throws IOException{
        try{
            HashMap<String,String> mapUser = User.getUserFromDatabase(userId);
            displayUserInformation(mapUser);
        }catch(NotFoundException notFound){
            displayNotFound();
        }catch(Exception e){
            unknownException();
        }
    }



    private void displayNotFound() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.print("La personne que vous cherchez n'a pas l'air d'exister.");
    }

    private void unknownException() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.print("Une erreur inconnue s'est produite.");
        //TODO Call Logger in order to log this error
    }

    private void displayUserInformation(HashMap<String,String> mapUser) throws IOException {
        JspWriter out = getJspContext().getOut();
        out.print("Nom d'utilisateur :"+ mapUser.get("username") + "<BR>");
        out.print("Date de naissance :"+ mapUser.get("dateBirth"));
        out.print("Langue :" + mapUser.get("language"));
    }
}
