package bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class SendMessage {

    public static void main(String[] args) {
        String address = "87E1F724E403"; // Replace this with the address of the device you want to send the message to
        String message = "Hello, world!"; // Replace this with the message you want to send

        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

            // Start the inquiry to discover devices
            RemoteDevice[] devices = discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);
            RemoteDevice remoteDevice = null;

            // Find the target device in the discovered devices
            for (RemoteDevice device : devices) {
                if (device.getBluetoothAddress().equals(address)) {
                    remoteDevice = device;
                    break;
                }
            }

            if (remoteDevice == null) {
                System.out.println("Device not found.");
                return;
            }

            // Create the URL to connect to the remote device
            String url = "btspp://" + remoteDevice.getBluetoothAddress() + ":1;authenticate=false;encrypt=false;master=false";
            StreamConnection streamConnection = (StreamConnection) Connector.open(url);

            // Open the output stream to send the message
            OutputStream outputStream = streamConnection.openOutputStream();
            outputStream.write(message.getBytes());
            outputStream.flush();
            outputStream.close();

            // Close the connection
            streamConnection.close();
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
