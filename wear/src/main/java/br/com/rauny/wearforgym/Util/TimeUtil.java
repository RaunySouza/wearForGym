package br.com.rauny.wearforgym.Util;

/**
 * @author raunysouza
 */
public class TimeUtil {

	public static String formatHours(long milliseconds) {
		return String.format("%02d", milliseconds / 1000 / 60 / 60);
	}

	public static String formatMinutes(long milliseconds) {
		return String.format("%02d", milliseconds / 1000 / 60);
	}

	public static String formatSeconds(long milliseconds) {
		return String.format("%02d", milliseconds / 1000);
	}

	public static String formatMilliseconds(long milliseconds) {
		return String.format("%03d", milliseconds % 1000);
	}
}
