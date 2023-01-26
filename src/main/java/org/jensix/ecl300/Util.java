package org.jensix.ecl300;

public class Util {

	public static String bufferAsReadableString(byte[] buf) {
		return String.format("[%02x %02x %02x %02x %02x]", buf[0], buf[1], buf[2], buf[3], buf[4]);
	}

}
