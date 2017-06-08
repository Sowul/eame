package main.app.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.util.Duration;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import main.app.SerialCommunication;

import java.awt.event.ActionListener;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private JFXButton readDataBtn;
    
    private Timer timer;
    
    @FXML 
    void readDataPololu(ActionEvent event) {
    	//dupa
    }
    
    public SilnikController getSilnik(){
    	return this;
    }
    
    @FXML
    void chngPos(ActionEvent event) {
    	targetPosField.setText(posSlider.getValue()+"");
        int position = (int) posSlider.getValue();
        System.out.println(position);
        sendJRKcommand(JRKCommands.JRK_SET_TARGET.com, position);                
    }
    
    @FXML
    void chngPlus(){
    	posSlider.setValue(posSlider.getValue() + 100);
    }
    
    @FXML
    void chngMinus(){
    	posSlider.setValue(posSlider.getValue() - 100);
    }

    double last_cmd_time;
    public SerialCommunication serialCommunication = new SerialCommunication();
    
    public SilnikController() {
        openCommunication();
        int i = 0;
        Timeline tmline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("yo co pol sekundy");
                sendJRKcommand(JRKCommands.JRK_GET_FEEDBACK.com, 0);
            }
        }));
        tmline.setCycleCount(Timeline.INDEFINITE);
        tmline.play();
    }
    
    public enum JRKCommands {
        JRK_SET_TARGET(0xC0),
    	JRK_GET_FEEDBACK(0xA7);
    	
        private int com;
        JRKCommands(int com){
            this.com = com;
        }
    };

    public void openCommunication(){
        List<String> portList = serialCommunication.getPortNames();
        for(String port: portList) {
            if(port.contains("COM3")) {
                serialCommunication.connect(port);
                try {
					serialCommunication.getSerialPort().addEventListener(new PortReader());
				} catch (SerialPortException e) { 
					e.printStackTrace();
				}
            }
        }
    }

    public boolean sendJRKcommand(int command, int parameter){
        if(serialCommunication == null)
            return false;
        if(!serialCommunication.isConnected())
            return false;
        last_cmd_time = System.currentTimeMillis() - last_cmd_time;
        /*
        if(last_cmd_time <= 500) {
        	System.out.println("[sendJRKcommand]last_cmd_time=" + last_cmd_time);
            return false;
        }
        */
        last_cmd_time = System.currentTimeMillis();
        System.out.println("[sendJRKcommand]parameter=" + parameter);        
        
        if(command == JRKCommands.JRK_SET_TARGET.com){
            char[] ba = new char[2];
            ba[0] = (char)(0xC0 + (parameter & 0x1F));
            ba[1] = (char)((parameter >> 5) & 0x7F);
            serialCommunication.write(ba);
            return true;
        } else {
        	char[] ba = new char[1];
        	ba[0] = (char)(command & 0xFF);
        	serialCommunication.write(ba);
            return true;
        }               
    }

    private ByteBuffer buffer = ByteBuffer.allocate(2);
    private class PortReader implements SerialPortEventListener 
	{
		@Override
		public void serialEvent(SerialPortEvent event) {
			if(event.isRXCHAR() && event.getEventValue() > 0)
			{								
				try {
					buffer.put(serialCommunication.readBytes());
					if(buffer.position()>=2)
					{
						int value = (((int)buffer.get(1)&0xFF)<<8) | (int)buffer.get(0)&0xFF;
						System.out.println("[PortReader]value=" + value);
						actualPosField.setText(value + "");
						buffer.clear();					
					}
				} 
				catch(BufferOverflowException e)
				{
					e.printStackTrace();
				}										
            }
		}
	}
    
}