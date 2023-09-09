package mel.battchargecontroller.configui;

import com.formdev.flatlaf.FlatDarculaLaf;
import mel.battchargecontroller.*;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConfigWindow {
    public ConfigWindow() {
        //Connect to and load from the database
        try {
            StorageDatabase sdb = new StorageDatabase(Constants.DB_PATH);
            //Load user config
            loadedConfig = sdb.loadUserCfg();
            //Load relays
            relayAssociations = sdb.loadAllRelayAssociations();
            //Close the connection
            sdb.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "A fatal SQL error occurred while loading the configuration database", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "A fatal IO error occurred while loading the configuration database", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
        //Gray out sliders for options that are unavailable
        BatteryInfo.update();
        if (!BatteryInfo.isTemperatureAvailable()) {
            limitByTemperatureCheckBox.setEnabled(false);
            temperatureSlider.setEnabled(false);
            temperatureUnsupportedLabel.setVisible(true);
        }
        if (!BatteryInfo.isPercentageAvailable()) {
            limitByPercentageCheckBox.setEnabled(false);
            percentageSlider.setEnabled(false);
            percentageUnsupportedLabel.setVisible(true);
        }

        //Display the user configuration
        limitByPercentageCheckBox.setSelected(loadedConfig.isLimitByPercentage());
        limitByTemperatureCheckBox.setSelected(loadedConfig.isLimitByTemperature());
        percentageSlider.setValue(loadedConfig.getMaxBattPercentage());
        temperatureSlider.setValue(loadedConfig.getMaxBattTemp());

        //Display the relay associations
        relayList.setListData(relayAssociations.toArray(new RelayAssociation[0]));


        //Listeners
        deleteRelayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRelay();
            }
        });
        addNewRelayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchAddRelayDialog();
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (syncWithDB()) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncWithDB();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        frame = new JFrame("Configuration");
        frame.setContentPane(new ConfigWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private UserConfigUnit loadedConfig;
    private ArrayList<RelayAssociation> relayAssociations;

    private ArrayList<RelayAssociation> deletedRelayAssociations = new ArrayList<>();


    private void deleteSelectedRelay() {
        RelayAssociation selectedRelay = relayList.getSelectedValue();
        int userChoice = JOptionPane.showConfirmDialog(tabs, String.format("Are you sure you want to delete relay \"%s\"?", selectedRelay.getFriendlyName()), "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (userChoice == JOptionPane.YES_OPTION) {
            relayAssociations.remove(selectedRelay);
            relayList.setListData(relayAssociations.toArray(new RelayAssociation[0]));
            deletedRelayAssociations.add(selectedRelay);
        }

    }
    private void launchAddRelayDialog() {
        new AddNewRelay(this);
    }

    //To be called by the AddNewRelay dialog to add the relay association into the system
    public void addRelay(String name, String addr) {
        //Check if the address is a valid IP address
        boolean isValidIP = InetAddressValidator.getInstance().isValid(addr);
        //Check if the address is a valid DNS name
        boolean isValidDomain = DomainValidator.getInstance(true).isValid(addr);
        if (isValidIP || isValidDomain) {
            //The address is valid, create and store the new association
            RelayAssociation newRa = new RelayAssociation(addr, name);
            relayAssociations.add(newRa);
            relayList.setListData(relayAssociations.toArray(new RelayAssociation[0]));
        } else {
            JOptionPane.showMessageDialog(frame, "The specified network address is not valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLoadedConfig() {
        loadedConfig.setLimitByPercentage(limitByPercentageCheckBox.isSelected());
        loadedConfig.setLimitByTemperature(limitByTemperatureCheckBox.isSelected());
        loadedConfig.setMaxBattPercentage(percentageSlider.getValue());
        loadedConfig.setMaxBattTemp(temperatureSlider.getValue());
    }

    private boolean syncWithDB() {
        try {
            StorageDatabase sdb = new StorageDatabase(Constants.DB_PATH);
            //Save settings
            updateLoadedConfig();
            sdb.saveUserCfg(loadedConfig);
            //Save all relay associations
            for (RelayAssociation ra : relayAssociations) {
                sdb.saveRelayAssociation(ra);
            }
            //Delete all deleted relay associations
            for (RelayAssociation delRa : deletedRelayAssociations) {
                if (delRa.isSavedInDB()) {
                    sdb.deleteRelayAssociation(delRa.getDBPrimaryKey());
                }
            }
            deletedRelayAssociations = new ArrayList<>();
            //Reload all relay associations to ensure that they have the appropriate savedInDB value
            relayList.setListData(relayAssociations.toArray(new RelayAssociation[0]));
            sdb.close();
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred while saving your configuration", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //GUI objects
    private static JFrame frame;
    private JTabbedPane tabs;
    private JPanel panel1;
    private JCheckBox limitByPercentageCheckBox;
    private JCheckBox limitByTemperatureCheckBox;
    private JLabel endDegree;
    private JList<RelayAssociation> relayList;
    private JButton addNewRelayButton;
    private JButton deleteRelayButton;
    private JButton OKButton;
    private JButton applyButton;
    private JButton cancelButton;
    private JSlider percentageSlider;
    private JSlider temperatureSlider;
    private JLabel beginDegree;
    private JLabel percentageUnsupportedLabel;
    private JLabel temperatureUnsupportedLabel;

}
