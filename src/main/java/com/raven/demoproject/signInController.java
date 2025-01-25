package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class signInController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button signInButton;
    @FXML private Button signUpButton;

    private Stage stage;

    // Database connection
    private Connection connection;

    public signInController() {
        // Initialize the connection to your MySQL database
        connection = DatabaseConnection.getConnection(); // Ensure this is configured properly
    }

    // Handle the Sign Up button click
    public void handleSignUp(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
            Scene scene_signUp = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_signUp.getStylesheets().add(css);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_signUp);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle the Sign In button click and validate user credentials
    public void handleSignIn(ActionEvent event) throws IOException {
        String email = usernameField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in both email and password.");
            return;
        }

        if (validateLogin(email, password)) {
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
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
        }
    }

    // Validate user login credentials
    private boolean validateLogin(String email, String password) {
        if (connection == null) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to connect to the database.");
            return false;
        }

        String query = "SELECT 1 FROM users WHERE email = ? AND user_password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, hashPassword(password));
            ResultSet resultSet = statement.executeQuery();

            // If a record is found, return true (valid user)
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while validating credentials.");
        }
        return false; // Invalid credentials
    }

    // Hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Show alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Optional: You can add a method to close the connection to the database if needed
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
