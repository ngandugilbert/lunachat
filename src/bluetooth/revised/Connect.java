package bluetooth.revised;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Connect {
    private static StreamConnection connection;

    // getter for the connection
    public static StreamConnection getConnection() {
        return connection;
    }

    public static void connect(String address) {
        connection = null; // set the connection to null
        try {
            // String deviceAddress = "48137E1527E2"; // Replace with the actual device address
            String connectionString = "btspp://" + address + ":3";

            StreamConnection streamConnection = (StreamConnection) Connector.open(connectionString);
            connection = streamConnection; // set the connection to the streamConnection

            // Now you can use the streamConnection to send/receive data with the device

            streamConnection.close(); // Close the connection when done
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
