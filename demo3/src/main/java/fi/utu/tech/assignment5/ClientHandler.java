package fi.utu.tech.assignment5;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Vastaanotettu viesti: " + message);

                String[] parts = message.split(";");
                if (parts.length >= 2) {
                    String command = parts[1].toUpperCase();
                    String id = (parts.length == 3) ? parts[2] : "default";

                    switch (command) {
                        case "ON":
                            System.out.println("Kytketään lamppu " + id + " PÄÄLLE.");
                            break;
                        case "OFF":
                            System.out.println("Kytketään lamppu " + id + " POIS.");
                            break;
                        case "QUERY":
                            System.out.println("Kyselykomento vastaanotettu.");
                            break;
                        default:
                            System.out.println("Tuntematon komento: " + command);
                            break;
                    }
                } else {
                    System.out.println("Väärässä muodossa oleva viesti: " + message);
                }
            }

        } catch (IOException e) {
            System.err.println("Virhe asiakkaan käsittelyssä: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Virhe suljettaessa asiakkaan socketia: " + e.getMessage());
            }
        }
    }
}
