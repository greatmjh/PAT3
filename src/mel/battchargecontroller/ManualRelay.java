package mel.battchargecontroller;

import javax.swing.*;

public class ManualRelay implements ChargeController {
    private boolean state = false;
    private boolean shownDialogBefore = false;
    public void setState (boolean newState) {
        if (newState == state && shownDialogBefore) return;
        if (newState) {
            JOptionPane.showMessageDialog(null, "Please connect the charger.");
        } else {
            JOptionPane.showMessageDialog(null, "Please disconnect the charger.");
        }
        shownDialogBefore = true;
        state = newState;
    }

    public boolean isOn() {
        return state;
    }

    public String toString() {
        return "Manual (no relay)";
    }
}
