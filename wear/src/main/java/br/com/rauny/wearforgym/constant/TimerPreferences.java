package br.com.rauny.wearforgym.constant;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class TimerPreferences {

	private static final String PREFERENCE_NAME = "timer";
	private static final String PROP_SELECTED_TIME = "selectedTime";

    private static TimerPreferences instance;

    private SharedPreferences mSharedPreferences;
    private long selectedTime;

    public static TimerPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new TimerPreferences(context);
        }

        return instance;
    }

	private TimerPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        selectedTime = mSharedPreferences.getLong(PROP_SELECTED_TIME, Constants.defaults.TIME);
	}

    public long getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(long selectedTime) {
        this.selectedTime = selectedTime;
        mSharedPreferences.edit()
                .putLong(PROP_SELECTED_TIME, selectedTime)
                .apply();
    }
}
