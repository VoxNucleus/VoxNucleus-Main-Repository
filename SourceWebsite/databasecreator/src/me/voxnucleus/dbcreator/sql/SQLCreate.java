package me.voxnucleus.dbcreator.sql;

/**
 * Create tables at the moment
 * Maybe more later
 * @author victork
 */
public class SQLCreate {

    protected SQLCreate() {
    }

    public static SQLCreate getInstance() {
        return new SQLCreate();
    }

    public void create() {
        SQLTableCreate table_create = SQLTableCreate.getInstance();
        table_create.create();
    }

    private void grantPermission(){
        
    }
}
