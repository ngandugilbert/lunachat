package bluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;


public class Test {
    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        

        
        DiscoverDevices.discover();

        var discovered = DiscoverDevices.getDiscoveredDevices();

        var iterator = discovered.iterator();
        while(iterator.hasNext()){
            try {
                System.out.println(iterator.next().getFriendlyName(false));
            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }

       
    }
}
