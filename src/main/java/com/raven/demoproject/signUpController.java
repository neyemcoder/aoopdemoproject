package com.raven.demoproject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;

public class signUpController {

    static Stage stage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;


    @FXML
    public Button getSignUpButton;

    @FXML
    private Button backButton;

    public void initialize() {
        // Handle sign-in button click
        getSignUpButton.setOnAction(event -> handleSignUp());


    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Perform sign-in logic here
        System.out.println("Username: " + username + ", Password: " + password);
    }
    public void handleBack(ActionEvent event) throws IOException {
        try { FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Scene scene_signIn = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_signIn.getStylesheets().add(css);
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_signIn);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }


    @FXML
    private RadioButton mButton, fButton;

    @FXML
    private Label myLabel;
    public void getGender(ActionEvent event) {

        if(mButton.isSelected()) {
            myLabel.setText(mButton.getText());
        }
        else if(fButton.isSelected()) {
            myLabel.setText(fButton.getText());

        }
    }

}


