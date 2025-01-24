package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerController {
    @FXML
    private ListView<String> clientList;
    @FXML
    private Button startServerButton;
    @FXML
    private TextArea serverMessageField;
    @FXML
    private TextArea messageArea; // Add this field

    private static final int PORT = 12345;
    private Map<Socket, ClientHandler> clients = new HashMap<>();

    @FXML
    public void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                Platform.runLater(() -> startServerButton.setDisable(true));
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.put(clientSocket, clientHandler);
                    clientHandler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void sendMessage() {
        String message = serverMessageField.getText();
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.sendMessage("Server: " + message);
        }
        serverMessageField.clear();
        appendMessage("Server: " + message); // Display the server's own message
    }

    private void appendMessage(String message) {
        Platform.runLater(() -> messageArea.appendText(message + "\n"));
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                final String clientName = in.readLine();
                Platform.runLater(() -> clientList.getItems().add(clientName));
                String message;
                while ((message = in.readLine()) != null) {
                    final String msg = message; // Create a final variable for the lambda expression
                    Platform.runLater(() -> messageArea.appendText(clientName + ": " + msg + "\n")); // Use the final variable
                    System.out.println("Received from " + clientName + ": " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

}
