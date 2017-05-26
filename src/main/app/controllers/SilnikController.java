package main.app.controllers;

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

    public SerialCommunication serialCommunication = new SerialCommunication();

    public enum JRKCommands {
        JRK_GET_TARGET(0xA3),
        JRK_GET_FEEDBACK(0xA7),
        JRK_GET_STATUS(0xB3),
        JRK_SET_TARGET(0xC0),
        JRK_SET_MOTOROFF(0xFF);

        private int com;
        JRKCommands(int com){
            this.com = com;
        }
    };

    public enum JRKStatus{
        JRK_STATUS_AWAITING     (0x0001),
        JRK_STATUS_NOPOWER      (0x0002),
        JRK_STATUS_DRVERR       (0x0004),
        JRK_STATUS_INVINPUT     (0x0008),
        JRK_STATUS_DISCINPUT    (0x0010),
        JRK_STATUS_DISCFEEDBACK (0x0020),
        JRK_STATUS_OVERCURRENT  (0x0040),
        JRK_STATUS_SERERROR     (0x0080),
        JRK_STATUS_SEROVERRUN   (0x0100),
        JRK_STATUS_RXOVERRUN    (0x0200),
        JRK_STATUS_CRCERR       (0x0400),
        JRK_STATUS_PROTOERR     (0x0800),
        JRK_STATUS_TIMEOUT      (0x1000),
        JRK_STATUS_RESERVED     (0x7000);

        private int com;
        JRKStatus(int com){
            this.com = com;
        }
    };
    public void openCommunication(){
        List<String> portNames;
        portNames = serialCommunication.getPortNames();
        serialCommunication.connect(portNames.get(1));
    }
    /* public String jrkStatusToText(int value){
         String out = new String;
         if (value & JRKStatus.JRK_STATUS_AWAITING)
             out += "Awaiting command\n";
         if (value&JRKStatus.JRK_STATUS_NOPOWER)
             out+="No motor power\n";
         if (value&JRKStatus.JRK_STATUS_DRVERR)
             out+="Motor driver error\n";
         if (value&JRKStatus.JRK_STATUS_INVINPUT)
             out+="Invalid input\n";
         if (value&JRKStatus.JRK_STATUS_DISCINPUT)
             out+="Input cable disconnected\n";
         if (value&JRKStatus.JRK_STATUS_DISCFEEDBACK)
             out+="Feedback cable disconnected\n";
         if (value&JRKStatus.JRK_STATUS_OVERCURRENT)
             out+="Overcurrent\n";
         if (value&(JRKStatus.JRK_STATUS_SERERROR|JRKStatus.JRK_STATUS_SEROVERRUN|JRKStatus.JRK_STATUS_RXOVERRUN|JRKStatus.JRK_STATUS_CRCERR|JRKStatus.JRK_STATUS_PROTOERR|JRKStatus.JRK_STATUS_TIMEOUT))
             out+="Serial communication error\n";
         return out;
     }
     */
    /*
    public bool sendJRKcommand(int command, int parameter){
        if(command == JRKCommands.JRK_SET_TARGET){

        }
    }*/
}
