package bluetooth;

import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class TestSender {

    public static void main(String[] args) {
        String targetDeviceAddress = "YOUR_TARGET_DEVICE_MAC_ADDRESS";

        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

            RemoteDevice targetDevice 

            String uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"; // This is a standard UUID for SPP (Serial Port Profile)

            StreamConnectionNotifier notifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:" + uuid + ";name=SampleServer");
            System.out.println("Waiting for a connection...");

            StreamConnection connection = notifier.acceptAndOpen();
            System.out.println("Connection established.");

            String messageToSend = "Hello, Bluetooth World!";

            // Send the message
            connection.openDataOutputStream().write(messageToSend.getBytes());

            connection.close();
            notifier.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

