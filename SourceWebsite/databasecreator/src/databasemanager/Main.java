/*
 * This program helps create base databases for Cassandra and PostGreSQL
 */

package databasemanager;

import databasemanager.cassandra2sql.CassandraToSQL;
import databasemanager.destroy.SQLTableDrop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.voxnucleus.dbcreator.sql.SQLCreate;
import me.voxnucleus.sql.delete.NucleusDELETEOperations;
/**
 *
 * @author victork
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        choseAction();
        
    }

    private static int getChoice(){
        String choice;
        System.out.println("1. Destroy all table (used by VoxNucleus)");
        System.out.println("2. Create tables (used by VoxNucleus)");
        System.out.println("3. Cassandra to SQL");
        System.out.println("4. Simple tests");
        System.out.println("5. Statistics\n");
        System.out.println("0. Exit database manager\n");
        System.out.println("Enter your choice...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            choice = br.readLine();
        }catch(IOException ex){
            choice = "";
        }
        return Integer.parseInt(choice);
    }

    private static void choseAction(){
        switch(getChoice()){
            case 0:
                System.exit(0);
                break;
            case 1:
                dropTable();
                break;
            case 2:
                createTable();
                break;
            case 3:
                cassandra2SQL();
                break;
            case 4:
                tests();
                break;
            case 5:
                displayStatistics();
                break;
            default:
                System.out.println("Unrecognized character please do it again.");
        }
        choseAction();
    }

    private static void dropTable() {
        SQLTableDrop sqldrop = SQLTableDrop.getInstance();
        sqldrop.drop();
    }

    private static void createTable() {
        SQLCreate sqlcreate = SQLCreate.getInstance();
        sqlcreate.create();
        choseAction();
    }

    private static void cassandra2SQL() {
        CassandraToSQL cass2sql = new CassandraToSQL();
        cass2sql.convert();
    }

    private static void tests() {
        try {
            
            NucleusDELETEOperations.deletePost("101103test");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void displayStatistics() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
