package me.FallingDownLib.CommonClasses.www.boxes;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.FallingDownConnector;
import me.FallingDownLib.CommonClasses.www.post.PrintMiniListPost;
import me.FallingDownLib.functions.bestbydate.RetrieveBestByDate;
import me.FallingDownLib.interfaces.www.ToCodeConverter;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

/**
 * provide a quick access to the top 7 best posts of the last 24HOurs
 * @author victork
 */
public class BestBox implements ToCodeConverter{

    StringBuilder box_builder;

    /**
     * Private constructor
     */

    private BestBox(){
        box_builder = new StringBuilder();
    }

    /**
     * 
     * @return an instance
     */

    public static BestBox getInstance(){
        return (new BestBox());
    }

    /**
     * Begin building of the box
     */

    private void buildBox(){
        box_builder.append("<div id=\"best_box\" class=\"lateral_box\">");
        box_builder.append("Le meilleur en 24 heures");
        RetrieveBestByDate bestByDate = new RetrieveBestByDate(FallingDownConnector.KEY_POSTS_24H,0,7, "Tout","Tout");
        try {
            ArrayList<String> list = bestByDate.getKeys();
            PrintMiniListPost miniPosts = PrintMiniListPost.getInstance(list);
            box_builder.append(miniPosts.getHTMLCode());
        } catch (InvalidRequestException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnavailableException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimedOutException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PoolExhaustedException ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BestBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        box_builder.append("</div>");
    }

    
    /**
     * 
     * @return HTML code of the mini best box
     */

    public String getHTMLCode() {
        buildBox();
        return box_builder.toString();
    }

}
