package fi.utu.tech.assignment4;


import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Yhdistetty palvelimeen " +serverAddress + ":" + port);
            writer.println("Hello");
            System.out.println("Viesti lähetetty: Hello");

            String response = reader.readLine();
            System.out.println("Palvelimen vastaus: " + response);

            if ("Ack".equalsIgnoreCase(response)) {
                System.out.println("Varmistus saatu.");
            }

            writer.println("quit");
            System.out.println("Viesti lähetetty: quit");

        } catch (IOException e) {
            System.err.println("Yhteyden muodostaminen epäonnistui: " + e.getMessage());
        }

    }

}
