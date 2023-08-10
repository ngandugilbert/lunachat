package bluetooth;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Connect {

    public static void main(String[] args) {
        String deviceAddress = "48137E1527E2"; // Replace with the actual device address


        String connectionString = "btspp://" + deviceAddress + ":3" 
                + ";authenticate=false;encrypt=false;master=false";

        try {
            // Open a Bluetooth connection
            StreamConnection connection = (StreamConnection) Connector.open(connectionString);

            // Get the input and output streams for data transfer
            InputStream inputStream = connection.openInputStream();
            OutputStream outputStream = connection.openOutputStream();

            // Example: Send data to the device
            String message = "Hello, Bluetooth Device!";
            byte[] messageBytes = message.getBytes();
            outputStream.write(messageBytes);
            outputStream.flush();

            // Example: Receive data from the device
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String receivedMessage = new String(buffer, 0, bytesRead);
            System.out.println("Received: " + receivedMessage);

            // Close the connection
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
