package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.*;

public class signUpController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private RadioButton mButton;
    @FXML private RadioButton fButton;

    @FXML
    private Label errorLabel;

    private ToggleGroup genderToggleGroup;

    public void initialize() {
        genderToggleGroup = new ToggleGroup();
        mButton.setToggleGroup(genderToggleGroup);
        fButton.setToggleGroup(genderToggleGroup);
    }

    public void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String phone = phoneNumberField.getText();
        String email = emailField.getText();
        RadioButton selectedGender = (RadioButton) genderToggleGroup.getSelectedToggle();
        String gender = selectedGender != null ? selectedGender.getText() : "";

        // Basic validation checks
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty() || password.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }

        try {
            // Save user details, including password
            DatabaseConnection.saveUser(username, email, phone, gender, password); // You need to update DatabaseUtil to handle password

        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while saving the user.");
        }
    }

    private void showError(String message) {

        errorLabel.setText(message);

        // Optionally show an alert dialog for error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Signup Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
