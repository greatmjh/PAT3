package mel.battchargecontroller;

//Stores user configuration as a single unit
public class UserConfigUnit {
    //Default configuration value for limitByPercentage
    public static final boolean DEFAULT_LIMIT_BY_PERCENTAGE = false;

    //Default configuration value for maxBattPercentage
    public static final int DEFAULT_MAXIMUM_PERCENTAGE = 80;

    //Whether or not the program should limit charging by battery percentage
    private boolean limitByPercentage;

    //User specified maximum battery percentage
    private int maxBatteryPercentage;

    //Constructor
    public UserConfigUnit(boolean limitByPercentage, int maxBatteryPercentage) {
        this.limitByPercentage = limitByPercentage;
        this.maxBatteryPercentage = maxBatteryPercentage;
    }

    //Accessors and mutators

    public boolean isLimitedByPercentage() {
        return limitByPercentage;
    }

    public void setLimitByPercentage(boolean limitByPercentage) {
        this.limitByPercentage = limitByPercentage;
    }

    public int getMaxBatteryPercentage() {
        return maxBatteryPercentage;
    }

    public void setMaxBatteryPercentage(int maxBatteryPercentage) {
        this.maxBatteryPercentage = maxBatteryPercentage;
    }
}
