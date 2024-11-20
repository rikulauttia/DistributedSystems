package fi.utu.tech.assignment6;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Yhdistetty palvelimeen " + serverAddress + ":" + port);

            writer.println("LIGHT;ON;2");
            writer.println("LIGHT;OFF;3");
            writer.println("LIGHT;QUERY");

            String response = reader.readLine();
            System.out.println("Palvelimen vastaus: " + response);

        } catch (IOException e) {
            System.err.println("Yhteyden muodostaminen epäonnistui: " + e.getMessage());
        }
    }
}
