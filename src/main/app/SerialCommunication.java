package main.app;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

import java.util.Arrays;
import java.util.List;

public class SerialCommunication {
    private SerialPort port;
    private byte[] buffer;

    public SerialCommunication() {
        port = null;
        buffer = new byte[100000];
    }

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

    public byte[] readData (int length, int timeout) {
        try {
            buffer = port.readBytes(length, timeout);
        } catch (SerialPortException ex) {
            System.err.println(ex);
        } catch (SerialPortTimeoutException tex) {
            System.err.println(tex);
        }
        return buffer;
    }
}
