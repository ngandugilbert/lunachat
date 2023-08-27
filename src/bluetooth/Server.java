package bluetooth;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import java.io.*;

public class Server {
    

    public static void main(String[] args) {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            String serviceUUID = "60F6774F74D1"; // Example UUID
            String serviceURL = "btspp://localhost:" + serviceUUID + ";name=ChatAppServer";

            StreamConnectionNotifier connectionNotifier = (StreamConnectionNotifier) Connector.open(serviceURL);

            System.out.println("Server is waiting for connections...");

            while (true) {
                StreamConnection connection = connectionNotifier.acceptAndOpen();
                new ClientHandler(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final StreamConnection connection;

        public ClientHandler(StreamConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = connection.openInputStream();
                OutputStream outputStream = connection.openOutputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(outputStream, true);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Client says: " + message);

                    // Echo the message back to the client
                    writer.println("Server echoes: " + message);
                }

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
}
