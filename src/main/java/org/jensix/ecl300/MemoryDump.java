package org.jensix.ecl300;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoryDump {

		private FileOutputStream logFile;
		PrintWriter writer;
		private String path;
		int address; // page in memory of 256 bytes to log
		private static Ecl300Controller controller;

		public MemoryDump(String path, int address) {
			super();
			controller = Ecl300Controller.getInstance();
			this.path = path;
			this.address = address;
		}

		public void dump() {
			openFile();
			startDumping();
			for (int i=0; i<256; i+=16) {
				String adrStr = String.format("%04x ", address+i);
				writer.print(adrStr);
				System.out.print(adrStr);
				for (int j=0; j<16; j+=2) {
					int adr = address+i+j;
					getValueAndLog(adr);
				}
				writer.println();
				writer.flush();
				System.out.println();
			}
		}

		public void stopDumping() {
			closeFile();
			try {
				controller.disconnect();
			} catch (IOException e) {
				System.err.println("Error while disconnecting: " + e.getMessage());
				e.printStackTrace();
			}
		}

		private void getValueAndLog(int adr) {
			byte[] data;// = {(byte) 0xC0, 0x11, (byte) 0xAA, 0x00, 0x00};
			byte b1 = (byte) (0xC0 | (adr >> 8));
			byte b2 = (byte) (adr % 256);
			byte[] commandBytes = {b1, b2, 0, 0, 0};
			try {
				System.out.println("Sending request: " + Util.bufferAsReadableString(commandBytes));
				data = controller.sendCommand(commandBytes);
				writeData(data);
			} catch (IOException e) {
				System.err.println("Error sending command: " + e);
				e.printStackTrace();
			}
		}

		private void startDumping() {
			writeHeader();
		}

		private void writeData(byte[] data) {
			String s = String.format("%02x %02x ", (data[2] & 0xFF), (data[3] & 0xFF));
			writer.print(s);
			System.out.print(s);
		}

		private void openFile() {
			String adr = String.format("%04x", address);

			String filePath = path + "-" + adr + ".log";
			try {
				logFile = new FileOutputStream(filePath);
				writer = new PrintWriter(logFile);
			} catch (FileNotFoundException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}

		private void closeFile() {
			try {
				if (null != writer) {
					writer.close();
				}
				if (null != logFile) {
					logFile.close();
				}
			} catch (IOException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}

		private void writeHeader() {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
			String formattedDate = formatter.format(new Date());

			writer.println("Time: " + formattedDate);
			String adr = String.format("Address: %04x", address);
			writer.println("Time: " + adr);
			System.out.println(adr);
			writer.println("     00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F");
			writer.println("====================================================");
			System.out.println("     00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F");
			System.out.println("====================================================");

		}
	}
