package fi.utu.tech.assignment3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream()) {
            byte[] receivedBytes = inputStream.readAllBytes();
            String message = new String(receivedBytes);
            System.out.println("Asiakkaalta vastaanotettu viesti: " + message);
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

