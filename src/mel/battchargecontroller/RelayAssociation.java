package mel.battchargecontroller;

//This class is used to represent and communicate with associated smart relays
public class RelayAssociation {
    //Stores the network address of the relay
    private String networkAddress;

    //Stores the name of the relay to be shown in the user interface
    private String friendlyName;

    //Stores the state of the relay. False represents the relay being switched off and true represents the relay being switched on
    private boolean state;

    //Indicates whether the object has been saved into the database
    private boolean savedInDatabase;

    //Stores the primary key which the association is saved under in the database
    private int databasePrimaryKey;

    //Creates a new relay association and commands relay to close the circuit.
    public RelayAssociation(String addr, String name) {
        friendlyName = name;
        networkAddress = addr;
    }

    public RelayAssociation(int primaryKey, String addr, String name) {
        friendlyName = name;
        networkAddress = addr;
        databasePrimaryKey = primaryKey;
    }

    //Attempts to command the device to change its state to what is provided. Only updates the state value if successful
    public void setState(boolean state) {

    }

    //Updates the databasePrimaryKey field and sets the savedInDatabase field to true.
    //Intended to be used by the mel.battchargecontroller.StorageDatabase class.
    public void setDBPrimaryKey(int databasePrimaryKey) {
        this.databasePrimaryKey = databasePrimaryKey;
        savedInDatabase = true;
    }

    //Accessors and mutators
    public String getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public boolean getState() {
        return state;
    }

    public boolean isSavedInDB() {
        return savedInDatabase;
    }

    public int getDBPrimaryKey() {
        return databasePrimaryKey;
    }


}
