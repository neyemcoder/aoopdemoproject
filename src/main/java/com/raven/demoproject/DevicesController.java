package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class  DevicesController {

    Stage stage;

    @FXML
    private Button Profile;

    @FXML
    private Button DashBoard;

    @FXML
    private Button Devices;

    @FXML
    private Button RB1;



    private boolean isConnected = false;

    // Method to handle Profile button action
    public void handleProfile(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene scene_Profile = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Profile.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Profile);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle DashBoard button action
    public void handleDashBoard(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashBoard.fxml"));
            Scene scene_DashBoard = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_DashBoard.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_DashBoard);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Method to handle Device button action
    public void handleDeviceAction(ActionEvent event) {
        try {
            Button clickedButton = (Button) event.getSource();
            toggleConnectionStatus(clickedButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to toggle connection status
    private void toggleConnectionStatus(Button button) {
        isConnected = !isConnected; // Toggle the connection status

        // Update button text based on connection status
        if (isConnected) {
            button.setText("Connected");


        } else {
            button.setText("Disconnected");
        }
    }

    // Method to initialize the controller
    @FXML
    public void initialize() {
        RB1.setOnAction(this::handleDeviceAction);
    }

    public void handleChatButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Chat Window");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
