package com.raven.demoproject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.Pane;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfileController {
    Stage stage;
    @FXML
    private TextField locationField;
    @FXML
    private TextField mobileNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField creditBalanceField;
    @FXML
    public Button LogOutButton;
    @FXML
    public Button changePassword;
    @FXML
    public Button chatBox;
    @FXML
    private Pane patternPane; // Add this to connect with the FXML placeholder

    public void LogOut(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Scene scene_Profile = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Profile.getStylesheets().add(css); stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Profile);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void handleDashBoard(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashBoard.fxml"));
            Scene scene_Profile = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Profile.getStylesheets().add(css); stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Profile);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void handleDevices(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Devices.fxml"));
            Scene scene_Profile = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Profile.getStylesheets().add(css); stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Raven Project");
            stage.setScene(scene_Profile);
            stage.show();
        }
        catch (Exception e) { e.printStackTrace(); }
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
