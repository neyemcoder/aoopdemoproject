package com.raven.demoproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignIn extends Application {
    @Override
    public void start(Stage stage) throws IOException {
       try{ FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource("SignIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366,768);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Raven Project");
        stage.setScene(scene);
        stage.show();
    }
       catch(Exception e) {
           e.printStackTrace();
       }
    }

    public static void main(String[] args) {
        launch();
    }
}
