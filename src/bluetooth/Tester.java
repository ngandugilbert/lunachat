package bluetooth;



import java.util.ArrayList;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        SPPClient client = new SPPClient();

        // Set up listeners for events
        client.setOnDeviceDiscovery(e -> {
            ArrayList<RemoteDeviceInfo> deviceInfos = client.getDeviceInfos();
            System.out.println("Discovered Devices:");
            for (int i = 0; i < deviceInfos.size(); i++) {
                System.out.println((i + 1) + ". " + deviceInfos.get(i));
            }
        });

        client.setOnConnectionSuccessful(e -> {
            System.out.println("Connection successful!");
            // You can perform further actions here after successful connection
            
        });

        client.setOnConnectionFailed(e -> {
            System.out.println("Connection failed!");
            // You can perform further actions here after failed connection
        });

        // Start device discovery
        client.startDiscovery();

        // Wait for device discovery to complete
        try {
            Thread.sleep(5000); // Adjust the time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Prompt user to choose a device
        ArrayList<RemoteDeviceInfo> deviceInfos = client.getDeviceInfos();
        if (deviceInfos.isEmpty()) {
            System.out.println("No devices found.");
            System.exit(0);
        }

        System.out.println("Choose a device by entering its index:");
        for (int i = 0; i < deviceInfos.size(); i++) {
            System.out.println((i + 1) + ". " + deviceInfos.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        int chosenIndex = scanner.nextInt();
        scanner.close();

        if (chosenIndex < 1 || chosenIndex > deviceInfos.size()) {
            System.out.println("Invalid choice.");
            System.exit(0);
        }

        // Connect to the chosen device
        client.connect(chosenIndex - 1);

        // Wait for connection to complete
        try {
            Thread.sleep(5000); // Adjust the time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Further actions can be performed here after successful connection
    }
}

