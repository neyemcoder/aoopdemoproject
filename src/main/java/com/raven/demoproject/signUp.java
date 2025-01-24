package com.raven.demoproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class signUp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(signUp.class.getResource("signUp-view.fxml"));
            Scene scene_signUp = new Scene(fxmlLoader.load(), 1366,768);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene_signUp.getStylesheets().add(css);
            stage.setTitle("Raven Project");
            stage.setScene(scene_signUp);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}