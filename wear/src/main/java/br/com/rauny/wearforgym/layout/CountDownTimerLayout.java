package br.com.rauny.wearforgym.layout;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class CountDownTimerLayout extends LinearLayout implements Animatable {

	private CountDownTimerListener mListener;
	private CountDownTimer mTimer;

	private TextView mHours;
	private TextView mMinutes;
	private TextView mSeconds;
	private TextView mMilliseconds;

	private long mStartTime;
	private boolean mRunning;
	private boolean mVibrateOnFinish = true;

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
		mMilliseconds = (TextView) getChildAt(6);

		setOrientation(HORIZONTAL);
	}

	/**
	 * Start the count down from time passed in parameter, without change the original start time
	 *
	 * @param initial
	 */
	public void startFrom(long initial) {
		if (!mRunning) {
			mTimer = new CountDownTimer(initial, 1) {
				@Override
				public void onTick(long millisUntilFinished) {
					mHours.setText(String.format("%02d", millisUntilFinished / 1000 / 60 / 60));
					mMinutes.setText(String.format("%02d", millisUntilFinished / 1000 / 60));
					mSeconds.setText(String.format("%02d", millisUntilFinished / 1000));
					mMilliseconds.setText(String.format("%03d", millisUntilFinished % 1000));

					if (mListener != null) {
						mListener.onTick(millisUntilFinished);
					}
				}

				@Override
				public void onFinish() {
					reset();
					if (mListener != null) {
						mListener.onFinish();
					}
					if (mVibrateOnFinish) {
						vibrate(500);
					}
					mRunning = false;

				}
			}.start();
			if (mListener != null) {
				mListener.onStart(initial);
			}
			mRunning = true;
		}
		else {
			stop();
		}
	}

	/**
	 * Start the count down from defined start time
	 */
	@Override
	public void start() {
		startFrom(mStartTime);
	}

	@Override
	public void stop() {
		if (mRunning) {
			mTimer.cancel();
			mTimer = null;
			reset();
		}
		mRunning = false;
	}

	@Override
	public boolean isRunning() {
		return mRunning;
	}

	private void vibrate(int milliseconds) {
		Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}

	public void setStartTime(long time) {
		mStartTime = time;
		mSeconds.setText(String.format("%02d", time / 1000));
	}

	public void setVibrateOnFinish(boolean flag) {
		mVibrateOnFinish = flag;
	}

	public void addCountDownTimerListener(CountDownTimerListener listener) {
		mListener = listener;
	}

	public void reset() {
		mHours.setText("00");
		mMinutes.setText("00");
		mSeconds.setText(String.format("%02d", mStartTime / 1000));
		mMilliseconds.setText("000");
		if (mListener != null) {
			mListener.onResetTimer();
		}
	}

	public interface CountDownTimerListener {
		void onTick(long millisUntilFinished);

		void onFinish();

		void onStart(long initial);

		void onResetTimer();
	}

}
