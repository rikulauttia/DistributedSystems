package fi.utu.tech.assignment6;

import java.io.*;
import java.net.Socket;


public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Hub hub;

    public ClientHandler(Socket clientSocket, Hub hub) {
        this.clientSocket = clientSocket;
        this.hub = hub;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Vastaanotettu viesti: " + message);

                String[] parts = message.split(";");
                if (parts.length >= 2) {
                    String command = parts[1].toUpperCase();
                    int id = parts.length == 3 ? Integer.parseInt(parts[2]) : -1;

                    switch (command) {
                        case "ON":
                            hub.turnOnLight(id);
                            break;
                        case "OFF":
                            hub.turnOffLight(id);
                            break;
                        case "QUERY":
                            StringBuilder response = new StringBuilder();
                            for (Light light : hub.getLights()) {
                                response.append(light.getId()).append(":")
                                        .append(light.isPowerOn() ? "ON" : "OFF").append(";");
                            }
                            writer.println(response.toString());
                            break;
                        default:
                            writer.println("UNKNOWN COMMAND");
                            break;
                    }
                } else {
                    writer.println("INVALID FORMAT");
                }
            }

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
