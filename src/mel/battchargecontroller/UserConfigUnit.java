package mel.battchargecontroller;

//Stores user configuration as a single unit
public class UserConfigUnit {
    //Default configuration value for limitByTemperature
    public static final boolean DEFAULT_LIMIT_BY_TEMPERATURE = false;

    //Default configuration value for maxBattTemp
    public static final int DEFAULT_MAXIMUM_TEMPERATURE = 50;

    //Default configuration value for limitByPercentage
    public static final boolean DEFAULT_LIMIT_BY_PERCENTAGE = false;

    //Default configuration value for maxBattPercentage
    public static final int DEFAULT_MAXIMUM_PERCENTAGE = 80;

    //Whether the program should limit charging by battery temperature
    private boolean limitByTemperature;

    //User specified maximum battery temperature (Celsius)
    private int maxBatteryTemperature;

    //Whether or not the program should limit charging by battery percentage
    private boolean limitByPercentage;

    //User specified maximum battery percentage
    private int maxBatteryPercentage;

    //Constructor
    public UserConfigUnit(boolean limitByTemperature, int maxBatteryTemperature, boolean limitByPercentage, int maxBatteryPercentage) {
        this.limitByTemperature = limitByTemperature;
        this.maxBatteryTemperature = maxBatteryTemperature;
        this.limitByPercentage = limitByPercentage;
        this.maxBatteryPercentage = maxBatteryPercentage;
    }

    //Accessors and mutators

    public boolean isLimitedByTemperature() {
        return limitByTemperature;
    }

    public void setLimitByTemperature(boolean limitByTemperature) {
        this.limitByTemperature = limitByTemperature;
    }

    public int getMaxBatteryTemperature() {
        return maxBatteryTemperature;
    }

    public void setMaxBatteryTemperature(int maxBatteryTemperature) {
        this.maxBatteryTemperature = maxBatteryTemperature;
    }

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
