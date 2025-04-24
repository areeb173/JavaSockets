import java.io.*;
import java.net.*;

class Client {

    public static void main(String argv[]) throws Exception {
        System.out.println("Client is running: ");

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
            System.out.print("Enter calculation (or 'close' to quit): ");
            String calc = inFromUser.readLine();

            outToServer.writeBytes(calc + '\n');

            if (calc.equalsIgnoreCase("close")) {
                System.out.println("Closing connection.");
                break;
            }

            String response = inFromServer.readLine();
            System.out.println("FROM SERVER: " + response);
        }

        clientSocket.close();
    }
}
