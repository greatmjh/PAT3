package mel.battchargecontroller;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainBackgroundService {
    private static final int MANAGED_TIME_DELAY = 5;
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
            try { //Force the program to reload if there's an error since it is a daemon
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
                        managedState(dia.getSelectedAssociation(), userConfig);
                    } else {
                        unmanagedState();
                    }
                } catch (SQLException | IOException e) {
                    System.err.println("Unable to connect to config database. Not entering managed mode.");
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Restarting...");
            }
        }
    }
    private static void managedState(ChargeController chargeController, UserConfigUnit userConfig) {
        BatteryInfo.update();
        do {
            sleep(MANAGED_TIME_DELAY); //Avoid short cycling
            boolean percentageLimitExceeded = BatteryInfo.isPercentageAvailable() && userConfig.isLimitByPercentage()
                    && BatteryInfo.getPercentage() > userConfig.getMaxBattPercentage();

            boolean temperatureLimitExceeded = BatteryInfo.isTemperatureAvailable() && userConfig.isLimitByTemperature()
                    && BatteryInfo.getTemperature() > userConfig.getMaxBattTemp();

            chargeController.setState(!(percentageLimitExceeded || temperatureLimitExceeded)); //Turn the relay off if either temperature or percentage limits are reached
            BatteryInfo.update();
        } while (!(!BatteryInfo.isCharging() && chargeController.isOn()));

    }
    private static void idleState() {
        //Get battery information from the OS
        BatteryInfo.update();

        //Wait for the PC to start charging
        while (!BatteryInfo.isCharging()) {
            sleep(1);
            BatteryInfo.update();
        }
    }

    private static void unmanagedState() {
        //Get battery information from the OS
        BatteryInfo.update();

        //Wait for the PC to start charging
        while (BatteryInfo.isCharging()) {
            sleep(1);
            BatteryInfo.update();
        }
    }
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {}
    }
}
