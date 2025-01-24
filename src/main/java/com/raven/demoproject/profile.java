package com.raven.demoproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class profile extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(profile.class.getResource("profile.fxml"));
            Scene scene_profile = new Scene(fxmlLoader.load(), 1366, 768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_profile.getStylesheets().add(css);
            stage.setTitle("Raven Project");
            stage.setScene(scene_profile);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
