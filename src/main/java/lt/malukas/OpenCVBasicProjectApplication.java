package main.java.lt.malukas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenCVBasicProjectApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("forms/main.fxml"));
        primaryStage.setTitle("OpenCV Basic Project");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
