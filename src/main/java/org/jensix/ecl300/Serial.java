package org.jensix.ecl300;

import gnu.io.CommPortIdentifier;

import java.io.IOException;


public class Serial {
	private static String DEFAULT_DEVICE_PATH = "/dev/cu.NoZAP-PL2303-000012FD";
	private static final Cmd[] DEFAULT_LOG_SET = {
		Cmd.READ_SENSOR_1,
		Cmd.READ_SENSOR_2,
		Cmd.READ_SENSOR_3,
		Cmd.READ_SENSOR_4,
		Cmd.READ_SENSOR_5,
		Cmd.READ_SENSOR_6,
		Cmd.READ_CALC_FLOW_TEMP_1,
		Cmd.READ_CALC_FLOW_TEMP_2,
		Cmd.READ_CALC_RETURN_TEMP_1,
		Cmd.READ_CALC_RETURN_TEMP_2,
		Cmd.READ_PUMP,
		Cmd.READ_VALVE
	};

    public static void main(String[] args) {
		System.out.println("Serial started.");
		Ecl300Controller serial = Ecl300Controller.getInstance();
		String devicePath;
		devicePath = System.getProperty("SerialPort");
		if (null == devicePath || devicePath.isEmpty()) {
			devicePath = DEFAULT_DEVICE_PATH;
		}

		if (args[0].equals("usage")) {
			usage();
			System.exit(1);
		}

		CommPortIdentifier port = null;
		boolean threadActive = false;

		try {
			port = serial.findPort(devicePath);
		} catch (Exception ex) {
		    System.out.println("No port found, exiting...");
			ex.printStackTrace();
			System.exit(1);
		}

		if (null == port) {
		    System.out.println("Exiting...");
			System.exit(1);
		}

		try {
			serial.connect(port);
			// Read all sensor values:
			if (args.length == 1) {
				if (args[0].equals("all")) {
					for (Cmd command : Cmd.values()) {
						serial.sendReadCommand(command);
						System.out.println();
					}
				} else if (args[0].equals("getPorts")) {
		            SerialHelper serialHelper = new SerialHelper();
		            SerialHelper.SerialHelperTester.checkListSerialPorts(serialHelper);
				} else {
					boolean commandFound = false;
					for (Cmd command : Cmd.values()) {
						if (args[0].equals(command.toString())) {
							serial.sendReadCommand(command);
							commandFound = true;
							break;
						}
					}
					if (!commandFound) {
						usage();
					}
				}
			} else if (args.length == 2) {
				if (args[0].equals("log")) {
					ValueLogger logger = new ValueLogger(args[1], DEFAULT_LOG_SET);
					threadActive = true;
					logger.logValues();
				} else {
					usage();
				}

			} else if (args.length == 3 && args[0].equals("write")) {
				for (Cmd command : Cmd.values()) {
					if (args[1].equals(command.toString())) {
						serial.sendWriteCommand(command, args[2]);
						break;
					}
				}
				usage();
			} else if (args.length == 3 && args[0].equals("dump")) {
				MemoryDump dumper = new MemoryDump(args[1], Integer.valueOf(args[2], 16));
				dumper.dump();
			} else if (args.length == 5 && args[0].equals("sendCommand")) {
				sendCommand(serial, args[1], args[2], args[3], args[4]);
			} else {
				usage();
			}
		} catch (IOException e) {
			threadActive = false;
			System.out.println("Error in serial communication: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (!threadActive) {
					serial.disconnect();
				}
			} catch (IOException e) {
				System.out.println("Error disconnect: " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("Serial done.");
	}

    private static void sendCommand(Ecl300Controller serial, String byte0, String byte1,
			String byte2, String byte3) {
		byte[] buf = { Integer.valueOf(byte0, 16).byteValue(),
				Integer.valueOf(byte1, 16).byteValue(),
				Integer.valueOf(byte2, 16).byteValue(),
				Integer.valueOf(byte3, 16).byteValue(),
				0};
		System.out.println("Sending command: ");
		try {
			byte[] response = serial.sendCommand(buf);
			System.out.println("Send command: " + Util.bufferAsReadableString(buf));
			System.out.println("Received response: " + Util.bufferAsReadableString(response));
		} catch (IOException e) {
			System.err.println("Error when sending command: " + e);
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.out.println("Usage: Serial <command> | all | getPorts | log <path>> | dump <path> <address> | sendCommand b1 b2 b3 b4");
		System.out.println("command one of: ");
		for (Cmd command : Cmd.values()) {
			System.out.println(command.toString());
		}
    }

}
