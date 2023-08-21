package bluetooth;

import java.io.IOException;
import java.io.OutputStream;
// import java.util.Scanner;
import java.util.Scanner;

import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.obex.*;

public class Chat extends Thread {

    public ClientSession clientSession;
    private String serverURL;
    private LocalDevice me;
    private boolean isSessionStarted;
    private String[] searchArgs = new String[1];

    public void setSearchArgs(String searchArgs) {
        this.searchArgs[0] = searchArgs;
    }

    public boolean isSessionStarted() {
        return isSessionStarted;
    }

    public boolean findService() {
        if (serverURL == null) {
            try {
                Search.main(searchArgs);
                if (Search.serviceFound.size() > 0) {
                    serverURL = (String) Search.serviceFound.elementAt(0);
                    return true;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean startSession() {
        clientSession = null;
        try {
            if (serverURL != null) {
                clientSession = (ClientSession) Connector.open(serverURL);
                HeaderSet hsConnectReply = clientSession.connect(null);
                if (hsConnectReply.getResponseCode() == ResponseCodes.OBEX_HTTP_OK) {
                    isSessionStarted = true;
                    return true;
                } else {
                    System.out.println("Failed to connect");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessage(String message) {
        if (clientSession != null) {
            try {
                me = LocalDevice.getLocalDevice();
                HeaderSet hsOperation = clientSession.createHeaderSet();
                hsOperation.setHeader(HeaderSet.NAME, me.getFriendlyName());
                hsOperation.setHeader(HeaderSet.TYPE, "text");
                Operation putOperation = clientSession.put(hsOperation);

                byte[] data = message.getBytes("iso-8859-1");
                OutputStream os = putOperation.openOutputStream();
                os.write(data);
                os.close();

                putOperation.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Client session is not established.");
        }
    }

    @Override
    public void run() {
        // find service
        if (findService()) {
            // service was found
            if (startSession()) {
                isSessionStarted = true;
            }
        }

    }

    public static void main(String[] args) {
        var chat = new Chat();
        chat.setSearchArgs("5C879CB41030");
        boolean serviceFound = chat.findService();
        if (serviceFound) {
            boolean sessionStarted = chat.startSession();

            if (sessionStarted) {

                Scanner input = new Scanner(System.in);
                int count = 0;
                do {
                    System.out.print("Enter text: ");
                    String message = input.nextLine();

                    chat.sendMessage(message);
                    count++;
                } while (count < 10);
                input.close();

            }
        } else {
            System.out.println("Bluetooth service not found.");
        }
    }
}
