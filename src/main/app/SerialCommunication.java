package main.app;

import jssc.*;

import java.util.Arrays;
import java.util.List;

public class SerialCommunication {
    private SerialPort port;

    public List<String> getPortNames() { return Arrays.asList(SerialPortList.getPortNames()); }

    public boolean isConnected() { return port != null; }

    public void connect (String portName) {
        int baudRate = SerialPort.BAUDRATE_9600;
        int dataBits = SerialPort.DATABITS_8;
        int stopBits = SerialPort.STOPBITS_1;
        int parity = SerialPort.PARITY_NONE;

        try {
            port = new SerialPort(portName);
            port.openPort();
            port.setParams(baudRate, dataBits, stopBits, parity, true, true);
            port.addEventListener(new MySerialPortEventListener());
        } catch (SerialPortException ex) {
            port = null;
            System.err.println(ex);
        }
    }

    public void close() {
        if (port != null) {
            try {
                if (port.isOpened()) {
                    port.removeEventListener();
                    port.closePort();
                }
            } catch (SerialPortException ex) {
                System.err.println(ex);
            } finally {
                port = null;
            }
        }
    }

    public void write (byte[] bytes) {
        if (!isConnected()) {
            throw new IllegalStateException("Port is not opened!");
        }
        try {
            port.writeBytes(bytes);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        }
    }

    public void dispose() {
        close();
    }

    private class MySerialPortEventListener implements SerialPortEventListener {
        @Override
        public synchronized void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                try {
                    byte[] buf = port.readBytes(event.getEventValue());
                    if (buf.length > 0) {
                        // TODO
                        // do whatever you want
                        // would be needed in detektor (returns photo) and migawka (returns OK or ERROR)
                    }
                } catch (SerialPortException ex) {
                    System.err.println(ex);
                }
            }
        }
    }
}
