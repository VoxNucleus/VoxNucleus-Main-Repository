/**
 * This code is published under GPL v3 licence.
 * Feel free to contribute
 * Authors : Victor Kabdebon
 * Don't forget to visit http://www.voxnucleus.fr
 */

package databasemanager.cassandra2sql;

/**
 *
 * @author victork
 */
public class CassandraToSQL {

    public CassandraToSQL(){
        
    }

    public void convert() {
        NucleusCass2SQL.transfer();
        UserCass2SQL.transfer();
    }

}
