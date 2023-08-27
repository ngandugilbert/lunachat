package bluetooth;

import java.io.*;
import java.util.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class LunaChatServer {
    private List<ClientHandler> clients;

    public void startServer() {
        clients = new ArrayList<>();

        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);

            String serviceUUID = localDevice.getBluetoothAddress(); // Example UUID
            String serviceURL = "btspp://localhost:" + serviceUUID + ";name=ChatAppServer";

            StreamConnectionNotifier connectionNotifier = (StreamConnectionNotifier) Connector.open(serviceURL);

            System.out.println("Server is waiting for connections...");

            while (true) {
                StreamConnection connection = connectionNotifier.acceptAndOpen();
                ClientHandler clientHandler = new ClientHandler(connection);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToClients(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private class ClientHandler extends Thread {
        private final StreamConnection connection;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(StreamConnection connection) {
            this.connection = connection;
        }

        public void sendMessage(String message) {
            writer.println(message);
        }

        @Override
        public void run() {
            var input = new Scanner(System.in);
            try {
                InputStream inputStream = connection.openInputStream();
                OutputStream outputStream = connection.openOutputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                writer = new PrintWriter(outputStream, true);

                String message;
                while (true) {
                    message = input.nextLine();
                    sendMessageToClients("Server echoes: " + message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    connection.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        var server = new LunaChatServer();
        server.startServer();
        Scanner input = new Scanner(System.in);
        System.out.print("Message: ");
        String message = input.nextLine();
        server.sendMessageToClients(message);
        input.close();
    }
}
