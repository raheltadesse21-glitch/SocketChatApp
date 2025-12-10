import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        // Enter username
        System.out.print(in.readLine()); // "Enter your username: "
        String username = userInput.readLine();
        out.println(username);

        // Thread to read messages from server
        new Thread(() -> {
            String serverMessage;
            try {
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Send messages to server
        String message;
        while ((message = userInput.readLine()) != null) {
            out.println(message);
        }

        socket.close();
    }
}
