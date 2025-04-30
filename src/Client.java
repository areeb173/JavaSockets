import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception {
        System.out.println("Client is running: ");

        // Create a socket connection to the server
        Socket clientSocket = new Socket("127.0.0.1", 6789);

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        System.out.println("Enter your name:");
        String name = inFromUser.readLine();
        outToServer.writeBytes(name + '\n');

        while (true) {
            System.out.print("Enter calculation (or 'close' to quit, 'shutdown' to close server): ");
            String calc = inFromUser.readLine();

            // Send the user's input to the server
            outToServer.writeBytes(calc + '\n');

            // If the user enters "close", close the client connection 
            if (calc.equalsIgnoreCase("close")) {
                System.out.println("Closing connection to the server.");
                break;  // Exit the loop, but don't shut down the server
            }
            
            // If the user enters "shutdown", close the client connection and shutdown server
            if (calc.equalsIgnoreCase("shutdown")) {
                System.out.println("Shutting down server and closing connection.");
                break;
            }

            // else, print the server's response to the calculation
            String response = inFromServer.readLine();
            System.out.println("FROM SERVER: " + response);
        }

        // Close socket connection
        clientSocket.close();
    }
}
