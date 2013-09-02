/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistance;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.JDBC;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import java.sql.SQLException;
import util.Constants;

/**
 *
 * @author Boban
 */
public class SDBDataProvider {

    private Store store;
    private StoreDesc storeDesc;
    private SDBConnection conn;
    private boolean format = false;

    public SDBDataProvider() {
        storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);
        JDBC.loadDriverMySQL();
        // get the connection
        establishConnection();
    }

    public Model getDataModel() {
        Model dataModel = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (format) {
                System.out.println("Formatting db tables...");
                // create the necessary tables - cleans the db
                store.getTableFormatter().create();
            }
            // get the data model
            establishConnection();
            System.out.println("Connecting default data model...");
            dataModel = SDBFactory.connectDefaultModel(store);
            System.out.println("Connection to the default data model established!");
        } catch (Exception e) {
            System.err.println("Could not load DataModel from database!");
        }
        return dataModel;
    }

    public void flushDataModel(Model dataModel) {
        store.close();
        conn.close();
    }

    private void establishConnection() {
        try {
            conn = new SDBConnection(Constants.DB_URL, Constants.DB_USRNAME, Constants.DB_PASS);
            System.out.println("Got SQL connection (is closed? : " + conn.getSqlConnection().isClosed() + ")");

            store = SDBFactory.connectStore(conn, storeDesc);
        } catch (SQLException e) {
            System.err.println("Could not get JDBC Connection for SDB store");
        }
    }
}
