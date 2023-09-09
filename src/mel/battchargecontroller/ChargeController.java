package mel.battchargecontroller;

public interface ChargeController {
    //Starts or stops charging
    void setState(boolean newState);
    //Determines whether the device is charging or not
    boolean isOn();
    //Returns the name of the controller
    String toString();
}
