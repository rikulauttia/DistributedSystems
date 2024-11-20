package fi.utu.tech.assignment2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;

public class Server {

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Palvelin odottaa yhteyksiä portissa " + port);

            try (Socket clientSocket = serverSocket.accept();
                 InputStream inputStream = clientSocket.getInputStream()) {

                System.out.println("Asiakas yhdistetty: " + clientSocket.getInetAddress());

                byte[] receivedBytes = inputStream.readAllBytes();
                String message = new String(receivedBytes);
                System.out.println("Vastaanotettu viesti: " + message);

            } catch (IOException e) {
                System.err.println("Virhe käsiteltäessä asiakkaan yhteyttä: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Palvelimessa tapahtui virhe: " + e.getMessage());
        }
    }
}