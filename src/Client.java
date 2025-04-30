import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("Client is running:");

        try (
            // Create a socket connection to the server
            Socket clientSocket = new Socket("127.0.0.1", 6789);
            BufferedReader inFromUser   = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer= new DataOutputStream(clientSocket.getOutputStream())
        ) {
            // Send user name
            System.out.println("Enter your name:");
            String name = inFromUser.readLine();
            outToServer.writeBytes(name + "\n");

            while (true) {
                System.out.print("Enter calculation (or 'close' to quit, 'shutdown' to close server): ");
                String calc = inFromUser.readLine();
                
                // Send the user's input to the server
                outToServer.writeBytes(calc + "\n");
                // If the user enters "close", close the client connection 
                if ("close".equalsIgnoreCase(calc)) {
                    System.out.println("Closing connection to the server.");
                    break;
            // If the user enters "shutdown", close the client connection and shutdown server
                } else if ("shutdown".equalsIgnoreCase(calc)) {
                    // Try to read the server's final acknowledgment (may get reset if ordering is wrong)
                    try {
                        String serverReply = inFromServer.readLine();
                        if (serverReply != null) {
                            System.out.println("FROM SERVER: " + serverReply);
                        }
                    } catch (SocketException ignored) {
                        // connection was reset before we could read
                    }
                    System.out.println("Shutting down server and closing connection.");
                    break;
                }

                // Normal response path
                String response = inFromServer.readLine();
                System.out.println("FROM SERVER: " + response);
            }
        }

       
    }
}
