package mel.battchargecontroller;

import mel.battchargecontroller.BatteryInfo;

public class MainBackgroundService {
    public static void main(String[] args) {
        //Check if the computer this program is running on can report basic battery information
        BatteryInfo.update();
        if (!BatteryInfo.isChargingStateAvailable()) {
            //This program will not work if the system cannot report its charging state
            System.err.println("Charging state not available");
            return;
        }

        //Enter a loop so that the program can restart if an exception occurs
        while (true) {
            try {
                //Get battery information from the OS
                BatteryInfo.update();

                //Wait for the PC to start charging
                while (!BatteryInfo.isCharging()) {
                    sleep(1);
                    BatteryInfo.update();
                }

                //The system has now started charging

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Restarting...");
            }
        }
    }
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {}
    }
}
