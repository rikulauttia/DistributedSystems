package fi.utu.tech.assignment3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             OutputStream outputStream = socket.getOutputStream()) {

            System.out.println("Yhdistetty palvelimeen " +serverAddress + ":" + port);

            String message = "Hello";
            byte[] messageBytes = message.getBytes();
            outputStream.write(messageBytes);
            System.out.println("Viesti lähetetty: " + message);

        } catch (IOException e) {
            System.err.println("Yhteyden muodostaminen epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
