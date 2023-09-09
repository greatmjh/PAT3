package mel.battchargecontroller;

import javax.swing.*;

//This class is used to instruct the user when to manually plug in and unplug the charger
public class ManualManagedCharger implements ChargeController {
    //Stores whether the cable should be plugged in or not
    private boolean state = false;
    //Stores whether or not a dialog has been displayed to the user
    private boolean shownDialogBefore = false;
    //Instructs the user to connect or disconnect the charger
    public void setState (boolean newState) {
        if (newState == state && shownDialogBefore) return;
        if (newState) {
            JOptionPane.showMessageDialog(null, "Please connect the charger and then click OK.");
        } else {
            JOptionPane.showMessageDialog(null, "Please disconnect the charger and then click OK.");
        }
        shownDialogBefore = true;
        state = newState;
    }

    //Accessor for state
    public boolean isOn() {
        return state;
    }

    //The display name is always "Manual (no relay)
    public String toString() {
        return "Manual (no relay)";
    }
}
