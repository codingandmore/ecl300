package org.jensix.ecl300;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.util.TooManyListenersException;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;

public class SerialHelper {

    private SerialPort serialPort;
    private OutputStream outStream;
    private InputStream inStream;

    /**
     * \brief List the available serial ports
     *
     * \return Array of string for the available serial port names
     */
    public static String[] listSerialPorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList portList = new ArrayList();
        String portArray[] = null;
        while (ports.hasMoreElements()) {
            CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
            if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(port.getName());
            }
        }
        portArray = (String[]) portList.toArray(new String[0]);
        return portArray;
    }

    /**
     * \brief Connect to the selected serial port with 57600bps-8N1 mode
     */
    public void connect(String portName) throws IOException {
        try {
            // Obtain a CommPortIdentifier object for the port you want to open
            CommPortIdentifier portId =
                    CommPortIdentifier.getPortIdentifier(portName);

            // Get the port's ownership
            serialPort = (SerialPort) portId.open("Demo application", 5000);

            // Set the parameters of the connection.
            setSerialPortParameters();

            // Open the input and output streams for the connection.
            // If they won't open, close the port before throwing an
            // exception.
            outStream = serialPort.getOutputStream();
            inStream = serialPort.getInputStream();
        } catch (NoSuchPortException e) {
            throw new IOException(e.getMessage());
        } catch (PortInUseException e) {
            throw new IOException(e.getMessage());
        } catch (IOException e) {
            serialPort.close();
            throw e;
        }
    }

    /**
     * \brief Get the serial port input stream
     * \return The serial port input stream
     */
    public InputStream getSerialInputStream() {
        return inStream;
    }

    /**
     * \brief Get the serial port output stream
     * \return The serial port output stream
     */
    public OutputStream getSerialOutputStream() {
        return outStream;
    }

    /**
     * \brief Sets the serial port parameters to 57600bps-8N1
     */
    protected void setSerialPortParameters() throws IOException {

        final int baudRate = 57600; // 57600bps

        try {
            // Set serial port to 57600bps-8N1..my favourite
            serialPort.setSerialPortParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            throw new IOException("Unsupported serial port parameter");
        }
    }

    /**
     * \brief Register listener for data available event
     *
     * @param dataAvailableListener The data available listener
     */
    public void addDataAvailableListener(SerialPortEventListener dataAvailableListener)
            throws TooManyListenersException {
        // Add the serial port event listener
        serialPort.addEventListener(dataAvailableListener);
        serialPort.notifyOnDataAvailable(true);
    }

    /**
     * \brief Disconnect the serial port
     */
    public void disconnect() {
        if (serialPort != null) {
            try {
                // close the i/o streams.
                outStream.close();
                inStream.close();
            } catch (IOException ex) {
                // don't care
            }
            // Close the port.
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * \brief Private inner class to test and debug the SerialHelper class
     */
    public static class SerialHelperTester implements SerialPortEventListener {

        public static void main(String[] args) {
            if (args.length < 1) {
                System.out.println(
                        "Usage: java SerialHelperTester <dataToBeSentToSerialPort>");
                System.exit(1);
            }

            SerialHelper serialHelper = new SerialHelper();

            checkListSerialPorts(serialHelper);
            checkConnect(serialHelper);
            checkAddDataAvailableListener(serialHelper, args[0]);
            checkDisconnect(serialHelper);
        }

        public static void checkListSerialPorts(SerialHelper serialHelper) {
            System.out.println("Check the listSerialPorts");
            System.out.println("-------------------------");
            String[] serialPorts = SerialHelper.listSerialPorts();
            if (serialPorts != null) {
                for (int i = 0; i < serialPorts.length; i++) {
                    System.out.println("Port name: " + serialPorts[i]);
                }
            }
            System.out.println();
        }

        private static void checkConnect(SerialHelper serialHelper) {
            // Replace it with the tested serial port
            final String serialPort = "/dev/ttyACM0";

            System.out.println("Connect to serial port");
            System.out.println("-------------------------");
            try {
                serialHelper.connect(serialPort);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            System.out.println();
        }

        private static void checkAddDataAvailableListener(
                SerialHelper serialHelper, String data) {
            System.out.println("Check data available listener");
            System.out.println("-----------------------------");

            SerialHelperTester tester =
                    new SerialHelperTester(serialHelper.getSerialInputStream(),
                    serialHelper.getSerialOutputStream());

            try {
                serialHelper.addDataAvailableListener(tester);
            } catch (TooManyListenersException ex) {
                System.err.println(ex.getMessage());
            }

            OutputStream outStream = serialHelper.getSerialOutputStream();
            data = data + "\r";
            try {
                outStream.write(data.getBytes());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            try {
                // Sleep for 10-secs
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
        }

        private static void checkDisconnect(SerialHelper serialHelper) {
            System.out.println("Disconnect from serial port");
            System.out.println("---------------------------");
            serialHelper.disconnect();
            System.out.println("done");
        }
        /**
         * Buffer to hold the reading
         */
        private byte[] readBuffer = new byte[400];
        /**
         * I/O stream for serial port
         */
        private InputStream inStream;
        private OutputStream outStream;

        public SerialHelperTester(InputStream inStream, OutputStream outStream) {
            this.inStream = inStream;
            this.outStream = outStream;
        }

        private void readSerial() {
            try {
                int availableBytes = inStream.available();
                if (availableBytes > 0) {
                    // Read the serial port
                    inStream.read(readBuffer, 0, availableBytes);

                    // Print it out
                    System.out.println("Recv :" +
                            new String(readBuffer, 0, availableBytes));
                }
            } catch (IOException e) {
            }
        }

        public void serialEvent(SerialPortEvent events) {
            switch (events.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE:
                    readSerial();
            }
        }
    }
}