import javax.bluetooth.*;
import com.intel.bluetooth.*;

public class ServiceFinder {
    public static void main(String[] args) {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

            // Replace with the actual Bluetooth address of the device
            String targetDeviceAddress = "XX:XX:XX:XX:XX:XX"; // Replace with the target device's address

            RemoteDevice remoteDevice = discoveryAgent.getRemoteDevice(new BluetoothAddress(targetDeviceAddress));

            UUID obexServiceUUID = new UUID("0000110500001000800000805F9B34FB", false); // OBEX Object Push service

            discoveryAgent.searchServices(null, new UUID[]{obexServiceUUID}, new RemoteDevice[]{remoteDevice}, new DiscoveryListener() {
                @Override
                public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                    System.out.println("Device discovered: " + remoteDevice.getBluetoothAddress());
                }

                @Override
                public void inquiryCompleted(int discType) {
                    System.out.println("Device discovery completed.");
                }

                @Override
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    for (ServiceRecord record : servRecord) {
                        String url = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                        System.out.println("Service URL: " + url);
                    }
                }

                @Override
                public void serviceSearchCompleted(int transID, int respCode) {
                    if (respCode == DiscoveryListener.SERVICE_SEARCH_COMPLETED) {
                        System.out.println("Service search completed.");
                    } else if (respCode == DiscoveryListener.SERVICE_SEARCH_TERMINATED) {
                        System.out.println("Service search terminated.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
