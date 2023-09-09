package mel.battchargecontroller.tests;

import mel.battchargecontroller.RelayAssociation;
import mel.battchargecontroller.StorageDatabase;
import mel.battchargecontroller.UserConfigUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class StorageDatabaseTest {
    //Testing constants
    static final String TEST_DB_PATH = "test_db_1.db";
    static final boolean TEST_LIMIT_BY_TEMPERATURE = true;
    static final int TEST_MAXIMUM_TEMPERATURE = 56;
    static final boolean TEST_LIMIT_BY_PERCENTAGE = false;
    static final int TEST_MAXIMUM_PERCENTAGE = 75;
    static final String TEST_RELAY_1_FN = "Relay 1";
    static final String TEST_RELAY_1_NA = "1.2.3.4";
    static final String TEST_RELAY_2_FN = "Relay 2";
    static final String TEST_RELAY_2_NA = "2.3.4.5";
    static final String TEST_RELAY_3_FN = "Relay 2";
    static final String TEST_RELAY_3_NA = "2.3.4.5";
    public static void main(String[] args) throws IOException {
        boolean allTestsPassed = true;
        System.out.println("Testing class StorageDatabase");

        //=============Constructor test=============
        System.out.println("Testing creation of new database");
        //Delete test file if exists
        Files.deleteIfExists(Path.of(TEST_DB_PATH));
        //Create StorageDatabase
        StorageDatabase testDB1;
        try {
            testDB1 = new StorageDatabase(TEST_DB_PATH);
            System.out.println("PASS: created new database with non-existing file");
        } catch (Exception e) {
            System.err.println("FAIL: exception occurred when creating database with non-existing file:");
            e.printStackTrace();
            return;
        }

        System.out.println();

        //User config related tests
        if (!configTests(testDB1))
            allTestsPassed = false;

        //Relay storage related tests
        if (!relayTests(testDB1))
            allTestsPassed = false;

        if (allTestsPassed)
            System.out.println("All tests passed");
    }

    static boolean relayTests(StorageDatabase testDB1) {
        boolean testsPassed = true;
        RelayAssociation preLoadRa1 = new RelayAssociation(TEST_RELAY_1_NA, TEST_RELAY_1_FN);
        RelayAssociation preLoadRa2 = new RelayAssociation(TEST_RELAY_2_NA, TEST_RELAY_2_FN);

        //=============Test of saveRelayAssociation=============
        System.out.println("Testing saveRelayAssociation()");
        try {
            testDB1.saveRelayAssociation(preLoadRa1);
            testDB1.saveRelayAssociation(preLoadRa2);
            System.out.println("PASS: saveRelayAssociation succeeded");
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred with saveRelayAssociation():");
            e.printStackTrace();
            return false;
        }

        System.out.println();

        //=============Test of loadAllRelayAssociations()=============
        System.out.println("Testing loadAllRelayAssociations()");
        try {
            ArrayList<RelayAssociation> loadedRAs = testDB1.loadAllRelayAssociations();
            //Check parameters
            if (Objects.equals(loadedRAs.get(0).getFriendlyName(), TEST_RELAY_1_FN) &&
                    Objects.equals(loadedRAs.get(0).getNetworkAddress(), TEST_RELAY_1_NA) &&
                    Objects.equals(loadedRAs.get(1).getFriendlyName(), TEST_RELAY_2_FN) &&
                    Objects.equals(loadedRAs.get(1).getNetworkAddress(), TEST_RELAY_2_NA)) {
                System.out.println("PASS: loadAllRelayAssociations() loaded with correct parameters");
            } else {
                System.out.println("FAIL: loadAllRelayAssociations() loaded incorrect parameters");
                testsPassed = false;
            }
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred with loadAllRelayAssociations():");
            e.printStackTrace();
            testsPassed = false;
        }
        System.out.println();

        //==============Test of loadRelayAssociation===============
        System.out.println("Testing loadRelayAssociation()");
        try {
            RelayAssociation loadedRA1 = testDB1.loadRelayAssociation(preLoadRa1.getDBPrimaryKey());
            //Check parameters
            if (Objects.equals(loadedRA1.getFriendlyName(), TEST_RELAY_1_FN) &&
                    Objects.equals(loadedRA1.getNetworkAddress(), TEST_RELAY_1_NA)) {
                System.out.println("PASS: loadRelayAssociation() loaded with correct parameters");
            } else {
                System.out.println("FAIL: loadAllAssociation() loaded incorrect parameters");
                testsPassed = false;
            }
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred with loadRelayAssociation():");
            e.printStackTrace();
            testsPassed = false;
        }
        System.out.println();

        //==================Test of deleteRelayAssociation==================
        System.out.println("Testing deleteRelayAssociation()");
        try {
            testDB1.deleteRelayAssociation(preLoadRa2.getDBPrimaryKey());
            System.out.println("PASS: deleteRelayAssociation() succeeded.");
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred with deleteRelayAssociationn():");
            e.printStackTrace();
            testsPassed = false;
        }
        System.out.println();

        return testsPassed;
    }
    static boolean configTests(StorageDatabase testDB1) {
        boolean testsPassed = true;
        //=============Test of loadUserCfg with no data=============
        System.out.println("Running loadUserCfg on an empty database");
        try {
            UserConfigUnit emptyDbCfg = testDB1.loadUserCfg();
            if (emptyDbCfg.isLimitedByPercentage() == UserConfigUnit.DEFAULT_LIMIT_BY_PERCENTAGE &&
                    emptyDbCfg.isLimitedByTemperature() == UserConfigUnit.DEFAULT_LIMIT_BY_TEMPERATURE &&
                    emptyDbCfg.getMaxBatteryPercentage() == UserConfigUnit.DEFAULT_MAXIMUM_PERCENTAGE &&
                    emptyDbCfg.getMaxBatteryTemperature() == UserConfigUnit.DEFAULT_MAXIMUM_TEMPERATURE) {
                System.out.println("PASS: Default values are present");
            } else {
                System.out.println("FAIL: Default values are not present");
                testsPassed = false;
            }
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred when loading user config from empty database:");
            e.printStackTrace();
            testsPassed = false;
        }
        System.out.println();

        //=============Test of saveUserCfg()=============
        System.out.println("Testing saveUserCfg() method");
        UserConfigUnit preSaveTestConfig = new UserConfigUnit(TEST_LIMIT_BY_TEMPERATURE, TEST_MAXIMUM_TEMPERATURE, TEST_LIMIT_BY_PERCENTAGE, TEST_MAXIMUM_PERCENTAGE);
        try {
            testDB1.saveUserCfg(preSaveTestConfig);
            System.out.println("PASS: saveUserCfg() succeeded");
        } catch (Exception e) {;
            System.out.println("FAIL: exception occurred when using saveUserCfg()");
            e.printStackTrace();
            return false;
        }
        System.out.println();

        //=============Test of loadUserCfg() with known data=============
        try {
            UserConfigUnit postSaveTestConfig = testDB1.loadUserCfg();
            if (postSaveTestConfig.isLimitedByPercentage() == TEST_LIMIT_BY_PERCENTAGE &&
                    postSaveTestConfig.isLimitedByTemperature() == TEST_LIMIT_BY_TEMPERATURE &&
                    postSaveTestConfig.getMaxBatteryPercentage() == TEST_MAXIMUM_PERCENTAGE &&
                    postSaveTestConfig.getMaxBatteryTemperature() == TEST_MAXIMUM_TEMPERATURE) {
                System.out.println("PASS: Correct values are present");
            } else {
                System.out.println("FAIL: Correct values are not present");
                return false;
            }
        } catch (Exception e) {
            System.out.println("FAIL: exception occurred when using loadUserCfg()");
            e.printStackTrace();
            return false;
        }
        System.out.println();
        return testsPassed;
    }
}
