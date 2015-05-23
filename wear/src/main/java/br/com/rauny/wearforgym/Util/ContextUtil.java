package br.com.rauny.wearforgym.Util;

import android.content.Context;
import android.os.Vibrator;

/**
 * @author raunysouza
 */
public class ContextUtil {

	public static void vibrate(Context context, long duration) {
		getVibrator(context).vibrate(duration);
	}

	public static Vibrator getVibrator(Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
}
