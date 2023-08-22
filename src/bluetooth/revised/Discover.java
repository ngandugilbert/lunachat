package bluetooth.revised;

import javax.bluetooth.*;

import java.io.IOException;
import java.util.LinkedList;

public class Discover extends Thread {
    private LinkedList<RemoteDevice> foundDevices = new LinkedList<>();

    public LinkedList<RemoteDevice> getFoundDevices() {
        return foundDevices;
    }

    @Override
    public void run() {
        LocalDevice localDevice;
        DiscoveryAgent discoveryAgent;

         // clear the list whenever a new inquiry is started
         foundDevices.clear();

        try {
            // Get the local Bluetooth device
            localDevice = LocalDevice.getLocalDevice();

            // Get the discovery agent
            discoveryAgent = localDevice.getDiscoveryAgent();

            // Start device discovery
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
               
                
                @Override
                public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                    try {
                        String deviceName = remoteDevice.getFriendlyName(false);
                        foundDevices.add(remoteDevice);
                        
                        System.out.println("Discovered device: " + deviceName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void inquiryCompleted(int discType) {
                    System.out.println("Device discovery completed.");
                    return;
                }

                @Override
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    // Not used in this example
                }

                @Override
                public void serviceSearchCompleted(int transID, int respCode) {
                    // Not used in this example
                }
            });
        } catch (BluetoothStateException e) {
            e.printStackTrace();
        }
    }
}
