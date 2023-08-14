import java.sql.Connection;
import java.util.ArrayList;

//This class is used to represent and connect to the SQLite database which will store all permanent information.
public class StorageDatabase {

    //Stores the connection to the database
    private final Connection jdbcConn;

    //Instantiates the object and connects to the SQLite database located at the provided path
    public StorageDatabase(String path) {

    }

    //Loads the user’s configuration from the database
    public UserConfigUnit loadUserCfg() {

    }

    //Saves the provided configuration into the database, overwriting the existing content
    public void saveUserCfg(UserConfigUnit cfg) {

    }

    //Loads all relay associations saved in the database
    public ArrayList<RelayAssociation> loadAllRelayAssociations() {

    }

    //Loads a specific relay association saved in the database under the provided primary key
    public RelayAssociation loadRelayAssociation(int primaryKey) {

    }

    //Creates a database entry or updates the existing database entry for the provided relay association
    public void saveRelayAssociation(RelayAssociation ra) {

    }
}
