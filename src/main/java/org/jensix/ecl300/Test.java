package org.jensix.ecl300;

public class Test {

	private static void writeBufferShortValue(byte[] buf, int offset, short value) {
		buf[offset] = (byte) (value >> 8);
		buf[offset+1] =	(byte) (value & 0xFF);
	}

	private static String bufferAsReadableString(byte[] buf) {
		return String.format("[%02x %02x %02x %02x]", buf[0], buf[1], buf[2], buf[3]);
	}

	private static void printBuffer(short val, byte[] buf) {
		System.out.println("short value: " + val + ", buffer: " + bufferAsReadableString(buf));
	}

	public static void main(String[] args) {
		byte[] buf = new byte[4];
		short tempVal1 = 100;
		short tempVal2 = 128;
		short tempVal3 = -1;
		short tempVal4 = -32768;
		short tempVal5 = 32767;
		writeBufferShortValue(buf, 1, tempVal1);
		printBuffer(tempVal1, buf);
		writeBufferShortValue(buf, 1, tempVal2);
		printBuffer(tempVal2, buf);
		writeBufferShortValue(buf, 1, tempVal3);
		printBuffer(tempVal3, buf);
		writeBufferShortValue(buf, 1, tempVal4);
		printBuffer(tempVal4, buf);
		writeBufferShortValue(buf, 1, tempVal5);
		printBuffer(tempVal4, buf);
	}


}
