import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    public void run() {
        try (
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream())
        ) {
            // Read client name and log connection time 
            String name = inFromClient.readLine();
            System.out.println("Connected with: " + name);
            Server.trackUserConnection(name);   // Track connection

            String mathExpression;
            while ((mathExpression = inFromClient.readLine()) != null) {
                if ("close".equalsIgnoreCase(mathExpression)) {
                    System.out.println("Client " + name + " disconnected.");
                    outToClient.writeBytes("Connection closed.\n");
                    break;

                } else if ("shutdown".equalsIgnoreCase(mathExpression)) {
                    System.out.println("Client " + name + " requested shutdown.");

                    // Send the shutdown acknowledgement first
                    outToClient.writeBytes("Shutting down server.\n");
                    outToClient.flush();

                    //  close the ServerSocket so no new accepts happen
                    Server.shutdownServer();
                    break;
                }

                // Normal calculation path
                try {
                    double result = Calculator.evaluate(mathExpression);
                    outToClient.writeBytes(result + "\n");
                } catch (IllegalArgumentException e) {
                    outToClient.writeBytes("Error: " + e.getMessage() + "\n");
                }
            }

            // Log disconnect and clean up
            Server.logUserDisconnect(name);

        } catch (IOException e) {
            System.err.println("IOException in ClientHandler: " + e.getMessage());
        } finally {
            // Always close the client socket
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
