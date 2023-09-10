package mel.battchargecontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

//This class is used to represent and connect to the SQLite database which will store all permanent information.
public class StorageDatabase {

    //Stores the connection to the database
    private final Connection jdbcConn;

    //Instantiates the object and connects to the SQLite database located at the provided path
    public StorageDatabase(String path) throws SQLException, IOException {
        //Create the directory for the path if it doesn't already exist
        Files.createDirectories(Paths.get(path).getParent());

        //JDBC url for SQLite database
        String jdbcUrl = "jdbc:sqlite:" + path;

        //Instantiate the JDBC connection
        jdbcConn = DriverManager.getConnection(jdbcUrl);
        if (jdbcConn == null) {
            throw new SQLException("DB Connection object is null");
        }

        //Create the necessary tables if they do not already exist
        String configTableBlueprint = "CREATE TABLE IF NOT EXISTS tblConfig (" +
                "ConfFieldID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FieldName TEXT UNIQUE NOT NULL," +
                "FieldValue TEXT);";
        String configTablePopulation = "INSERT OR IGNORE INTO tblConfig(FieldName) VALUES" +
                "(\"LimitByPercentage\")," +
                "(\"MaxBattPercentage\")";
        String relayTableBlueprint = "CREATE TABLE IF NOT EXISTS tblRelay(" +
                "RelayID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NetworkAddress TEXT NOT NULL," +
                "FriendlyName TEXT);";
        Statement stmt = jdbcConn.createStatement();
        stmt.addBatch(configTableBlueprint);
        stmt.addBatch(configTablePopulation);
        stmt.addBatch(relayTableBlueprint);
        stmt.executeBatch();
    }

    //Loads the user’s configuration from the database
    public UserConfigUnit loadUserCfg() throws SQLException {
        //SQL query to be run
        String query = "SELECT FieldName, FieldValue " +
                "FROM tblConfig;";

        //Execute the query
        Statement stmt = jdbcConn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //Load default values in case the database has these fields stored as NULL
        boolean limitByPercentage = UserConfigUnit.DEFAULT_LIMIT_BY_PERCENTAGE;
        int maxBattPercentage = UserConfigUnit.DEFAULT_MAXIMUM_PERCENTAGE;

        //Loop through the results and assign these variables if they are present
        while (rs.next()) {
            //Load the row
            String field = rs.getString("FieldName");
            String value = rs.getString("FieldValue");

            //If the value is NULL in the database, do not try to parse it
            if (rs.wasNull())
                continue;

            //Determine which field this row is for and populate it
            try {
                switch (field) {
                    case "LimitByPercentage":
                        limitByPercentage = Boolean.parseBoolean(value);
                        break;
                    case "MaxBattPercentage":
                        maxBattPercentage = Integer.parseInt(value);
                        break;
                }
            } catch (NumberFormatException ignored) {}; //If there is something wrong with the data, the default must be used
        }

        return new UserConfigUnit(limitByPercentage, maxBattPercentage);
    }

    //Saves the provided configuration into the database, overwriting the existing content
    public void saveUserCfg(UserConfigUnit cfg) throws SQLException {
        //SQL query template
        String query = "UPDATE tblConfig SET FieldValue = ? WHERE FieldName = ?;";
        //Create prepared statement based on query template
        PreparedStatement pstmt = jdbcConn.prepareStatement(query);

        //Save LimitByPercentage
        pstmt.setString(2, "LimitByPercentage");
        pstmt.setString(1, String.valueOf(cfg.isLimitedByPercentage()));
        pstmt.addBatch();

        //Save MaxBattPercentage
        pstmt.setString(2, "MaxBattPercentage");
        pstmt.setString(1, String.valueOf(cfg.getMaxBatteryPercentage()));
        pstmt.addBatch();

        //Execute the query
        pstmt.executeBatch();
    }

    //Loads all relay associations saved in the database
    public ArrayList<RelayAssociation> loadAllRelayAssociations() throws SQLException {
        //SQL query
        String query = "SELECT RelayID, NetworkAddress, FriendlyName FROM tblRelay;";
        //Execute query and get results
        Statement stmt = jdbcConn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //Create result object
        ArrayList<RelayAssociation> result = new ArrayList<>();

        //Loop through SQL results
        while (rs.next()) {
            //Load data
            int relayID = rs.getInt("RelayID");
            String netAddr = rs.getString("NetworkAddress");
            String friendlyName = rs.getString("FriendlyName");
            //Create object
            RelayAssociation ra = new RelayAssociation(relayID, netAddr, friendlyName);
            //Add to result ArrayList
            result.add(ra);
        }

        return result;
    }

    //Loads a specific relay association saved in the database under the provided primary key
    public RelayAssociation loadRelayAssociation(int primaryKey) throws SQLException {
        //SQL query
        String query = "SELECT RelayID, NetworkAddress, FriendlyName FROM tblRelay WHERE RelayID = ?;";
        //Prepare statement
        PreparedStatement pstmt = jdbcConn.prepareStatement(query);
        //Set value
        pstmt.setInt(1, primaryKey);
        //Execute statement
        ResultSet rs = pstmt.executeQuery();

        //Load results
        String netAddr = rs.getString("NetworkAddress");
        String friendlyName = rs.getString("FriendlyName");

        //Instantiate and return object
        return new RelayAssociation(primaryKey, netAddr, friendlyName);
    }

    //Creates a database entry or updates the existing database entry for the provided relay association
    public void saveRelayAssociation(RelayAssociation ra) throws SQLException {
        if (ra.isSavedInDB()) { //Update the existing record
            //SQL query
            String query = "UPDATE tblRelay SET NetworkAddress = ?, FriendlyName = ? WHERE RelayID = ?";
            //Prepare statement
            PreparedStatement pstmt = jdbcConn.prepareStatement(query);
            //Set values
            pstmt.setString(1, ra.getNetworkAddress());
            pstmt.setString(2, ra.getFriendlyName());
            //Execute query
            pstmt.execute();
        } else { //Create a new record
            //SQL query
            String query = "INSERT INTO tblRelay(NetworkAddress, FriendlyName) VALUES (?, ?);";
            //Prepare statement
            PreparedStatement pstmt = jdbcConn.prepareStatement(query);
            //Set values
            pstmt.setString(1, ra.getNetworkAddress());
            pstmt.setString(2, ra.getFriendlyName());
            //Execute query
            pstmt.execute();

            //Get generated primary key
            int primaryKey = pstmt.getGeneratedKeys().getInt(1);
            //Save primary key to object
            ra.setDBPrimaryKey(primaryKey);
        }
    }

    //Deletes the relay association with the specified primary key from the database
    public void deleteRelayAssociation(int primaryKey) throws SQLException {
        //SQL query
        String query = "DELETE FROM tblRelay WHERE RelayID = ?";
        //Prepare statement
        PreparedStatement pstmt = jdbcConn.prepareStatement(query);
        pstmt.setInt(1, primaryKey);
        //Execute query
        pstmt.execute();
    }

    public void close() {
        try {
            jdbcConn.close();
        } catch (SQLException ignored) {}
    }
}
