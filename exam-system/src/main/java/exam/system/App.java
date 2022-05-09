package exam.system;

import java.io.IOException;

import exam.system.models.Client;

/**
 * Main app
 *
 */
public class App {

    public static void main(String[] args) {

        // If no assets than create
        try {
            new FileService().createAssets();
        } catch (IOException e) {
            System.err.println("Nepavyko sukurti programos failu.");
            return;
        }

        // Services
        ScannerService scannerService = new ScannerService();
        ClientService clientService = new ClientService(scannerService);

        while (true) {
            // Actions - login, register and ect.
            int action = clientService.scanAction();

            // Close the application
            if (action == 3)
                break;

            // Scan user nickname and password
            Client client = clientService.scanData();

            // Login
            if (action == 1)
                clientService.login(client.getNickname(), client.getPassword());

            // Register
            if (action == 2)
                clientService.register(client.getNickname(), client.getPassword());
        }

        // Close scanner
        scannerService.close();

        // System.exit(0);
    }

}
