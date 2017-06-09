package main.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import main.app.SerialCommunication;

import java.net.URL;
import java.util.ResourceBundle;

public class MigawkaController implements Initializable {

    @FXML
    private JFXComboBox<String> filterCB;

    @FXML
    private JFXButton filterBtn;

    @FXML
    private JFXSlider openingTimeSlider;

    @FXML
    private JFXButton openingTimeBtn;

    @FXML
    private JFXButton operatingModeBtn;

    @FXML
    private JFXComboBox<String> operatingModeCB;

    @FXML
    private JFXSlider tablePositionSlider;

    @FXML
    private JFXButton tablePositionBtn;

    public byte[] sendByte = new byte[10];
    public SerialCommunication serialCommunication = new SerialCommunication();

    /*
     * Kalibracja migawki
     */
    @FXML
    void calibrateSnapshot(ActionEvent event) {
        char[] calibration = new char[2];
        calibration[0] = 'k';
        calibration[1] = '\r';

        sendByte = convertCharToByte(calibration);
        serialCommunication.write(sendByte);
    }

    /*
     * Zamknięcie migawki
     */
    @FXML
    void closeSnapshot(ActionEvent event) {
        char[] csnapshot = new char[2];
        csnapshot[0] = 'c';
        csnapshot[1] = '\r';
        
        sendByte = convertCharToByte(csnapshot);
        serialCommunication.write(sendByte);
    }

    /*
     * Wykonanie ekspozycji
     */
    @FXML
    void executeExposition(ActionEvent event) {
        char[] blink = new char[2];
        blink[0] = 'b';
        blink[1] = '\r';
        
        sendByte = convertCharToByte(blink);
        serialCommunication.write(sendByte);
    }

    /*
     * Otwarcie migawki
     */
    @FXML
    void openSnapshot(ActionEvent event) {
        char[] snapshot = new char[2];
        snapshot[0] = 'o';
        snapshot[1] = '\r';
        
        sendByte = convertCharToByte(snapshot);
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw typ filtra
     */
    @FXML
    void setFilterType(ActionEvent event) {
        char[] filter = new char[6];
        filter[0] = 'n';
        filter[1] = ' ';
        filter[2] = '{';
        
        String typeFilter = filterCB.getValue().toString();

        if(typeFilter.equals("Mosiadz 2.04 mm")) {
        	filter[3] = '0';
        }
        else if(typeFilter.equals("Mosiadz 0.68 mm")) {
        	filter[3] = '1';
        }
        else if(typeFilter.equals("Miedz 0.17 mm")) {
        	filter[3] = '2';
        }
        else if(typeFilter.equals("Olow 0.5 mm")) {
        	filter[3] = '3';
        }
        else if(typeFilter.equals("Pustka")) {
        	filter[3] = '4';
        }
        else if(typeFilter.equals("Aluminium 1 mm")) {
        	filter[3] = '5';
        }
        
        filter[4] = '}';
        filter[5] = '\r';
        
        sendByte = convertCharToByte(filter);
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw czas otwarcia migawki
     */
    @FXML
    void setOpeningTime(ActionEvent event) {
        char[] time = new char[6];
        time[0] = 't';
        time[1] = ' ';
        time[2] = '{';
        
        // odczyt ze slidera
        time[3] = openingTimeSlider.getIndicatorPosition().toString().charAt(0);
        
        time[4] = '}';
        time[5] = '\r';
        
        sendByte = convertCharToByte(time);
        serialCommunication.write(sendByte);
    }

    /*
     * Wybierz tryb pracy
     */
    @FXML
    void setOperatingMode(ActionEvent event) {
    	char[] operation = new char[6];
    	operation[0] = 'm';
    	operation[1] = ' ';
    	operation[2] = '{';
        
    	String operatingMode = operatingModeCB.getValue().toString();

        if(operatingMode.equals("Bierny naciśnięcie przycisku na panelu")) {
            operation[3] = '0';
        }
        else if(operatingMode.equals("Aktywny przed otwarciem migawki")) {
            operation[3] = '1';
        }
        else if(operatingMode.equals("Aktywny po otwarciu migawki")) {
            operation[3] = '2';
        }
        
        operation[4] = '}';
        operation[5] = '\r';
        
        sendByte = convertCharToByte(operation);
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw pozycje stolika
     */
    @FXML
    void setTablePosition(ActionEvent event) {
        char[] position = new char[6];
        position[0] = 'p';
        position[1] = ' ';
        position[2] = '{';

        // odczyt ze slidera
        position[3] = tablePositionSlider.getIndicatorPosition().toString().charAt(0);

        position[4] = '}';
        position[5] = '\r';

        sendByte = convertCharToByte(position);
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw pozycję stolika na zero
     */
    @FXML
    void setTablePositionToZero(ActionEvent event) {
    	char[] zero = new char[2];
    	zero[0] = 'z';
    	zero[1] = '\r';

    	sendByte = convertCharToByte(zero);
        serialCommunication.write(sendByte);
    }

    public void setPort(String s)
    {
        String portName = s;
        serialCommunication.connect(portName);
    }

    public byte[] convertCharToByte(char[] buffer)
    {
        byte[] b = new byte[buffer.length];
        for (int i = 0; i < b.length; i++)
            b[i] = (byte) buffer[i];

        return b;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // migawka jest na COM1
        setPort("COM1");

        // dodanie elementów do listy filtrów
        filterCB.getItems().addAll("Mosiądz 2.04 mm",
        		"Mosiądz 0.68 mm",
        		"Miedź 0.17 mm",
        		"Ołów 0.5 mm",
        		"Pustka",
        		"Aluminium 1 mm");

        // dodanie elementów do listy pracy
        operatingModeCB.getItems().addAll("Bierny naciśnięcie przycisku na panelu",
        		"Aktywny przed otwarciem migawki",
        		"Aktywny po otwarciu migawki");
    }
}