package fi.utu.tech.assignment4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Palvelin odottaa yhteyksiä portissa " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Asiakas yhdistetty: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }

        } catch (IOException e) {
            System.err.println("Palvelimessa tapahtui virhe: " + e.getMessage());
        }
    }
}