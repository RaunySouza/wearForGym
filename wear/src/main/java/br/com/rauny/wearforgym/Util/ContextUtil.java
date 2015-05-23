package br.com.rauny.wearforgym.Util;

import android.content.Context;
import android.os.Vibrator;

/**
 * @author raunysouza
 */
public class ContextUtil {

	/**
	 * Pattern Example:
	 * <br>
	 *     {0, 100, 400, 100} = Vibrate twice for 100 milliseconds waiting 400 milliseconds between vibrations
	 * <br>
	 *     Passing -1 in repeat argument, means no repeat,]. If 0 (zero) or greater than 0 (zero), repeat
	 *     correspondent index.
	 * @param context
	 * @param pattern
	 * @param repeat
	 */
	public static void vibrate(Context context, long[] pattern, int repeat) {
		getVibrator(context).vibrate(pattern, repeat);
	}

	public static void vibrate(Context context, long duration) {
		getVibrator(context).vibrate(duration);
	}

	public static void cancelVibration(Context context) {
		getVibrator(context).cancel();
	}

	public static Vibrator getVibrator(Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
}
