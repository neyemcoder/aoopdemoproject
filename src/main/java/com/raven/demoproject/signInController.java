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

public class signInController {
    private Stage stage;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML public Button signInButton;
    @FXML private Button signUpButton;


 public void handleSignUp(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
            Scene scene_signUp = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_signUp.getStylesheets().add(css); stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_signUp);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void handleSignIn(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene scene_Profile = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Profile.getStylesheets().add(css); stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Profile);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    }