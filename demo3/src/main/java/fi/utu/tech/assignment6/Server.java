package fi.utu.tech.assignment6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 12345;
        Hub hub = new Hub();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Palvelin odottaa yhteyksiä portissa " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Asiakas yhdistetty: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, hub);
                clientHandler.start();
            }

        } catch (IOException e) {
            System.err.println("Palvelimessa tapahtui virhe: " + e.getMessage());
        }
    }
}