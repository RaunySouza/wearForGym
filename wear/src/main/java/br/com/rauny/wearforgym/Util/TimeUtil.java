package br.com.rauny.wearforgym.Util;

/**
 * @author raunysouza
 */
public class TimeUtil {

	public static String[] formatTime(long time) {
		if (time < 0) {
			throw new IllegalArgumentException("Time must be positive");
		}
		long seconds = time / 1000;
		long hundreds = (time - seconds * 1000) / 10;
		long minutes = seconds / 60;
		seconds = seconds - minutes * 60;
		long hours = minutes / 60;
		minutes = minutes - hours * 60;
		if (hours > 999) {
			hours = 0;
		}

		// Normalize values
		if (hundreds != 0) {
			seconds++;
			if (seconds == 60) {
				seconds = 0;
				minutes++;
				if (minutes == 60) {
					minutes = 0;
					hours++;
				}
			}
		}

		String formattedHours = "00";
		String formattedMinutes = "00";
		String formattedSeconds;
		String formattedMilliseconds;
		// Hours may be empty
		if (hours > 0) {
			formattedHours = String.format("%02d", hours);
		}

		if (minutes > 0) {
			formattedMinutes = String.format("%02d", minutes);
		}

		// Seconds are always two digits
		formattedSeconds = String.format("%02d", seconds);
		formattedMilliseconds = String.format("%03d", time % 1000);

		return new String[]{formattedHours, formattedMinutes, formattedSeconds, formattedMilliseconds};
	}
}
