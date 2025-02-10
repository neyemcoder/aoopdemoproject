package com.raven.demoproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class ProfileController {

    @FXML
    private Button profileButton;
    @FXML
    private Button dashBoardButton;
    @FXML
    private Button ChatButton;
    @FXML
    private Button devicesButton;

    @FXML
    private Button logOut;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField genderField;
    private Stage stage;

    // Getter and setter for nameField
    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(String name) {
        this.nameField.setText(name);
    }

    // Getter and setter for emailField
    public TextField getEmailField() {
        return emailField;
    }

    public void setEmailField(String email) {
        this.emailField.setText(email);
    }

    // Getter and setter for phoneField
    public TextField getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(String phone) {
        this.phoneField.setText(phone);
    }

    // Getter and setter for genderField
    public TextField getGenderField() {
        return genderField;
    }

    public void setGenderField(String gender) {
        this.genderField.setText(gender);
    }

    // Method to set the profile using the email
    public void setProfile(String email) {
        try {
            // Fetch user data from the database
            ResultSet resultSet = DatabaseConnection.getUserByEmail(email);

            // Check if a result is returned
            if (resultSet.next()) {
                // Set the fields from the database values
                setNameField(resultSet.getString("name"));
                setEmailField(resultSet.getString("email"));
                setPhoneField(resultSet.getString("phone"));
                setGenderField(resultSet.getString("gender"));
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLogOut(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Scene scene_Devices = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Devices.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Devices);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleDevices(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("devices.fxml"));
            Scene scene_Devices = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Devices.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Devices);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleChatButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client.fxml"));
            Parent root = fxmlLoader.load();
            Stage chatStage = new Stage();
            chatStage.setTitle("Chat Window");
            chatStage.setScene(new Scene(root));
            chatStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleDashBoard(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashBoard.fxml"));
            Scene scene_Devices = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Devices.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Devices);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
}}
