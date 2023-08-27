package bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class LunaServer {
    private boolean isStarted;
    static InputStream inputStream;
    static OutputStream outputStream;
    static BufferedReader reader;
    static PrintWriter writer;
    static StreamConnection connection;

    public void startServer() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            String serviceUUID = localDevice.getBluetoothAddress(); // Example UUID
            String serviceURL = "btspp://localhost:" + serviceUUID + ";name=ChatAppServer";

            StreamConnectionNotifier connectionNotifier = (StreamConnectionNotifier) Connector.open(serviceURL);

            System.out.println("Server is waiting for connections...");
            isStarted = true;
            while (true) {
                connection = connectionNotifier.acceptAndOpen();
                new ClientHandler(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            reader.close();
            writer.close();
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private static class ClientHandler extends Thread {
        private final StreamConnection connection;

        public ClientHandler(StreamConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                inputStream = connection.openInputStream();
                outputStream = connection.openOutputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                writer = new PrintWriter(outputStream, true);

                String message;
                // while ((message = reader.readLine()) != null) {
                // System.out.println("Client says: " + message);

                // // Echo the message back to the client
                // writer.println("Server echoes: " + message);
                // }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
