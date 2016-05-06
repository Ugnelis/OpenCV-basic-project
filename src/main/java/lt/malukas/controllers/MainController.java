package main.java.lt.malukas.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    ImageView imageView1;

    @FXML
    Button openFileButton;

    @FXML
    Button runButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
    }

    public void handleOpenFileButtonAction(ActionEvent actionEvent) {
    }

    public void handleRunButtonAction(ActionEvent actionEvent) {
    }
}
