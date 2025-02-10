package com.raven.demoproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in both email and password.");
            return;
        }

        try {
            // Database connection and query
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users2 WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // On successful login, show the profile window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                ProfileController profileController = loader.getController();
                profileController.setProfile(email); // Pass email to set the profile

                // Show the profile stage
                stage.show();

                // Close the current login window
                Stage loginStage = (Stage) emailField.getScene().getWindow();
                loginStage.close();
            } else {
                showError("Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred during login.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);

        // Optionally show an alert dialog for error
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage chatStage = new Stage();
            chatStage.setTitle("Chat Window");
            chatStage.setScene(new Scene(root));
            chatStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
