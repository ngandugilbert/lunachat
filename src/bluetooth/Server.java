package bluetooth;

import java.io.IOException;
import java.io.InputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;

public class Server extends Thread {
    private  boolean isStarted;
    private  String serverName;

    public  String getServerName() {
        return serverName;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public  void run() {
        String name = "lunachat";
        try {

            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

            SessionNotifier serverConnection = (SessionNotifier) Connector
                    .open("btgoep://localhost:" + LocalDevice.getLocalDevice().getBluetoothAddress() + ";name=" + name);
            serverName = serverConnection.getClass().getName();

            serverConnection.getClass().getName();
            int count = 0;
            while (count < 2) {
                RequestHandler handler = new RequestHandler();
                serverConnection.acceptAndOpen(handler);
                System.out.println("Received OBEX connection " + (++count));
            }
        } catch (BluetoothStateException e) {
            // Handle this problem
        } catch (IOException e) {
            // handle this problem
        }

    }

    private static class RequestHandler extends ServerRequestHandler {

        public int onPut(Operation op) {
            try {
                HeaderSet hs = op.getReceivedHeaders();
                String name = (String) hs.getHeader(HeaderSet.NAME);
                if (name != null) {
                    System.out.println("put name:" + name);
                }

                InputStream is = op.openInputStream();

                StringBuffer buf = new StringBuffer();
                int data;
                while ((data = is.read()) != -1) {
                    buf.append((char) data);
                }

                System.out.println("got:" + buf.toString());

                op.close();
                return ResponseCodes.OBEX_HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
            }
        }
    }

}
