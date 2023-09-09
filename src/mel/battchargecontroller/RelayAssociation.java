package mel.battchargecontroller;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.SECONDS;

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
        savedInDatabase = true;
    }

    //Attempts to command the device to change its state to what is provided. Only updates the state value if successful
    public void setState(boolean state) {
        String sendState = state ? "on" : "off"; //Set the attribute which will be sent to the relay to either "on" or "off depending on what is required of us
        String uri = String.format("http://%s/relay/0?turn=%s", networkAddress, sendState); //HTTP uri to command device with

        //Send the network request
        try {
            //Create request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .timeout(Duration.of(1, SECONDS))
                    .GET()
                    .build();

            //Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            //Send the request asynchronously
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(new setStateCallback()); //Callback class to update the field in the object only if the operation succeeds

        } catch (URISyntaxException u) {
            System.err.println("Error in URI syntax while commanding relay device:");
            u.printStackTrace();
        }
    }

    //Updates the databasePrimaryKey field and sets the savedInDatabase field to true.
    //Intended to be used by the mel.battchargecontroller.StorageDatabase class.
    public void setDBPrimaryKey(int databasePrimaryKey) {
        this.databasePrimaryKey = databasePrimaryKey;
        savedInDatabase = true;
    }

    public String toString() {
        return friendlyName;
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

    //Callback class for receiving the state from the relay
    private final class setStateCallback implements Consumer<String> {
        public void accept(String response) {
            //This method will run as a callback for the asynchronous HTTP request sent in setState()
            //It will analyse the JSON response and set the `state` field accordingly

            try {
                //Create JSON object
                JSONObject jsonObject = new JSONObject(response);

                //Load the 'ison' property and save it into the field
                state = jsonObject.getBoolean("ison");
            } catch (JSONException j) {
                System.err.println("Error in JSON received from relay:");
                j.printStackTrace();
            }

        }
    }

}
