//Stores user configuration as a single unit
public class UserConfigUnit {
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
