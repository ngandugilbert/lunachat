package bluetooth;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;

public class Client {

    public static void main(String[] args) {
        try {
            String serverAddress = "00:11:22:33:44:55"; // Replace with the server's address
            String serviceUUID = "0000110100001000800000805F9B34FB"; // Example UUID
            String serviceURL = "btspp://" + serverAddress + ":" + serviceUUID;

            StreamConnection connection = (StreamConnection) Connector.open(serviceURL);

            InputStream inputStream = connection.openInputStream();
            OutputStream outputStream = connection.openOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter writer = new PrintWriter(outputStream, true);

            // Sending a message to the server
            writer.println("Hello from the client!");

            // Reading the server's response
            String response = reader.readLine();
            System.out.println("Server response: " + response);

            reader.close();
            writer.close();
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

