import java.io.*;
import java.net.*;

class Server {

    public static void main(String argv[]) throws Exception {
        String mathExpression;

        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server is UP and running!");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected.");

            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream outToClient =
                    new DataOutputStream(connectionSocket.getOutputStream());

            String name = inFromClient.readLine();
            System.out.println("Connected with: " + name);

            while (true) {
                mathExpression = inFromClient.readLine();
                if (mathExpression == null || mathExpression.equalsIgnoreCase("close")) {
                    System.out.println("Client disconnected.");
                    break;
                }

                try {
                    double result = Calculator.evaluate(mathExpression);
                    outToClient.writeBytes(result + "\n");
                } catch (IllegalArgumentException e) {
                    outToClient.writeBytes("Error: " + e.getMessage() + "\n");
                }
            }

            connectionSocket.close();
        }
    }
}
