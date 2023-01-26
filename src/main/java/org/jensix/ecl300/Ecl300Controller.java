package org.jensix.ecl300;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class Ecl300Controller {
	private static Ecl300Controller INSTANCE;

	public static Ecl300Controller getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new Ecl300Controller();
		}
		return INSTANCE;
	}

	private static int DELAY_TIME = 400;

    private SerialPort serialPort;
    private OutputStream outStream;
    private InputStream inStream;
    private long timeReceivedLast = 0;


    // Command
    // Read controller mode = 11
    // Read pumps = 20
    // Write pumps = 21
    // Read valve = 22
    // Write valve = 23
    // Read EEPROM (16bit) = 80
    // Write EEPROM (16 bit) = 90
    // Write EEPROM lowbyte = A0
    // Write EEPROM highbyte = B0
    // Read RAM = Cx
    // Write RAM (16 bit) = DX
    // Write RAM lowbyte = EX
    // Write RAM highbyte = FX

	public void sendReadCommand(Cmd command) throws IOException {
		byte[] response;
		String responseMessage;
		Ecl300Command eclCmd;

		System.out.println("Sending request "  + command.toString());
		if (command == Cmd.READ_TIME) {
			eclCmd = Ecl300Command.getCommand(Cmd.READ_TIME_MONTH_YEAR);
			response = sendCommand(eclCmd);
			byte weekDayMonth = response[2];
			byte year = response[3];
			eclCmd = Ecl300Command.getCommand(Cmd.READ_TIME_DAY_HOUR);
			response = sendCommand(eclCmd);
			byte day = response[2];
			byte hour = response[3];
			eclCmd = Ecl300Command.getCommand(Cmd.READ_TIME_MIN_SEC);
			response = sendCommand(eclCmd);
			byte minutes = response[2];
			byte seconds = response[3];
			responseMessage = getReponseMessageDateTime(weekDayMonth, year, day, hour, minutes, seconds);
		} else {
			eclCmd = Ecl300Command.getCommand(command);
			if (eclCmd == null) {
				throw new IOException("Unknown command: " + command);
			}
			response = sendCommand(eclCmd);
			responseMessage = getResponseMessageLong(eclCmd, response);
		}
		System.out.println("Received response: " + responseMessage);
	}

	public void sendWriteCommand(Cmd command, String paramValue) {
		System.out.println("Not yet implemented.");
	}

	public String getReadValueLong(Cmd command) throws IOException {
		Ecl300Command eclCmd = Ecl300Command.getCommand(command);
		if (eclCmd == null) {
			throw new IOException("Unknown command: " + command);
		}
		byte[] response = sendCommand(eclCmd);
		return getResponseMessageLong(eclCmd, response);
	}

	public String getReadValue(Cmd command) throws IOException {
		Ecl300Command eclCmd = Ecl300Command.getCommand(command);
		if (eclCmd == null) {
			throw new IOException("Unknown command: " + command);
		}
		byte[] response = sendCommand(eclCmd);
		return getResponseMessage(eclCmd, response);

	}

	private byte[] sendCommand(Ecl300Command command) throws IOException {
		byte[] request = new byte[5];
		request = command.getSerialCmd();
//		return sendCommandMock(request);
		return sendCommand(request);
	}

	public byte[] sendCommand(byte[] commandBytes) throws IOException {
		byte[] response = new byte[5];

		calcChecksum(commandBytes);
		long now = System.currentTimeMillis();
		timeReceivedLast = now;
		if (now - timeReceivedLast < DELAY_TIME) {
			long sleepTime = DELAY_TIME - (now - timeReceivedLast);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				throw new IOException("Interrupted in sleep", e);
			}
		}
		outStream.write(commandBytes);
		int bytesRead = 0;
		while (bytesRead < response.length) {
			int bytesReceived = inStream.read(response, bytesRead, response.length-bytesRead);
			bytesRead += bytesReceived;
		}
		timeReceivedLast = System.currentTimeMillis();
		System.out.println("Response in total: " + Util.bufferAsReadableString(response));
		if (bytesRead < response.length) {
			throw new IOException("Response received is too short (" + bytesRead + "bytes)");
		}
		if (!checkChecksum(response)) {
			throw new IOException("Checksum error in response from controller");
		}
		checkResponse(response);
		return response;
	}

	public byte[] sendCommandMock(byte[] commandBytes) throws IOException {
		byte[] result = new byte[5];
		calcChecksum(commandBytes);
		result[0] = 3;
		result[1] = 7;
		result[2] = 11;
		result[3] = 15;
		calcChecksum(result);
		return result;
	}

	private String getResponseMessage(Ecl300Command command, byte[] buf) {
		StringBuffer message = new StringBuffer();
		String result = command.getUnit().convert(buf, message);
		return result;
	}

	private String getResponseMessageLong(Ecl300Command command, byte[] buf) {
		StringBuffer message = new StringBuffer();
		message.append(command.getLabel()).append(": ");
		command.getUnit().convert(buf, message);
		return message.toString();
	}

	private String getReponseMessageDateTime(byte weekDayMonth, byte year,
			byte day, byte hour, byte minutes, byte seconds) {
		String sWeekDay;
		int weekDay = (int)((weekDayMonth & 0xF0) >> 4);
		int iMonth = (int)(weekDayMonth & 0x0F);
		int iYear = (int)(year & 0xFF) + 1900;
		int iDay = (int)(day & 0xFF);
		int iHour = (int)(hour & 0xFF);
		int iMinutes = (int)(minutes & 0xFF);
		switch (weekDay) {
			case 1:
				sWeekDay = "Mon";
				break;
			case 2:
				sWeekDay = "Tue";
				break;
			case 3:
				sWeekDay = "Wed";
				break;
			case 4:
				sWeekDay = "Thu";
				break;
			case 5:
				sWeekDay = "Fri";
				break;
			case 6:
				sWeekDay = "Sat";
				break;
			case 7:
				sWeekDay = "Sun";
				break;
			default:
				sWeekDay = "???";
				break;
		}
		return String.format("%s %04d-%02d-%02d %02d:%02d", sWeekDay, iYear, iMonth, iDay, iHour, iMinutes);
	}

	private void calcChecksum(byte[] buf) {
		int xor = buf[0] ^ buf[1] ^ buf[2] ^ buf[3];
		buf[4] = (byte) xor;
	}

	private void checkResponse(byte[] buf) throws IOException {
		int b1 = buf[0];
		switch (b1) {
		case 0x00:
			throw new IOException("Unknown value");
		case 0x02:
		    break;
		case 0x10:
			throw new IOException("Invalid value for date or time.");
		case 0x20:
			throw new IOException("Non existing circuit.");
		case 0x30:
			throw new IOException("Non existing mode.");
		case 0x40:
			throw new IOException("Non existing port.");
		case 0xF0:
			throw new IOException("Communication error.");
		}
	}

	private boolean checkChecksum(byte[] buf) {
		int xor = buf[0] ^ buf[1] ^ buf[2] ^ buf[3];
		int sum = buf[4];
		return xor == sum;
	}

	private void writeBufferShortValue(byte[] buf, int offset, short value) {
		buf[offset] = (byte) (value >> 8);
		buf[offset+1] =	(byte) (value & 0xFF);
	}

	private void writeBufferByteValue(byte[] buf, int offset, byte value) {
		buf[offset] = value;
	}

	private void writeBufferFloatValue(byte[] buf, int offset, float temp) {
		short tempVal = (short)Math.round(temp * 128.0f);
		writeBufferShortValue(buf, offset, tempVal);
	}

	public CommPortIdentifier findPort(String devicePath) {
	    System.out.println("Looking for serial port: " + devicePath + ".");
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		while (ports.hasMoreElements()) {
		    CommPortIdentifier port = ports.nextElement();
		    System.out.println("Found port: " + port.getName());
		    if (port.getName().equals(devicePath)) {
			    System.out.println("Found correct port.");
			return port;
		    }
		}
	    System.out.println("Could not find correct port " + devicePath + ".");
	    return null;
	}

	public void connect(CommPortIdentifier port) throws IOException {
		String errorMessage = "Error in connecting to port";
        try {
            // Obtain a CommPortIdentifier object for the port you want to open
            // Get the port's ownership
            serialPort = (SerialPort) port.open("Danfoss ECL 300 application", 5000);

            // Set the parameters of the connection.
            setSerialPortParameters();

            // Open the input and output streams for the connection.
            // If they won't open, close the port before throwing an
            // exception.
            outStream = serialPort.getOutputStream();
            inStream = serialPort.getInputStream();
            if (null == outStream || null == inStream) {
		throw new IOException("Failed to get stream from SerialPort.");
            }
        } catch (PortInUseException e) {
            throw new IOException(errorMessage, e);
        } catch (IOException e) {
            serialPort.close();
            throw new IOException(errorMessage, e);
        }
	}

	public void disconnect() throws IOException {
        // close the i/o streams.
        if (serialPort != null) {
            try {
                outStream.close();
            } catch (IOException ex) {
                System.out.println("Error when closing output stream." + ex);
            }
            try {
                inStream.close();
            } catch (IOException ex) {
                System.out.println("Error when closing input stream." + ex);
            }
            // Close the port.
            serialPort.close();
            serialPort = null;
        }
    }


	private void setSerialPortParameters() throws IOException {
        final int baudRate = 1200;

        try {
            serialPort.setSerialPortParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_ODD);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            throw new IOException("Unsupported serial port parameter", ex);
        }
	}

}
