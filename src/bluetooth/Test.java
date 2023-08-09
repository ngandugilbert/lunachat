package bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

public class Test {
    public static void main(String[] args) throws BluetoothStateException, InterruptedException {
        LocalDevice device = LocalDevice.getLocalDevice();
        System.out.println(device.getBluetoothAddress());;
    }
}
