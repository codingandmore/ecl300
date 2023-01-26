package org.jensix.ecl300;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ValueLogger {

	private Date startTimeOfCurrentFile;
	private FileOutputStream logFile;
	PrintWriter writer;
	private String pathPrefix;
	private int timeInterval = 60000; // once per 60 seconds
	Cmd[] sampleSet; // parameter set of values to log
	private static Ecl300Controller controller;

	private class LoggerTask extends TimerTask {

		@Override
		public void run() {
			getValuesAndLog();
		}
	}

	public ValueLogger(String pathPrefix, Cmd[] parameters) {
		super();
		controller = Ecl300Controller.getInstance();
		this.pathPrefix = pathPrefix;
		this.sampleSet = parameters;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public void logValues() {
		openNewFile();
		startLogging();
	}

	public void stopLogging() {
		closeFile();
		try {
			controller.disconnect();
		} catch (IOException e) {
			System.err.println("Error while disconnecting: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void getValuesAndLog() {
		Object[] sampleValues;
		try {
			Date now = new Date();
			sampleValues = getSampleValues();
			logValues(sampleValues, now);
		} catch (IOException e) {
			System.err.println("Error sending command: " + e);
			e.printStackTrace();
		}
	}

	private void startLogging() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate( new LoggerTask(), 0, timeInterval);

		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
			stopLogging();
		    }
		 });
	}

	private void logValues(Object[] sampleValues, Date time) {
		checkNewFile();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(time);
		writer.print(formattedDate);
		writer.print(", ");

		for (int i=0; i<sampleValues.length; i++) {
			writer.print(sampleValues[i].toString());
			if (i<sampleValues.length-1) {
				writer.print(", ");
			}
		}
		writer.println();
		writer.flush();
	}

	private void checkNewFile() {
		Date now = new Date();
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(now);
		cal2.setTime(startTimeOfCurrentFile);
		int dayOfYear1 = cal1.get(Calendar.DAY_OF_YEAR);
		int dayOfYear2 = cal2.get(Calendar.DAY_OF_YEAR);
		if (dayOfYear1 != dayOfYear2) {
			switchFile();
		}
	}

	private void switchFile() {
		closeFile();
		openNewFile();
	}

	private Object[] getSampleValues() throws IOException {
		Object[] samples = new Object[sampleSet.length];
		for (int i=0; i<sampleSet.length; i++) {
			samples[i] = controller.getReadValue(sampleSet[i]);
		}
		return samples;
	}

	private void openNewFile() {
		startTimeOfCurrentFile = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String formattedDate = formatter.format(startTimeOfCurrentFile);
		String filePath = pathPrefix + "-" + formattedDate + ".log";
		try {
			logFile = new FileOutputStream(filePath);
			writer = new PrintWriter(logFile);
			logHeader();
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

	private void logHeader() {
		writer.print("Timestamp");
		for (Cmd cmd: sampleSet) {
			writer.print(", ");
			String label = getLabel(cmd);
			writer.print(label);
		}
		writer.println();
	}

	private final String getLabel(Cmd cmd) {
		Ecl300Command eclCmd = Ecl300Command.getCommand(cmd);
		return eclCmd.getLabel();
	}
}
