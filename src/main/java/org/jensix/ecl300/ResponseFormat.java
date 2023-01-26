package org.jensix.ecl300;

public enum ResponseFormat {
	BIT0,
	BIT1,
	BIT2,
	BIT3,
	BIT4,
	BIT5,
	BIT6,
	BIT7,
	NIBBLE,
	BYTE,
	SIGNED_BYTE,
	INT,
	FLOAT1B,
	FLOAT2B,
	TEMP,
	TEMP_INT,
	CONTROLLER_MODE,
	DATE_TIME_MONTH_YEAR,
	DATE_TIME_INT_INT,
	TRIAC_VALVE,
	RELAIS_PUMP,
	APPLICATION_STRING;

	private static final float NO_SENSOR = 192.0F;
	private static final float SENSOR_SHORTCIRCUIT = -64.0F;

	public String convert(byte[] responseBuffer, StringBuffer message) {

		switch (this) {
		case BIT0:
			getResponseAsBitValue(responseBuffer, 0, message);
			break;
		case BIT1:
			getResponseAsBitValue(responseBuffer, 1, message);
			break;
		case BIT2:
			getResponseAsBitValue(responseBuffer, 2, message);
			break;
		case BIT3:
			getResponseAsBitValue(responseBuffer, 3, message);
			break;
		case BIT4:
			getResponseAsBitValue(responseBuffer, 4, message);
			break;
		case BIT5:
			getResponseAsBitValue(responseBuffer, 5, message);
			break;
		case BIT6:
			getResponseAsBitValue(responseBuffer, 6, message);
			break;
		case BIT7:
			getResponseAsBitValue(responseBuffer, 7, message);
			break;
		case NIBBLE:
			getResponseAsNibbleValue(responseBuffer, message);
			break;
		case BYTE:
			getResponseAsByteValue(responseBuffer, message);
			break;
		case SIGNED_BYTE:
			getResponseAsSignedByteValue(responseBuffer, message);
			break;
		case CONTROLLER_MODE:
			getResponseAsStringControllerMode(responseBuffer, message);
			break;
		case FLOAT1B:
			getResponseAsStringFloatValue1Byte(responseBuffer, message);
			break;
		case FLOAT2B:
			getResponseAsStringFloatValue2Bytes(responseBuffer, message);
			break;
		case INT:
			getResponseAsShortValue(responseBuffer, message);
			break;
		case RELAIS_PUMP:
			getResponseAsStringPumpStatusValue(responseBuffer, message);
			break;
		case TEMP:
			getResponseAsStringTemperatureValue(responseBuffer, message);
			break;
		case TEMP_INT:
			getResponseAsStringParameterTemperatureValue(responseBuffer, message);
			break;
		case TRIAC_VALVE:
			getResponseAsStringTriacStatusValue(responseBuffer, message);
			break;
		case APPLICATION_STRING:
			getResponseAsStringReadApplication(responseBuffer, message);
			break;
		 case DATE_TIME_INT_INT:
		 case DATE_TIME_MONTH_YEAR:
			message.append("Command not supported: " + this.toString() + " use READ_TIME");
			break;
		default:
			message.append("Unknown response format: " + this.toString());
			break;
		}
		return message.toString();
	}

	private void getResponseAsShortValue(byte[] buf, StringBuffer message) {
		short value = getShortValue(buf);
		message.append(": ").append(value);
	}

	private void getResponseAsByteValue(byte[] buf, StringBuffer message) {
		int value = getByteValue(buf);
		message.append(": ").append(value);
	}

	private void getResponseAsNibbleValue(byte[] buf, StringBuffer message) {
		int value = getNibbleValue(buf);
		message.append(": ").append(value);
	}

	private void getResponseAsSignedByteValue(byte[] buf, StringBuffer message) {
		int value = getSignedByteValue(buf);
		message.append(": ").append(value);
	}

	private void getResponseAsBooleanValue(byte[] buf, StringBuffer message) {
		int value = getByteValue(buf);
		message.append(value == 0 ? "OFF" : "ON");
	}

	private void getResponseAsBitValue(byte[] buf, int digit, StringBuffer message) {
		boolean isSet = isNthBitSetBoolean(buf, digit);
		message.append(isSet ? "ON" : "OFF");
	}

	private void getResponseAsStringTemperatureValue(byte[] buf,
			StringBuffer message) {
		float tempVal = getTemperatureValue(buf);
		if (tempVal == NO_SENSOR) {
			message.append("No sensor present (n/a)!");
		} else if (tempVal == SENSOR_SHORTCIRCUIT) {
			message.append("Short circuit in sensor!");
		} else {
			String text = String.format("%.1f", tempVal);
			message.append(text);
		}
	}

	private void getResponseAsStringParameterTemperatureValue(byte[] buf,
			StringBuffer message) {
		int tempVal = getIntTemperatureValue(buf);
		String text = String.format("%d", tempVal);
		message.append(text);
	}

	private void getResponseAsStringFloatValue1Byte(byte[] buf,
			StringBuffer message) {
		int tempVal = getByteValue(buf);
		String text = String.format("%d.%d", tempVal/10, tempVal%10);
		message.append(text);
	}

	private void getResponseAsStringFloatValue2Bytes(byte[] buf,
			StringBuffer message) {
		int tempVal = getShortValue(buf);
		String text = String.format("%d.%d", tempVal/10, tempVal%10);
		message.append(text);
	}

	private void getResponseAsStringControllerMode(byte[] buf, StringBuffer message) {
		int mode = buf[3];
		String modeStr;
		switch (mode) {
		case 0:
			modeStr = "manual mode";
			break;
		case 1:
			modeStr = "clock mode";
			break;
		case 2:
			modeStr = "comfort mode (day)";
			break;
		case 3:
			modeStr = "reduced mode (night)";
			break;
		case 4:
			modeStr = "standby mode";
			break;
		default:
			modeStr = "unknown value";
		}
		message.append(modeStr);
	}

	private void getResponseAsStringPumpStatusValue(byte[] responseBuffer,
			StringBuffer message) {
		boolean p1On = isNthBitSet(responseBuffer[3], 0);
		boolean p2On = isNthBitSet(responseBuffer[3], 1);
		boolean p3On = isNthBitSet(responseBuffer[3], 2);
		String p1 = p1On ? "off" : "on";
		String p2 = p2On ? "off" : "on";
		String p3 = p3On ? "off" : "on";
		message.append(p1).append(", ").append(p2).append(", ").append(p3);
	}

	private void getResponseAsStringTriacStatusValue(byte[] responseBuffer,
			StringBuffer message) {
		boolean v1Close = isNthBitSet(responseBuffer[3], 0);
		boolean v1Open = isNthBitSet(responseBuffer[3], 1);
		boolean v2Close = isNthBitSet(responseBuffer[3], 2);
		boolean v2Open = isNthBitSet(responseBuffer[3], 3);
		String t1 = v1Open ? "opening" : v1Close ? "closing" : "stopped";
		String t2 = v2Open ? "opening" : v2Close ? "closing" : "stopped";
		message.append(t1).append(", ").append(t2);
	}

	private void getResponseAsStringReadApplication(byte[] buf,
			StringBuffer message) {
		if (buf[0] != 0x02 && buf[1] != 0x80) {
			message.append("Unknown response format " + Util.bufferAsReadableString(buf));
		} else {
			message.append("Application ");
			byte b1 = buf[2];
			byte b2 = buf[3];
			switch (b1) {
			case 0:
				message.append("A");
				break;
			case 1:
				message.append("b");
				break;
			case 2:
				message.append("C");
				break;
			case 3:
				message.append("d");
				break;
			case 4:
				message.append("E");
				break;
			case 5:
				message.append("F");
				break;
			case 6:
				message.append("G");
				break;
			case 7:
				message.append("H");
				break;
			case 8:
				message.append("L");
				break;
			case 9:
				message.append("n");
				break;
			case 10:
				message.append("o");
				break;
			case 11:
				message.append("P");
				break;
			case 12:
				message.append("U");
				break;
			}
			message.append(b2);
		}
	}

	private int getByteValue(byte[] buf) {
		return(int)(buf[2] & 0xFF);
	}

	private int getSignedByteValue(byte[] buf) {
		return(int)buf[2];
	}

	private int getNibbleValue(byte[] buf) {
		return(int)(buf[2] & 0x0F) ;
	}

	private final short getShortValue(byte[] buf) {
		short tempVal = (short)(((buf[2] & 0xFF) << 8) | (buf[3] & 0xFF));
		return tempVal;
	}

	private float getTemperatureValue(byte[] buf) {
		short tempVal = getShortValue(buf);
		float result = tempVal / 128.0f;
		return result;
	}

	private int getIntTemperatureValue(byte[] buf) {
		int tempVal = getSignedByteValue(buf);
		return (int) tempVal ;
	}

	private boolean isNthBitSetBoolean(byte[] buf, int n) {
		int b = (int)(buf[2] & 0xFF);
		int mask = (1 << n);
		int setValue = b & mask;
		return setValue != 0;
	}

	private boolean isNthBitSet(byte buf, int n) {
		int b = (int)(buf & 0xFF);
		int mask = (1 << n);
		int setValue = b & mask;
		return setValue != 0;
	}
}
