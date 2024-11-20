package fi.utu.tech.assignment4;

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
                System.out.println("Asiakkaalta vastaanotettu viesti: " + message);

                if (message.equalsIgnoreCase("Hello")) {
                    writer.println("Ack");
                } else if (message.equalsIgnoreCase("quit")) {
                    System.out.println("Asiakas lopetti yhteyden.");
                    break;
                } else {
                    writer.println("Unknown command");
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

