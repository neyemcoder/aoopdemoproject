package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField messageField;
    @FXML
    private ListView<String> messageList;
    @FXML
    private Button connectButton;

    @FXML


    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;

    @FXML
    public void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(nameField.getText());
                Platform.runLater(() -> connectButton.setDisable(true));

                new Thread(() -> {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            String finalMessage = message;
                            Platform.runLater(() -> messageList.getItems().add("Server: " + finalMessage));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void sendMessage() {
        String message = messageField.getText();
        if (out != null && !message.isEmpty()) {
            out.println(message);
            messageField.clear();
            messageList.getItems().add("Me: " + message);
        }
    }
}
