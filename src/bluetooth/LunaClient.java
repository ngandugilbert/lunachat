package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import Controllers.ChatControllerNew;

public class LunaClient {

    private StreamConnection connection;
    private BufferedReader reader;
    private PrintWriter writer;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String response;

    public String getResponse() {
        return response;
    }
    public BufferedReader getReader() {
        return reader;
    }

    public boolean connect(String address) {
        try {

            String serviceUUID = "4"; // Example UUID
            String serviceURL = "btspp://" + address + ":" + serviceUUID;

            connection = (StreamConnection) Connector.open(serviceURL);

            inputStream = connection.openInputStream();
            outputStream = connection.openOutputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(outputStream, true);

            return true;

        } catch (Exception e) {

        }
        return false;
    }

    public void sendMessage(String message) {
        try {
            // Sending a message to the server
            writer.println(message);

            // Reading the server's response
            String response = reader.readLine();

            
            System.out.println(response);

        } catch (Exception e) {
            // This is to be handled bro!
        }
    }

    public void closeConn() {
        try {
            reader.close();
            writer.close();
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            // Handle this one too bro
            e.printStackTrace();
        }

    }

    public LunaClient() {
        // listen for messages from the server

    }

    
}
