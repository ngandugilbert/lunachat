package bluetooth.revised;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.obex.*;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

public class Server extends Thread {
    private boolean isStarted;
    private static String SERVER_UUID;
    private final static int MAX_CONNECTIONS = 3;
    private static SessionNotifier serverConnection;

    public Server() {
        try {
            SERVER_UUID = LocalDevice.getLocalDevice().getBluetoothAddress();
            System.out.println(SERVER_UUID);
        } catch (BluetoothStateException e) {
            // Handle this exception properly
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void run() {
        try {
            startOBEXServer();
        } catch (IOException e) {
            // Handle this exception properly
        }
    }

    private static void startOBEXServer() throws IOException {
        LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

        String connectionURL = "btgoep://localhost:" + SERVER_UUID + ";name=LunaChat";
        serverConnection = (SessionNotifier) Connector.open(connectionURL);

        int connectionCount = 0;
        while (connectionCount < MAX_CONNECTIONS) {
            RequestHandler handler = new RequestHandler();
            serverConnection.acceptAndOpen(handler);
            System.out.println("Received OBEX connection " + (++connectionCount));
        }
    }

    private static class RequestHandler extends ServerRequestHandler {

        @Override
        public int onPut(Operation op) {
            try {
                processReceivedData(op);
                return ResponseCodes.OBEX_HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
            }
        }

        private void processReceivedData(Operation op) throws IOException {
            HeaderSet hs = op.getReceivedHeaders();
            String name = (String) hs.getHeader(HeaderSet.NAME);

            if (name != null) {
                System.out.println("put name:" + name);
            }

            InputStream is = op.openInputStream();
            String receivedData = readFromInputStream(is);
            System.out.println("got:" + receivedData);
            op.close();

        }

        private String readFromInputStream(InputStream is) throws IOException {
            StringBuilder buf = new StringBuilder();
            int data;
            while ((data = is.read()) != -1) {
                buf.append((char) data);
            }
            return buf.toString();
        }
    }

}
