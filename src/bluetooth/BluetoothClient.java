package bluetooth;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
//import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BluetoothClient {
    public static void main(String[] args) throws IOException {
         String serverAddress = "2839264ACBBC";
         UUID uuid = new UUID("1101", true);
         String url = "btspp://" + serverAddress + ":"+uuid;

         StreamConnection connection = (StreamConnection) Connector.open(url);

         DataInputStream inputStream = new DataInputStream(connection.openInputStream());
         DataOutputStream outputStream = new DataOutputStream(connection.openOutputStream());

         outputStream.writeUTF("Hello from client");
         outputStream.flush();

         String response = inputStream.readUTF();
         System.out.println("Received response: " + response);
         connection.close();
      connection.close();
}
}