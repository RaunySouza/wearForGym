package br.com.rauny.wearforgym.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.Util.TimeUtil;

/**
 * @author raunysouza
 */
public class CountDownTimerLayout extends LinearLayout {

	private TextView mHours;
	private TextView mMinutes;
	private TextView mSeconds;

	private long mStartTime;

	public CountDownTimerLayout(Context context) {
		this(context, null);
	}

	public CountDownTimerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.count_down_timer_layout, this, true);
		mHours = (TextView) getChildAt(0);
		mMinutes = (TextView) getChildAt(2);
		mSeconds = (TextView) getChildAt(4);

		setOrientation(HORIZONTAL);
	}

	public void setStartTime(long time) {
		mStartTime = time;
		reset();
	}

	public void reset() {
		String[] formattedTime = TimeUtil.formatTime(mStartTime);
		mHours.setText(formattedTime[0]);
		mMinutes.setText(formattedTime[1]);
		mSeconds.setText(formattedTime[2]);
	}

	public void update(long currentTime) {
		String[] formattedTime = TimeUtil.formatTime(currentTime);
		mHours.setText(formattedTime[0]);
		mMinutes.setText(formattedTime[1]);
		mSeconds.setText(formattedTime[2]);
	}
}
