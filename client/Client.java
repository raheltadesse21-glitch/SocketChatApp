import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {

    private String username;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public Client() {

        username = JOptionPane.showInputDialog(null, "Enter your username:", 
                                               "Chat Login", JOptionPane.PLAIN_MESSAGE);

        if (username == null || username.trim().isEmpty()) {
            System.exit(0);
        }

        setupGUI();
        connectToServer();
        listenForMessages();
    }

    // ---- FRONTEND (GUI) ----
    private void setupGUI() {
        frame = new JFrame("Chat App - " + username);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    // ---- BACKEND CONNECTION ----
    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 5000);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(username);  // send username to server

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
            System.exit(0);
        }
    }

    // ---- SEND MESSAGE ----
    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            writer.println(msg);
            inputField.setText("");
        }
    }

    // ---- RECEIVE MESSAGES ----
    private void listenForMessages() {
        Thread thread = new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    chatArea.append(line + "\n");
                }
            } catch (Exception e) {
                chatArea.append("Disconnected.\n");
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
