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

    //Whether or not the program should limit charging by battery temperature
    private boolean limitByTemperature;

    //User specified maximum battery temperature (Celsius)
    private int maxBattTemp;

    //Whether or not the program should limit charging by battery percentage
    private boolean limitByPercentage;

    //User specified maximum battery percentage
    private int maxBattPercentage;

    //Constructor
    public UserConfigUnit(boolean limitByTemperature, int maxBattTemp, boolean limitByPercentage, int maxBattPercentage) {
        this.limitByTemperature = limitByTemperature;
        this.maxBattTemp = maxBattTemp;
        this.limitByPercentage = limitByPercentage;
        this.maxBattPercentage = maxBattPercentage;
    }

    //Accessors and mutators

    public boolean isLimitByTemperature() {
        return limitByTemperature;
    }

    public void setLimitByTemperature(boolean limitByTemperature) {
        this.limitByTemperature = limitByTemperature;
    }

    public int getMaxBattTemp() {
        return maxBattTemp;
    }

    public void setMaxBattTemp(int maxBattTemp) {
        this.maxBattTemp = maxBattTemp;
    }

    public boolean isLimitByPercentage() {
        return limitByPercentage;
    }

    public void setLimitByPercentage(boolean limitByPercentage) {
        this.limitByPercentage = limitByPercentage;
    }

    public int getMaxBattPercentage() {
        return maxBattPercentage;
    }

    public void setMaxBattPercentage(int maxBattPercentage) {
        this.maxBattPercentage = maxBattPercentage;
    }
}
