package main.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import main.app.SerialCommunication;

public class MigawkaController {

    @FXML
    private JFXComboBox<Label> filterCB;
    //filterCB.getItems().add(new Label("Highest"));

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
    //operatingModeCB

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
        String charCalibrationSnapshot = "k <CR>";
        sendByte = convertChatToByte(charCalibrationSnapshot);
        serialCommunication.write(sendByte);
    }

    /*
     * Zamknięcie migawki
     */
    @FXML
    void closeSnapshot(ActionEvent event) {
        String charClosingSnapshot = "c <CR>";
        sendByte = convertChatToByte(charClosingSnapshot);
        serialCommunication.write(sendByte);
    }

    /*
     * Wykonanie ekspozycji
     */
    @FXML
    void executeExposition(ActionEvent event) {
        String charExposition = "b <CR>";
        sendByte = convertChatToByte(charExposition);
        serialCommunication.write(sendByte);
    }

    /*
     * Otwarcie migawki
     */
    @FXML
    void openSnapshot(ActionEvent event) {
        String charOpeningSnapshot = "o <CR>";
        sendByte = convertChatToByte(charOpeningSnapshot);
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw typ filtra
     */
    @FXML
    void setFilterType(ActionEvent event) {
        String filterNumber = "";
        String typeFilter = filterCB.getValue().toString();

        if(typeFilter.equals("Mosiadz 2.04 mm")) {
            filterNumber = "n 0 <CR>";
        }
        else if(typeFilter.equals("Mosiadz 0.68 mm")) {
            filterNumber = "n 1 <CR>";
        }
        else if(typeFilter.equals("Miedz 0.17 mm")) {
            filterNumber = "n 2 <CR>";
        }
        else if(typeFilter.equals("Olow 0.5 mm")) {
            filterNumber = "n 3 <CR>";
        }
        else if(typeFilter.equals("Pustka")) {
            filterNumber = "n 4 <CR>";
        }
        else if(typeFilter.equals("Aluminium 1 mm")) {
            filterNumber = "n 5 <CR>";
        }
        else{
            //
        }

        if (typeFilter != null && !typeFilter.isEmpty()){
            sendByte = convertChatToByte(typeFilter);
            serialCommunication.write(sendByte);
        }
    }

    /*
     * Ustaw czas otwarcia migawki
     */
    @FXML
    void setOpeningTime(ActionEvent event) {
        String openingTimeSnapshot = openingTimeSlider.getIndicatorPosition().toString();
        sendByte = convertChatToByte("t "+openingTimeSnapshot+" <CR>");
        serialCommunication.write(sendByte);
    }

    /*
     * Wybierz tryb pracy
     */

    @FXML
    void setOperatingMode(ActionEvent event) {
        //String numberOperatingMode = "m 0 <CR>";
        String numberOperatingMode = "";
        String operatingMode = operatingModeCB.getValue().toString();

        if(operatingMode.equals("Bierny  nacisniecie przycisku na panelu")) {
            numberOperatingMode= "m 0 <CR>";
        }
        else if(operatingMode.equals("Aktywny przed otwarciem migawki")) {
            numberOperatingMode = "m 1 <CR>";
        }
        else if(operatingMode.equals("Aktywny po otwarciem migawki")) {
            numberOperatingMode= "m 2 <CR>";
        }
        else{}

        if (numberOperatingMode != null && !numberOperatingMode.isEmpty()){
            sendByte = convertChatToByte(numberOperatingMode);
            serialCommunication.write(sendByte);
        }
        else{
            //
        }
    }

    /*
     * Ustaw pozycje stolika
     */
    @FXML
    void setTablePosition(ActionEvent event) {
        String tablePosition = tablePositionSlider.getIndicatorPosition().toString();
        System.out.println(tablePosition);
        sendByte = convertChatToByte("p "+tablePosition+" <CR>");
        serialCommunication.write(sendByte);
    }

    /*
     * Ustaw pozycję stolika na zero
     */
    @FXML
    void setTablePositionToZero(ActionEvent event) {
        String charTablePositiontToZero = "z <CR>";
        sendByte = convertChatToByte(charTablePositiontToZero);
        serialCommunication.write(sendByte);
    }

    public void setPort()
    {
        String portName = "COM3"; // Przykładowa nazwa portu
        serialCommunication.connect(portName);
    }

    public byte[] convertChatToByte(String str)
    {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length];
        for (int i = 0; i < b.length; i++)
        {
            b[i] = (byte) buffer[i];
            System.out.println(b[i]);
        }

        return b;
    }
}