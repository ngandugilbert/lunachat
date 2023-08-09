package bluetooth;

import java.util.LinkedList;
import javax.bluetooth.*;

public class DiscoverDevices implements Runnable {
    // list of discovered devices
    private LinkedList<RemoteDevice> discoveredDevices;
    private boolean started;

    public LinkedList<RemoteDevice> getDiscoveredDevices() {
        return discoveredDevices;
    }

    final Object inquiryCompletedEvent = new Object();

    @Override
    public void run() {
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

            try {
                started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,
                        listener);
            } catch (BluetoothStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (started) {
                try {
                    inquiryCompletedEvent.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
