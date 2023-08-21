package bluetooth;

import java.io.IOException;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer {
    public static void main(String[] args) {
        
        LocalDevice localDevice;
        try {
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());
        } catch (BluetoothStateException e) {
            e.printStackTrace();
            return;
        }

        // UUID uuid = new UUID("1101", true);
        String url = "btspp://localhost:" + "60F6774F74D1" + ";name=BluetoothServer";

        StreamConnectionNotifier notifier;
        try {
            notifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StreamConnection connection;
        try {
            connection =  notifier.acceptAndOpen();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        RemoteDevice remoteDevice;
        try {
            remoteDevice = RemoteDevice.getRemoteDevice(connection);
            System.out.println("Connected to device: " + remoteDevice.getBluetoothAddress());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] buffer = new byte[1024];
        int bytesRead;

        while(true){
            try {
                bytesRead = connection.openInputStream().read(buffer);
                String message = new String(buffer, 0, bytesRead);
                System.out.println("Received message: " + message);

                connection.openOutputStream().write(("Echoing back - " + message).getBytes());
                connection.openOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
