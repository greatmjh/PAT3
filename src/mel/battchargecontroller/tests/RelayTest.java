package mel.battchargecontroller.tests;

import mel.battchargecontroller.RelayAssociation;

public class RelayTest {
    private static final String IP_ADDR = "192.168.33.1";
    public static void main(String[] args) {
        RelayAssociation ra = new RelayAssociation(IP_ADDR, "testing");
        ra.setState(true);
        while(true);
    }
}
