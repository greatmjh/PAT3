package mel.battchargecontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class BatteryInfo {
    //Stores whether the system support battery percentage reporting
    private static boolean percentageAvailable = false;

    //Stores the battery percentage
    private static int percentage = 0;

    //Stores whether the system support battery temperature reporting
    private static boolean temperatureAvailable = false;

    //Stores battery temperature in Celsius
    private static int temperature = 0;

    //Stores whether the system can report whether the device is charging
    private static boolean chargingStateAvailable = false;

    //Stores whether the system is charging
    private static boolean charging = false;

    //Accessors
    public static boolean isPercentageAvailable() {
        return percentageAvailable;
    }

    public static int getPercentage() {
        return percentage;
    }

    public static boolean isTemperatureAvailable() {
        return temperatureAvailable;
    }

    public static int getTemperature() {
        return temperature;
    }

    public static boolean isChargingStateAvailable() {
        return chargingStateAvailable;
    }

    public static boolean isCharging() {
        return charging;
    }

    //Queries the system for the latest battery info
    public static void update() {
        Runtime rt = Runtime.getRuntime();
        try {
            //Get the battery percentage
            String batteryPercentageCmd = "Get-CimInstance -ClassName Win32_Battery | Select-Object -ExpandProperty EstimatedChargeRemaining"; //Borrowed from https://powershell.one/wmi/root/cimv2/win32_battery
            //Run the command
            String stPercentage = execPowershell(batteryPercentageCmd);
            //Attempt to process the output
            try {
                percentage = Integer.parseInt(stPercentage);
                //Set percentageAvailable only if this does not throw an exception
                percentageAvailable = true;

            } catch (NumberFormatException n) {
                //The OS produced invalid data, disregard it
                percentageAvailable = false;
            }

            //Get the battery temperature
            String batteryTemperatureCmd = ""; //TODO: implement this PowerShell code
            //Run the command
            String stTemperature = execPowershell(batteryTemperatureCmd);
            //Attempt to process the output
            try {
                temperature = Integer.parseInt(stTemperature);
                //Set temperatureAvailable only if this does not throw an exception
                temperatureAvailable = true;
            } catch (NumberFormatException n) {
                temperatureAvailable = false;
            }

            //Get the charging state
            String chargingStateCmd = "(Get-CimInstance -Namespace \"ROOT\\WMI\" -ClassName \"BatteryStatus\").Charging"; //Borrowed from https://github.com/gwblok/garytown/blob/master/hardware/HP/BatteryInfo.ps1
            //Run the command
            String stChargingState = execPowershell(chargingStateCmd);
            //Process the output into a boolean
            if (stChargingState.equalsIgnoreCase("True")) {
                chargingStateAvailable = true;
                charging = true;
            } else if (stChargingState.equalsIgnoreCase("False")) {
                chargingStateAvailable = true;
                charging = false;
            } else {
                //The system produced invalid information
                chargingStateAvailable = false;
                //System.err.println("System returned invalid response for charging state request: " + stChargingState);
            }
            //Force charging to be true if the battery is at 100%
            if (percentageAvailable && percentage == 100) {
                charging = true;
            }

        } catch (IOException ex) {
            //An error occurred when running commands
            percentageAvailable = false;
            temperatureAvailable = false;
            chargingStateAvailable = false;
        }
    }

    //Borrowed and adapted from https://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
    private static String execPowershell(String cmd) throws IOException {
        String result = "";
        InputStream inputStream = Runtime.getRuntime().exec("powershell -c " + cmd).getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        result = s.hasNext() ? s.next() : "";
        s.close();
        inputStream.close();
        return result.trim();
    }

}
