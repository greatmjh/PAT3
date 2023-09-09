package mel.battchargecontroller;

public interface ChargeController {
    void setState(boolean newState);
    boolean isOn();

    String toString();
}
