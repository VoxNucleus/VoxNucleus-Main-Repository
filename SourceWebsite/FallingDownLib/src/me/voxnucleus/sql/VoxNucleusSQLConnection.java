package me.voxnucleus.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.voxnucleus.configuration.admin.PRIVATE_INFORMATIONS;

/**
 *
 * @author victork
 */
public class VoxNucleusSQLConnection {

    private static final String PSQL_IP="localhost";
    private static final String PSQL_PORT="5432";
    private static final String PSQL_DB_NAME="voxnucleus";


    public static Connection openConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("org.postgresql.Driver").newInstance();
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:postgresql://"+PSQL_IP+":"
                +PSQL_PORT+"/"+PSQL_DB_NAME,
                PRIVATE_INFORMATIONS.PSQL_USER, PRIVATE_INFORMATIONS.PSQL_USER_PASSWORD);
        return connection;
    }

    public static Connection openAdminConnection() throws SQLException,Exception {
        Class.forName("org.postgresql.Driver").newInstance();
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:postgresql://" + PSQL_IP + ":"
                + PSQL_PORT + "/" + PSQL_DB_NAME,
                PRIVATE_INFORMATIONS.PSQL_ADMIN, PRIVATE_INFORMATIONS.PSQL_ADMIN_PASSWORD);
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoxNucleusSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
