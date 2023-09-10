package mel.battchargecontroller;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainBackgroundService {
    private static final int MANAGED_TIME_DELAY = 60;
    public static void main(String[] args) {
        //Set look and feel for managed charging initiation dialog
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ignored) {}

        //Check if the computer this program is running on can report basic battery information
        BatteryInfo.update();
        if (!BatteryInfo.isChargingStateAvailable()) {
            //This program will not work if the system cannot report its charging state
            System.err.println("Charging state not available");
            return;
        }

        //Main control loop
        while (true) {
            //Initially enter the idle state
            idleState();
            //The system has now started charging - ask the user whether to enter managed charging
            try {
                //Load relays and config
                StorageDatabase sdb = new StorageDatabase(Constants.DB_PATH);
                ArrayList<RelayAssociation> relayAssociations = sdb.loadAllRelayAssociations();
                UserConfigUnit userConfig = sdb.loadUserCfg();
                sdb.close();

                //Launch the dialog
                ManagedChargingIntiation dia = new ManagedChargingIntiation(relayAssociations);
                //Determine whether to use managed or unmanaged charging
                if (dia.isManagedChargingEnabled()) {
                    System.out.println("Managed charging initiated.");
                    managedState(dia.getSelectedAssociation(), userConfig);
                } else {
                    System.out.println("Unmanaged charging initiated");
                    unmanagedState();
                }
                System.out.println("Charging ended");
            } catch (SQLException | IOException e) {
                System.err.println("Unable to connect to config database. Not entering managed mode.");
            }
        }
    }
    //Runs managed charging
    private static void managedState(ChargeController chargeController, UserConfigUnit userConfig) {
        BatteryInfo.update();
        do {
            sleep(MANAGED_TIME_DELAY); //Avoid short cycling
            boolean percentageLimitExceeded = BatteryInfo.isPercentageAvailable() && userConfig.isLimitedByPercentage()
                    && BatteryInfo.getPercentage() > userConfig.getMaxBatteryPercentage();

            chargeController.setState(!percentageLimitExceeded); //Turn the relay off if either temperature or percentage limits are reached
            sleep(1); //Give time for the state to change
            BatteryInfo.update();
        } while (BatteryInfo.isCharging() || !chargeController.isOn()); //Stay in the managed state while the system is either charging, or supposed to be paused

    }
    //Blocks until a charger is plugged in
    private static void idleState() {
        do {
            //Get battery information from the OS
            BatteryInfo.update();
            //Wait to avoid polling the OS too frequently
            sleep(1);
        } while (!BatteryInfo.isCharging());
    }
    //Blocks until the charger is disconnected
    private static void unmanagedState() {

        do {
            //Get battery information from the OS
            BatteryInfo.update();
            //Wait to avoid polling the OS too frequently
            sleep(1);
        } while (BatteryInfo.isCharging());
    }
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {}
    }
}
