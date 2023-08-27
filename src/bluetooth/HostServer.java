package bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.obex.*;
import java.io.IOException;
import java.io.InputStream;

public class HostServer {

        static String serverUUID;// = "70F395A4FF5E";//
        static String message="test";

   public static void main(String[] args) {
       HostServer hs=new HostServer();
       hs.startServer();
   }

        public void startServer(){
            try{
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
            //get device local bluetooth address
            serverUUID=LocalDevice.getLocalDevice().getBluetoothAddress();


            SessionNotifier serverConnection = (SessionNotifier) Connector.open("btgoep://localhost:"
                    + serverUUID + ";name=ChatApp");

            int count = 0;
            while(count < 2) {
                RequestHandler handler = new RequestHandler();
                serverConnection.acceptAndOpen(handler);
                System.out.println("Received OBEX connection " + (++count));
            }
            }catch (BluetoothStateException e){
                System.err.println(e);
            }catch (IOException e){
                System.err.println(e);
            }
        }
        public String getMessage(){
            return message;
        }

        private static class RequestHandler extends ServerRequestHandler {
            public int onPut(Operation op) {
                try {
                    System.out.println("in request handler");
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

                    //send buff.toString() to text file
                    System.out.println("got:" + buf.toString());
                    message=buf.toString();


                    op.close();
                    return ResponseCodes.OBEX_HTTP_OK;
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
                }
            }
        }
    }
