package com.raven.demoproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Devices extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(profile.class.getResource("Devices.fxml"));
            Scene scene_Devices = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_Devices.getStylesheets().add(css);
            stage.setTitle("Raven Project");
            stage.setScene(scene_Devices);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
