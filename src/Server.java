import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static ServerSocket serverSocket;
    private static Map<String, Long> userConnectionTimes = new HashMap<>(); // To store user connection times
    private static Map<String, String> userConnectDates = new HashMap<>(); // To store connection date and time

    public static void main(String[] args) {
        try {
            // Run server
            serverSocket = new ServerSocket(6789);
            System.out.println("Server is up and running");
            
            // Listen for connections
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected");

                    // Handle the connection in a separate thread
                    ClientHandler handler = new ClientHandler(clientSocket);
                    new Thread(handler).start();
                } catch (SocketException e) {
                    System.out.println("Server socket closed. Shutting down.");
                    break; // Exit loop and end server
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void shutdownServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }

    // Write user data to log file
    public static void logUserDisconnect(String userName) {
        if (userConnectionTimes.containsKey(userName)) {
            long connectionTime = userConnectionTimes.get(userName);
            long disconnectTime = System.currentTimeMillis();
            long duration = disconnectTime - connectionTime;

            String connectTimeString = userConnectDates.get(userName);
            // System.out.println("User " + userName + " connected at " + connectTimeString + " and connected for " + duration / 1000 + " seconds.");

            // Log to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_connections.txt", true))) {
                writer.write("User: " + userName + ", Connected at: " + connectTimeString + ", Disconnected at: " + new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date(disconnectTime)) + ", Duration: " + duration / 1000 + " seconds\n");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    // Track connect/disconnect times
    public static void trackUserConnection(String userName) {
        long currentTime = System.currentTimeMillis();
        userConnectionTimes.put(userName, currentTime);

        // Store the connection time with MM-dd-yyyy format
        String formattedTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date(currentTime));
        userConnectDates.put(userName, formattedTime);
    }
}
