package main.app.controllers;

import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import main.app.SerialCommunication;

import java.util.List;

public class SilnikController {

    @FXML
    private Tab silnikTab;

    @FXML
    private Label actualPosLbl;

    @FXML
    private Label targetPosLbl;

    @FXML
    private Label choosePosLbl;

    @FXML
    private Label mvLbl;

    @FXML
    private JFXTextField actualPosField;

    @FXML
    private JFXTextField targetPosField;

    @FXML
    private JFXSlider posSlider;

    @FXML
    private JFXButton mvPlusBtn;

    @FXML
    private JFXButton mvMinusBtn;

    @FXML
    private JFXButton chngPosBtn;

    @FXML
    void chngPos(ActionEvent event) {
        int position = (int) posSlider.getValue();
        System.out.println(position);
        sendJRKcommand(JRKCommands.JRK_SET_TARGET.com, position);
        byte[] status = serialCommunication.readData(2, 1000);
        String value = status.toString();
        actualPosField.setText(value);
    }

    double last_cmd_time;
    public SerialCommunication serialCommunication = new SerialCommunication();

    public SilnikController() {
        openCommunication();
    }

    public enum JRKCommands {
        JRK_SET_TARGET(0xC0);

        private int com;
        JRKCommands(int com){
            this.com = com;
        }
    };

    public void openCommunication(){
        List<String> portList = serialCommunication.getPortNames();
        for(String port: portList) {
            if(port.contains("pololu"))
                serialCommunication.connect(port);
        }
    }

    public boolean sendJRKcommand(int command, int parameter){
        if(serialCommunication == null)
            return false;
        if(!serialCommunication.isConnected())
            return false;
        last_cmd_time = System.currentTimeMillis() - last_cmd_time;
        if(last_cmd_time <= 500000) {
            return false;
        }
        last_cmd_time = System.currentTimeMillis();
        System.out.println(parameter);
        if(command == JRKCommands.JRK_SET_TARGET.com){
            char[] ba = new char[2];
            ba[0] = (char)(0xC0 + (parameter & 0x1F));
            ba[1] = (char)((parameter >> 5) & 0x7F);

            serialCommunication.write(ba);
            return true;
        }
        return false;
    }
}