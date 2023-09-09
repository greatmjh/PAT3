package mel.battchargecontroller;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ManagedChargingIntiation extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<ChargeController> associationComboBox;
    private boolean managedChargingEnabled = false;
    private ChargeController selectedAssociation = null;

    public ManagedChargingIntiation(ArrayList<RelayAssociation> associations) {
        //Build the drop-down
        for (RelayAssociation ra : associations) {
            associationComboBox.addItem(ra);
        }
        associationComboBox.addItem(new ManualManagedCharger()); //Manual option

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setModal(true);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });



        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
        setTitle("Managed charging initiation");
        setVisible(true);
    }

    private void onOK() {
        managedChargingEnabled = true;
        selectedAssociation = (ChargeController)associationComboBox.getSelectedItem();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public ChargeController getSelectedAssociation() {
        return selectedAssociation;
    }

    public boolean isManagedChargingEnabled() {
        return managedChargingEnabled;
    }

}
