import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

            // Read the client's name and log the connection time
            String name = inFromClient.readLine();
            System.out.println("Connected with: " + name);
            Server.trackUserConnection(name);  // Track connection

            String mathExpression;
            while ((mathExpression = inFromClient.readLine()) != null) {
                if (mathExpression.equalsIgnoreCase("close")) {
                    System.out.println("Client " + name + " disconnected.");
                    outToClient.writeBytes("Connection closed.\n");
                    break;
                } else if (mathExpression.equalsIgnoreCase("shutdown")) {
                    System.out.println("Client " + name + " requested shutdown.");
                    outToClient.writeBytes("Shutting down server.\n");
                    Server.shutdownServer(); // Calls shutdown method in Server
                    break;
                }

                try {
                    // Evaluate expression
                    double result = Calculator.evaluate(mathExpression);
                    outToClient.writeBytes(result + "\n");
                } catch (IllegalArgumentException e) {
                    outToClient.writeBytes("Error: " + e.getMessage() + "\n");
                }
            }

            // Log user disconnection and duration
            Server.logUserDisconnect(name);
            socket.close();

        } catch (IOException e) {
            System.err.println("IOException in ClientHandler: " + e.getMessage());
        }
    }
}
