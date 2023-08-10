package bluetooth;

import javax.bluetooth.*;

public class FindService {

    public static boolean find(RemoteDevice device) throws Exception {
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        
        UUID[] uuidSet = { new UUID(0x1105) }; // OBEX Object Push service
        int[] attrIDs = { 0x0100 }; // Service name attribute ID
        
        final Object serviceSearchCompletedEvent = new Object();
        final boolean[] serviceFound = { false }; // Using an array to make it mutable
        
        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {}
            public void inquiryCompleted(int discType) {}
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (ServiceRecord record : servRecord) {
                    String url = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    }

                    DataElement serviceName = record.getAttributeValue(0x0100);

                    if (serviceName != null) {
                        serviceFound[0] = true;
                        System.out.println("Service " + serviceName.getValue() + " found at " + url);
                        System.out.println("Connecting...");
                    } else {
                        System.out.println("Service not found at " + url);
                    }
                }
            }
            
            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("Service search completed!");
                synchronized (serviceSearchCompletedEvent) {
                    serviceSearchCompletedEvent.notifyAll();
                }
            }
        };
        
        agent.searchServices(attrIDs, uuidSet, device, listener);
        
        synchronized (serviceSearchCompletedEvent) {
            serviceSearchCompletedEvent.wait();
        }
        
        return serviceFound[0];
    }
}
