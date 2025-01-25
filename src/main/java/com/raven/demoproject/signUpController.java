package com.raven.demoproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class signUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private RadioButton mButton;

    @FXML
    private RadioButton fButton;

    @FXML
    private Label getMyLabel;

    private ToggleGroup genderToggleGroup;

    public void initialize() {
        // Create a ToggleGroup for gender radio buttons
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

        System.out.println(username);
        System.out.println(password);
        System.out.println(phone);
        System.out.println(email);
        System.out.println(gender);

        // Validation logic
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            getMyLabel.setText("All fields must be filled!");
            return;
        }

        if (insertUser(username, password, phone, email, gender)) {
            getMyLabel.setText("User signed up successfully!");
        } else {
            getMyLabel.setText("Sign-up failed. Please try again.");
        }
    }

    private boolean insertUser(String username, String password, String phone, String email, String gender) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Database connection details
            String url ="jdbc:mysql://localhost:3306/revansquad?useSSL=false&allowPublicKeyRetrieval=true";;
            String dbUsername = "nayem";
            String dbPassword = "12345678";

            connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Insert query (omit `id` as it's auto-increment)
            String query = "INSERT INTO users (user_name, email, user_password, phone, gender) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, gender);

            // Execute query
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
