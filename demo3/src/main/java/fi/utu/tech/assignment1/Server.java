package fi.utu.tech.assignment1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Palvelin odottaa yhteyksiä portissa " + port);

            //odotetaan että asiakas yhdistää
            Socket clientSocket = serverSocket.accept();
            System.out.println("Asiakas yhdistetty: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.err.println("Palvelimessa tapahtui virhe: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
