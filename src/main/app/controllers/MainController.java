package main.app.controllers;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
	
	@FXML
	private SilnikController sc;
	
	public SilnikController getSC(){
		return sc;
	}
	
    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    public void initialize() {
    	borderPane.setCenter(tabPane);
    }
    
    
}