package bluetooth;

import java.util.LinkedList;
import javax.bluetooth.*;

public class DiscoverDevices {
    // list of discovered devices
    static LinkedList<RemoteDevice> discoveredDevices;

    public static LinkedList<RemoteDevice> getDiscoveredDevices() {
        return discoveredDevices;
    }

    final static Object inquiryCompletedEvent = new Object();

    public static void discover() throws BluetoothStateException, InterruptedException {
        discoveredDevices = new LinkedList<>();
        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice bluetoothDeice, DeviceClass cod) {
                // add the discovered the devices to the list
                discoveredDevices.add(bluetoothDeice);
            }

            public void inquiryCompleted(int discType) {

                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized (inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,
                    listener);
            if (started) {
                inquiryCompletedEvent.wait();
            }
        }

    }

}
