package org.jensix.ecl300;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ecl300Command {
	@SuppressWarnings("serial")
	private static final List<Ecl300Command> getCommandList() { return new ArrayList<Ecl300Command>() {{
		// Version and info commands
		add(new Ecl300Command(Cmd.READ_APPLICATION, "Application", new byte[] {(byte) 0x80, 0x09, 0x00, 0x00}, ResponseFormat.APPLICATION_STRING));
		add(new Ecl300Command(Cmd.READ_MODE_1, "Controller mode circuit 1", new byte[] {(byte) 0x11, 0x01, 0x00, 0x00}, ResponseFormat.CONTROLLER_MODE));
		add(new Ecl300Command(Cmd.READ_MODE_2, "Controller mode circuit 2", new byte[] {(byte) 0x11, 0x02, 0x00, 0x00}, ResponseFormat.CONTROLLER_MODE));
		add(new Ecl300Command(Cmd.READ_MODE_3, "Controller mode circuit 3", new byte[] {(byte) 0x11, 0x03, 0x00, 0x00}, ResponseFormat.CONTROLLER_MODE));

		// PID calculations and sensor values
		add(new Ecl300Command(Cmd.READ_SENSOR_1, "Outdoor temp. (S1)", new byte[] {(byte) 0xCE, 0x3A, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_SENSOR_2, "Flow temp. circuit 1 (S2)", new byte[] {(byte) 0xCE, 0x38, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_SENSOR_3, "Return temp. circuit 1 (S3)", new byte[] {(byte) 0xCE, 0x36, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_SENSOR_4, "Flow temp. circuit 2 (S4)", new byte[] {(byte) 0xCE, 0x34, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_SENSOR_5, "Return temp. circuit 2 (S5)", new byte[] {(byte) 0xCE, 0x32, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_SENSOR_6, "Hotwater temp. upper (S6)", new byte[] {(byte) 0xCE, 0x30, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_OUTDOOR_TEMP, "Outdoor temp.", new byte[] {(byte) 0xCE, 0x3C, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_CALC_FLOW_TEMP_1, "Calculated flow temp. circuit 1", new byte[] {(byte) 0xCE, 0x46, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_CALC_FLOW_TEMP_2, "Calculated flow temp. circuit 2", new byte[] {(byte) 0xCE, 0x48, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_CALC_RETURN_TEMP_1, "Calculated return temp. circuit 1", new byte[] {(byte) 0xCE, 0x4A, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_CALC_RETURN_TEMP_2, "Calculated return temp. circuit 2", new byte[] {(byte) 0xCE, 0x4C, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_ROOM_TEMP_1, "Room temp circuit 1", new byte[] {(byte) 0xCE, 0x3E, 0x00, 0x00}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.READ_ROOM_TEMP_2, "Room temp circuit 2", new byte[] {(byte) 0xCE, 0x40, 0x00, 0x00}, ResponseFormat.TEMP));

		// date and time setting
		add(new Ecl300Command(Cmd.READ_TIME_MONTH_YEAR, "Month Year", new byte[] {(byte) 0xCB, (byte) 0x80, 0x00, 0x00}, ResponseFormat.DATE_TIME_MONTH_YEAR));
		add(new Ecl300Command(Cmd.READ_TIME_DAY_HOUR, "Day Hour", new byte[] {(byte) 0xCB, (byte) 0x82, 0x00, 0x00}, ResponseFormat.DATE_TIME_INT_INT));
		add(new Ecl300Command(Cmd.READ_TIME_MIN_SEC, "Min Sec", new byte[] {(byte) 0xCB, (byte) 0x84, 0x00, 0x00}, ResponseFormat.DATE_TIME_INT_INT));

		// pump and valve status
		add(new Ecl300Command(Cmd.READ_PUMP, "Status pump (on off) P1, P2, P3", new byte[] {(byte) 0x20, 0x00, 0x00, 0x00}, ResponseFormat.RELAIS_PUMP));
		add(new Ecl300Command(Cmd.READ_VALVE, "Status valve (opening (arrow up) closing (arrow down) stopped) V1, V2", new byte[] {(byte) 0x22, 0x00, 0x00, 0x00}, ResponseFormat.TRIAC_VALVE));

		// parameter settings
		// from here copy from Excel sheet!!
		add(new Ecl300Command(Cmd.CHOICE_TIME_CONTROL_UNIT_1_10, "10  Choice of time control unit (0..5) (1)", new byte[] {(byte)0xce, (byte)0xe3, (byte)0x0, (byte)0x0}, ResponseFormat.NIBBLE));
		add(new Ecl300Command(Cmd.CANCEL_REDUCED_TEMP_1_11, "11  Cancellation of reduced temperature(OFF/-29..10) (1)", new byte[] {(byte)0xce, (byte)0xa6, (byte)0x0, (byte)0x0}, ResponseFormat.SIGNED_BYTE));
		add(new Ecl300Command(Cmd.REFERENCE_RAMPING_1_13, "13  Reference ramping min (0..99) (1)", new byte[] {(byte)0xce, (byte)0xaa, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.OPTIMIZE_CONST_1_14, "14  Optimising constant (OFF/10..99) (1)", new byte[] {(byte)0xce, (byte)0xc0, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.ADAPT_FUNC_ROOM_TEMP_1_15, "15  Adaptive function of room temperature (OFF/1..30) (1)", new byte[] {(byte)0xce, (byte)0xc8, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TEMP_REF_FEEDBACK_1_17, "17  Temperature reference feed back (OFF/1..20) (1)", new byte[] {(byte)0xce, (byte)0xc4, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.OPTIMIZE_ROOM_OUTDOOR_1_20, "20  Optimising based on room (ON)/outdoor temp  (OFF) (1)", new byte[] {(byte)0xce, (byte)0xee, (byte)0x0, (byte)0x0}, ResponseFormat.BIT0));
		add(new Ecl300Command(Cmd.TOTAL_STOP_1_21, "21  Total stop (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xec, (byte)0x0, (byte)0x0}, ResponseFormat.BIT0));
		add(new Ecl300Command(Cmd.PUMP_MOTION_1_22, "22  Pump motion (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT4));
		add(new Ecl300Command(Cmd.VALVE_MOTION_1_23, "23  Valve motion (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT6));
		add(new Ecl300Command(Cmd.GEAR_THERMO_MOTOR_1_24, "24  Gear motor(ON) / thermo motor (OFF) (1)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT0));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_CONST_1_30, "30  Return temperature limit - constant °C (10..110) (1)", new byte[] {(byte)0xce, (byte)0xb4, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_UPPER_X_1_31, "31  Return temperature limitation - upper X °C (-30..15) (1)", new byte[] {(byte)0xce, (byte)0xaf, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_UPPER_Y_1_32, "32  Return temperature limitation - upper Y °C (10..110) (1)", new byte[] {(byte)0xce, (byte)0xad, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_LOWER_X_1_33, "33  Return temperature limitation - lower X °C (-30..15) (1)", new byte[] {(byte)0xce, (byte)0xae, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_LOWER_Y_1_34, "34  Return temperature limitation - lower Y °C (10..110) (1)", new byte[] {(byte)0xce, (byte)0xac, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_INFL_MAX_1_35, "35  Return temperature influence - max. °C (-9,9 .. 9,9) (1)", new byte[] {(byte)0xce, (byte)0x7a, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT2B));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_INFL_MIN_1_36, "36  Return temperature influence - min °C (-9,9 .. 9,9) (1)", new byte[] {(byte)0xce, (byte)0x7c, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT2B));
		add(new Ecl300Command(Cmd.ADAPT_FUNC_RETURN_LIMITER_1_37, "37  Adaptive function of return limiter (OFF / 1 ... 50) (1)", new byte[] {(byte)0xce, (byte)0xca, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.CHARGE_PUMP_POST_RUN_PRIMARY_1_40, "40  Charging pump post run - primary min (0..9) (1)", new byte[] {(byte)0xce, (byte)0xe5, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.CHARGE_PUMP_POST_RUN_SECONDARY_1_41, "41  Charging pump post run - secondary min (0..9) (1)", new byte[] {(byte)0xce, (byte)0xe5, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.PRIORITY_OR_PARALLEL_OP_1_43, "43  Priority or parallel operation (OFF / 1 ... 99 K) (1)", new byte[] {(byte)0xce, (byte)0xd4, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.MAX_HOTWATER_LOAD_TIME_1_44, "44  Max Hotwater load time min (0=INFINITE, 1 ... 250) (1)", new byte[] {(byte)0xce, (byte)0xce, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.MAX_HOTWATER_LOCK_TIME_1_45, "45  Max hotwater lock time min (0..250) (1)", new byte[] {(byte)0xce, (byte)0xcf, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.CHARGE_PUMP_OR_DIVERTING_VALUE_1_51, "51  Charging pump (OFF) or diverting valve (ON) (1)", new byte[] {(byte)0xce, (byte)0xef, (byte)0x0, (byte)0x0}, ResponseFormat.BIT0));
		add(new Ecl300Command(Cmd.PI_REF_DURING_HWS_1_53, "53  PI-reference during hot water load (ON=PI/OFF=hot water temp)) (1)", new byte[] {(byte)0xce, (byte)0xef, (byte)0x0, (byte)0x0}, ResponseFormat.BIT2));
		add(new Ecl300Command(Cmd.CIRCULATION_DURING_HOTWATER_LOAD_1_55, "55  Circulation duing Hotwater load (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xec, (byte)0x0, (byte)0x0}, ResponseFormat.BIT4));
		add(new Ecl300Command(Cmd.ADAPT_FUNC_COMPENSATION_1_67, "67  Adaptive function of compensation °C (ON=inactive/1..50) (1)", new byte[] {(byte)0xce, (byte)0xbc, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.SETPOINT_MISC_1_78, "78  Setpoint, miscellaneous Legionellen °C (OFF/1..100) (1)", new byte[] {(byte)0xce, (byte)0xb6, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TIME_SETUP_MISC_1_80, "80  Time set-up, miscellaneous Legionellen min (1..250) (1)", new byte[] {(byte)0xce, (byte)0xd3, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.APP_SELECTION_3_MISC_1_83, "83  Application selection III, miscellaneous Legionellen daily=ON/weekly=OFF) (1)", new byte[] {(byte)0xce, (byte)0xee, (byte)0x0, (byte)0x0}, ResponseFormat.BIT5));
		add(new Ecl300Command(Cmd.ACTUAL_FLOW_ENERGY_1_110, "110  Actual flow / energy (1)", new byte[] {(byte)0xce, (byte)0x86, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.ACTUAL_SETPOINT_FLOW_ENERGY_1_111, "111  Actual setpoint flow/energy (0-2999) (1)", new byte[] {(byte)0xce, (byte)0x8a, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.TAU_FLOW_INTEGRATOR_1_112, "112  Tau flow integrator  (1-250) (1)", new byte[] {(byte)0xce, (byte)0xde, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TAU_FLOW_FILTER_INTEGRATOR_1_113, "113  Tau flow filter integrator (1-250) (1)", new byte[] {(byte)0xce, (byte)0xdf, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TYPE_FLOW_PULSE_UNIT_1_114, "114  type of flow  / pulse unit (1)", new byte[] {(byte)0xce, (byte)0x8c, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.FLOW_UNIT_1_115, "115  Flow unit (1)", new byte[] {(byte)0xce, (byte)0xe8, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.FLOW_LIMIT_UPPER_Y_1_116, "116  Flow limiter - upper value (y coordinate) (1)", new byte[] {(byte)0xce, (byte)0x9a, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.FLOW_LIMIT_LOWER_Y_1_117, "117  Flow limiter - lower value (y coordinate) (1)", new byte[] {(byte)0xce, (byte)0x9c, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.SENSOR_TYPE_1_140, "140  Sensor type S5 (0=Return Temp,1=Lower hotwater, 2=hotwater load)) (1)", new byte[] {(byte)0xce, (byte)0xe6, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.OVERRIDE_VIA_SENSOR_INPUT_1_141, "141  Override via sensor input (OFF/S1=1..S6=6) (1)", new byte[] {(byte)0xce, (byte)0xe2, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.MAX_TANK_TEMP_1_152, "152  Maximum tank temperature °(10..110) (1)", new byte[] {(byte)0xce, (byte)0xbd, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.PROTECT_1_174, "174  Protect valve min (OFF/10..59) (1)", new byte[] {(byte)0xce, (byte)0xd2, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.HEATING_CURVE_1_175, "175  Heating curve (0,2 .... 3,4) (1)", new byte[] {(byte)0xce, (byte)0xc9, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT1B));
		add(new Ecl300Command(Cmd.PARALLEL_DISPLACEMENT_1_176, "176  Parallel displacement (-9 ..9) (1)", new byte[] {(byte)0xce, (byte)0xa8, (byte)0x0, (byte)0x0}, ResponseFormat.SIGNED_BYTE));
		add(new Ecl300Command(Cmd.FLOW_TEMP_MIN_1_177, "177  Flow temperature - minimum (10..110) (1)", new byte[] {(byte)0xce, (byte)0xcb, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.FLOW_TEMP_MAX_1_178, "178  Flow temperature - maximum (10..110) (1)", new byte[] {(byte)0xce, (byte)0xcc, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.HEATING_CUT_OUT_1_179, "179  Heating cut-out (10..30) (1)", new byte[] {(byte)0xce, (byte)0xc2, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_DAY_SETPOINT_1_180, "180  Room temperature - day setpoint (1)", new byte[] {(byte)0xce, (byte)0x9e, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_NIGHT_SETPOINT_1_181, "181  Room temperature night setpoint (1)", new byte[] {(byte)0xce, (byte)0xa0, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_INFLUENCE_MAX_1_182, "182  Room temperature influence - max. (-99..0) (1)", new byte[] {(byte)0xce, (byte)0x76, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_INFLUENCE_MIN_1_183, "183  Room temperature influence - min. (0..99) (1)", new byte[] {(byte)0xce, (byte)0x78, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.PROPORTIONAL_BAND_1_184, "184  Proportional band - Xp (1-250) (1)", new byte[] {(byte)0xce, (byte)0xc5, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.INTEGRATION_CONSTANT_1_185, "185  Integration constant - Tn (5..999) (1)", new byte[] {(byte)0xce, (byte)0x82, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.MOTOR_VALUE_RUNNING_TIME_1_186, "186  Motor/valve running time s (5..250) (1)", new byte[] {(byte)0xce, (byte)0xc6, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.NEUTRAL_ZONE_1_187, "187  Neutral zone °C (0..9)  (1)", new byte[] {(byte)0xce, (byte)0xc7, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.DIFFERENTIAL_1_CUT_OUT_TEMP_1_194, "194  Differential 1 - Cut-out temperature (1)", new byte[] {(byte)0xce, (byte)0xbe, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.DIFFERENTIAL_2_CUT_IN_TEMP_1_195, "195  Differential 2 - Cut-in temperature (1)", new byte[] {(byte)0xce, (byte)0xbf, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.SERVICE_PIN_LON_1_196, "196  Service pin - LON (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xf0, (byte)0x0, (byte)0x0}, ResponseFormat.BIT7));
		add(new Ecl300Command(Cmd.LON_RESET_1_197, "197  LON reset (ON/OFF) (1)", new byte[] {(byte)0xce, (byte)0xf0, (byte)0x0, (byte)0x0}, ResponseFormat.BIT6));
		add(new Ecl300Command(Cmd.SUMMER_TIME_CHANGE_1_198, "198  Summer time change (ON=automatic/OFF=manual) (1)", new byte[] {(byte)0xce, (byte)0xee, (byte)0x0, (byte)0x0}, ResponseFormat.BIT7));
		add(new Ecl300Command(Cmd.SLAVE_ADDRESS_1_199, "199  Slave address (0..9,15) (1)", new byte[] {(byte)0xce, (byte)0xeb, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.ROOM_TEMP_1_211, "211  Room temperature (1)", new byte[] {(byte)0xce, (byte)0x3e, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S2_SENSOR_REFERENCE_1_229, "229  S2 sensor 2 reference (1)", new byte[] {(byte)0xce, (byte)0x46, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S3_SENSOR_REFERENCE_1_230, "230  S3 sensor 3 reference (1)", new byte[] {(byte)0xce, (byte)0x4a, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.CHOICE_TIME_CONTROL_UNIT_2_10, "10  Choice of time control unit (0..5) (2)", new byte[] {(byte)0xce, (byte)0xe3, (byte)0x0, (byte)0x0}, ResponseFormat.NIBBLE));
		add(new Ecl300Command(Cmd.CANCEL_REDUCED_TEMP_2_11, "11  Cancellation of reduced temperature(OFF/-29..10) (2)", new byte[] {(byte)0xce, (byte)0xa7, (byte)0x0, (byte)0x0}, ResponseFormat.SIGNED_BYTE));
		add(new Ecl300Command(Cmd.REFERENCE_RAMPING_2_13, "13  Reference ramping min (0..99) (2)", new byte[] {(byte)0xce, (byte)0xab, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.OPTIMIZE_CONST_2_14, "14  Optimising constant (OFF/10..99) (2)", new byte[] {(byte)0xce, (byte)0xc1, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.ADAPT_FUNC_ROOM_TEMP_2_15, "15  Adaptive function of room temperature (OFF/1..30) (2)", new byte[] {(byte)0xce, (byte)0xd8, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.OPTIMIZE_ROOM_OUTDOOR_2_20, "20  Optimising based on room (ON)/outdoor temp  (OFF) (2)", new byte[] {(byte)0xce, (byte)0xee, (byte)0x0, (byte)0x0}, ResponseFormat.BIT1));
		add(new Ecl300Command(Cmd.TOTAL_STOP_2_21, "21  Total stop (ON/OFF) (2)", new byte[] {(byte)0xce, (byte)0xec, (byte)0x0, (byte)0x0}, ResponseFormat.BIT1));
		add(new Ecl300Command(Cmd.PUMP_MOTION_2_22, "22  Pump motion (ON/OFF) (2)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT5));
		add(new Ecl300Command(Cmd.VALVE_MOTION_2_23, "23  Valve motion (ON/OFF) (2)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT7));
		add(new Ecl300Command(Cmd.GEAR_THERMO_MOTOR_2_24, "24  Gear motor(ON) / thermo motor (OFF) (2)", new byte[] {(byte)0xce, (byte)0xed, (byte)0x0, (byte)0x0}, ResponseFormat.BIT1));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_UPPER_X_2_31, "31  Return temperature limitation - upper X °C (-30..15) (2)", new byte[] {(byte)0xce, (byte)0xb3, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_UPPER_Y_2_32, "32  Return temperature limitation - upper Y °C (10..110) (2)", new byte[] {(byte)0xce, (byte)0xb1, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_LOWER_X_2_33, "33  Return temperature limitation - lower X °C (-30..15) (2)", new byte[] {(byte)0xce, (byte)0xb2, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_LOWER_Y_2_34, "34  Return temperature limitation - lower Y °C (10..110) (2)", new byte[] {(byte)0xce, (byte)0xb0, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_INFL_MAX_2_35, "35  Return temperature influence - max. °C (-9,9 .. 9,9) (2)", new byte[] {(byte)0xce, (byte)0x7e, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT2B));
		add(new Ecl300Command(Cmd.RETURN_TEMP_LIMIT_INFL_MIN_2_36, "36  Return temperature influence - min °C (-9,9 .. 9,9) (2)", new byte[] {(byte)0xce, (byte)0x80, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT2B));
		add(new Ecl300Command(Cmd.ADAPT_FUNC_RETURN_LIMITER_2_37, "37  Adaptive function of return limiter (OFF / 1 ... 50) (2)", new byte[] {(byte)0xce, (byte)0xda, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.PRIORITY_OR_PARALLEL_OP_2_43, "43  Parallel or parallel withh reduced heating (OFF / 1 ... 99 K) (2)", new byte[] {(byte)0xce, (byte)0xcd, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.CLOSED_VALVE_PI_REGULATION_2_52, "52  Closed valve (ON)/ PI regulation (OFF) (2)", new byte[] {(byte)0xce, (byte)0xef, (byte)0x0, (byte)0x0}, ResponseFormat.BIT1));
		add(new Ecl300Command(Cmd.ACTUAL_FLOW_ENERGY_2_110, "110  Actual flow / energy (2)", new byte[] {(byte)0xce, (byte)0x88, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.ACTUAL_SETPOINT_FLOW_ENERGY_2_111, "111  Actual setpoint flow/energy (0-2999) (2)", new byte[] {(byte)0xce, (byte)0x90, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.TAU_FLOW_INTEGRATOR_2_112, "112  Tau flow integrator  (1-250) (2)", new byte[] {(byte)0xce, (byte)0xe0, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TAU_FLOW_FILTER_INTEGRATOR_2_113, "113  Tau flow filter integrator  (1-250) (2)", new byte[] {(byte)0xce, (byte)0xe1, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.TYPE_FLOW_PULSE_UNIT_2_114, "114  type of flow  / pulse unit (2)", new byte[] {(byte)0xce, (byte)0x8e, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.FLOW_UNIT_2_115, "115  Flow unit (2)", new byte[] {(byte)0xce, (byte)0xe8, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.FLOW_LIMIT_UPPER_Y_2_116, "116  Flow limiter - upper value (y coordinate) (2)", new byte[] {(byte)0xce, (byte)0x92, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.FLOW_LIMIT_LOWER_Y_2_117, "117  Flow limiter - lower value (y coordinate) (2)", new byte[] {(byte)0xce, (byte)0x94, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.OVERRIDE_VIA_SENSOR_INPUT_2_141, "141  Override via sensor input (2)", new byte[] {(byte)0xce, (byte)0xe2, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.PROTECT_2_174, "174  Protect valve min (OFF/10..59) (2)", new byte[] {(byte)0xce, (byte)0xb5, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.HEATING_CURVE_2_175, "175  Heating curve (0,2 .... 3,4) (2)", new byte[] {(byte)0xce, (byte)0xd9, (byte)0x0, (byte)0x0}, ResponseFormat.FLOAT1B));
		add(new Ecl300Command(Cmd.PARALLEL_DISPLACEMENT_2_176, "176  Parallel displacement (-9 ..9) (2)", new byte[] {(byte)0xce, (byte)0xa9, (byte)0x0, (byte)0x0}, ResponseFormat.SIGNED_BYTE));
		add(new Ecl300Command(Cmd.FLOW_TEMP_MIN_2_177, "177  Flow temperature - minimum (10..110) (2)", new byte[] {(byte)0xce, (byte)0xdb, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.FLOW_TEMP_MAX_2_178, "178  Flow temperature - maximum (10..110) (2)", new byte[] {(byte)0xce, (byte)0xdc, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.HEATING_CUT_OUT_2_179, "179  Heating cut-out (10..30) (2)", new byte[] {(byte)0xce, (byte)0xc3, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_DAY_SETPOINT_2_180, "180  Room temperature - day setpoint (2)", new byte[] {(byte)0xce, (byte)0x9f, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_NIGHT_SETPOINT_2_181, "181  Room temperature night setpoint (2)", new byte[] {(byte)0xce, (byte)0xa1, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_INFLUENCE_MAX_2_182, "182  Room temperature influence - max. (-99..0) (2)", new byte[] {(byte)0xce, (byte)0x72, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.ROOM_TEMP_INFLUENCE_MIN_2_183, "183  Room temperature influence - min. (0..99) (2)", new byte[] {(byte)0xce, (byte)0x74, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.PROPORTIONAL_BAND_2_184, "184  Proportional band - Xp (1..250) (2)", new byte[] {(byte)0xce, (byte)0xd5, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.INTEGRATION_CONSTANT_2_185, "185  Integration constant - Tn (5..999) (2)", new byte[] {(byte)0xce, (byte)0x84, (byte)0x0, (byte)0x0}, ResponseFormat.INT));
		add(new Ecl300Command(Cmd.MOTOR_VALUE_RUNNING_TIME_2_186, "186  Motor/valve running time s (5..250) (2)", new byte[] {(byte)0xce, (byte)0xd6, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.NEUTRAL_ZONE_2_187, "187  Neutral zone °(0..9)  (2)", new byte[] {(byte)0xce, (byte)0xd7, (byte)0x0, (byte)0x0}, ResponseFormat.BYTE));
		add(new Ecl300Command(Cmd.ROOM_TEMP_2_211, "211  Room temperature (2)", new byte[] {(byte)0xce, (byte)0x3e, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S2_SENSOR_REFERENCE_2_229, "229  S2 sensor 2 reference (2)", new byte[] {(byte)0xce, (byte)0x46, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S3_SENSOR_REFERENCE_2_230, "230  S3 sensor 3 reference (2)", new byte[] {(byte)0xce, (byte)0x4a, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.HOT_WATER_TEMP_DAY_SETPOINT_3_190, "190  Hot water temperature - day setpoint (3)", new byte[] {(byte)0xce, (byte)0xd0, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.HOT_WATER_TEMP_NIGHT_SETPOINT_3_191, "191  Hot water temperature - night setpoint (3)", new byte[] {(byte)0xce, (byte)0xd1, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP_INT));
		add(new Ecl300Command(Cmd.S6_SENSOR_3_206, "206  S6 sensor (3)", new byte[] {(byte)0xce, (byte)0x30, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S11_EXTRA_SENSOR_3_208, "208  S11 extra sensor (3)", new byte[] {(byte)0xce, (byte)0xfe, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));
		add(new Ecl300Command(Cmd.S12_EXTRA_SENSOR_3_209, "209  S12 extra sensor (3)", new byte[] {(byte)0xce, (byte)0x44, (byte)0x0, (byte)0x0}, ResponseFormat.TEMP));	}};
	}

	private static Map<Cmd, Ecl300Command> CMD_MAP = null;

	private static void initCommandMap()
	{
		CMD_MAP = new HashMap<Cmd, Ecl300Command>();
		List<Ecl300Command> cmdList = getCommandList();
		for (Ecl300Command cmd : cmdList) {
			CMD_MAP.put(cmd.getCmd(), cmd);
		}
	}

	public static Ecl300Command getCommand(Cmd command) {
		if (CMD_MAP == null) {
			initCommandMap();
		}
		return CMD_MAP.get(command);
	}

	private Cmd cmd;
	private String label;
	private byte[] serialCmd;
	private ResponseFormat unit;

	private Ecl300Command(Cmd cmd, String label, byte[] serialCmd, ResponseFormat unit) {
		this.cmd = cmd;
		this.label = label;
		this.serialCmd = new byte[5];
		System.arraycopy(serialCmd, 0, this.serialCmd, 0, 4);
		this.unit = unit;
	}

	public Cmd getCmd() {
		return cmd;
	}

	public String getLabel() {
		return label;
	}

	public byte[] getSerialCmd() {
		return serialCmd;
	}

	public ResponseFormat getUnit() {
		return unit;
	}

}
