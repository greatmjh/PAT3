package mel.battchargecontroller.tests;

import mel.battchargecontroller.BatteryInfo;

public class BatteryInfoTest {
    public static void main(String[] args) {
        BatteryInfo.update();
        System.out.printf("Battery percentage avail:\t%b\n", BatteryInfo.isPercentageAvailable());
        System.out.printf("Battery percentage:\t\t%d\n", BatteryInfo.getPercentage());
        System.out.printf("Battery charge state avail:\t\t%b\n", BatteryInfo.isChargingStateAvailable());
        System.out.printf("Battery charge state:\t\t%s\n", BatteryInfo.isCharging());
    }
}
