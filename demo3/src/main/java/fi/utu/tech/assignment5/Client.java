package fi.utu.tech.assignment5;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Yhdistetty palvelimeen " + serverAddress + ":" + port);

            writer.println("LIGHT;ON;3");
            writer.println("LIGHT;OFF;5");
            writer.println("LIGHT;QUERY");
            writer.println("LIGHT;INVALID");
            writer.println("INVALID FORMAT");

            System.out.println("Viestejä lähetetty. Lopetetaan yhteys.");


        } catch (IOException e) {
            System.err.println("Yhteyden muodostaminen epäonnistui: " + e.getMessage());
        }

    }

}
