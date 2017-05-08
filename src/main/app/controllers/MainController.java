package main.app.controllers;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    public void initialize() {
        try{
            Tab migawkaTab = FXMLLoader.load(getClass().getResource("/resources/Migawka.fxml"));
            Tab silnikTab = FXMLLoader.load(getClass().getResource("/resources/Silnik.fxml"));
            Tab detektorTab = FXMLLoader.load(getClass().getResource("/resources/Detektor.fxml"));

            tabPane.getTabs().add(migawkaTab);
            tabPane.getTabs().add(silnikTab);
            tabPane.getTabs().add(detektorTab);

            borderPane.setCenter(tabPane);
        }catch(IOException ioe){

        }
    }
}