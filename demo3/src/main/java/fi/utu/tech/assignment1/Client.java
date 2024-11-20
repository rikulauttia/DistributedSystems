package fi.utu.tech.assignment1;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 12345;

        try(Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Yhdistetty palvelimeen " +serverAddress + ":" + port);

        } catch (IOException e) {
            System.err.println("Yhteyden muodostaminen epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
