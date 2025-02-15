package com.raven.demoproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(new Scene(root, 341, 431));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
