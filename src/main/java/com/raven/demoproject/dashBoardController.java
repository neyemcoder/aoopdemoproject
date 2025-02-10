package com.raven.demoproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class dashBoardController {

    private Stage stage;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button UpW;

    @FXML
    private Button DownS;

    @FXML
    private Button LeftA;

    @FXML
    private Button RightD;

    @FXML
    private WebView webView1, webView2, webView3, webView4;

    // Navigation methods
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

    // Methods for button actions
    @FXML
    public void scrollUp(ActionEvent event) {
        // Your logic for scrolling up
        System.out.println("Scroll Up");
        // Example: Scroll webView1 up
        webView1.getEngine().executeScript("window.scrollBy(0, -50)");
    }

    @FXML
    public void scrollDown(ActionEvent event) {
        // Your logic for scrolling down
        System.out.println("Scroll Down");
        // Example: Scroll webView1 down
        webView1.getEngine().executeScript("window.scrollBy(0, 50)");
    }

    @FXML
    public void scrollLeft(ActionEvent event) {
        // Your logic for scrolling left
        System.out.println("Scroll Left");
        // Example: Scroll webView1 left
        webView1.getEngine().executeScript("window.scrollBy(-50, 0)");
    }

    @FXML
    public void scrollRight(ActionEvent event) {
        // Your logic for scrolling right
        System.out.println("Scroll Right");
        // Example: Scroll webView1 right
        webView1.getEngine().executeScript("window.scrollBy(50, 0)");
    }

    // Key event handlers
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                UpW.arm(); // Visual effect
                scrollUp(new ActionEvent());
                break;
            case S:
                DownS.arm();
                scrollDown(new ActionEvent());
                break;
            case A:
                LeftA.arm();
                scrollLeft(new ActionEvent());
                break;
            case D:
                RightD.arm();
                scrollRight(new ActionEvent());
                break;
            default:
                break;
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                UpW.disarm(); // Remove visual effect
                break;
            case S:
                DownS.disarm();
                break;
            case A:
                LeftA.disarm();
                break;
            case D:
                RightD.disarm();
                break;
            default:
                break;
        }
    }

    @FXML
    public void initialize() {
        // Ensure the rootPane can receive key events
        Platform.runLater(() -> {
            rootPane.requestFocus();
        });

        // Set key event handlers
        rootPane.setOnKeyPressed(this::handleKeyPressed);
        rootPane.setOnKeyReleased(this::handleKeyReleased);

        // Load content into WebViews as needed
//        webView2.getEngine().load();
        webView3.getEngine().load("/projectSimulator/src/index.html");

        // Set custom user agent for webView1 and load a local URL
//        WebEngine webEngine = webView1.getEngine(".src\\index.html");
//        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//        webEngine.load("http://localhost:1791/src/index.html"); // Change to your local URL
//    }
}
}