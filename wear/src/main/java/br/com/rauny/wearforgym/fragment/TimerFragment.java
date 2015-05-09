package br.com.rauny.wearforgym.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class TimerFragment extends Fragment {

	public static final String TIMER_PREFERENCE_NAME = "timerPrefs";
	public static final String SELECTED_TIME = "selectedTime";

	private OnFragmentInteractionListener mListener;
	private SharedPreferences sharedPreferences;

	private TimerState timerState;

	private TextView mHour;
	private TextView mMinute;
	private TextView mSecond;
	private TextView mMillisecond;
	private LinearLayout mStartStopButton;
	private ImageView mStartIcon;
	private ProgressBar mTimeProgress;

	public static TimerFragment newInstance() {
		TimerFragment fragment = new TimerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TimerFragment() {
		timerState = new TimerState();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_timer, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHour = (TextView) getActivity().findViewById(R.id.hours);
		mMinute = (TextView) getActivity().findViewById(R.id.minutes);
		mSecond = (TextView) getActivity().findViewById(R.id.seconds);
		mMillisecond = (TextView) getActivity().findViewById(R.id.milliseconds);
		mStartStopButton = (LinearLayout) getActivity().findViewById(R.id.startStopButton);
		mStartIcon = (ImageView) getActivity().findViewById(R.id.startIcon);
		mTimeProgress = (ProgressBar) getActivity().findViewById(R.id.time_progress);
		sharedPreferences = getActivity().getSharedPreferences(TIMER_PREFERENCE_NAME, Context.MODE_PRIVATE);

		timerState.selectedTime = sharedPreferences.getLong(SELECTED_TIME, 10000);
		mSecond.setText(String.format("%02d", timerState.selectedTime / 1000));

		mStartStopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTimer();
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private void vibrate(int milliseconds) {
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}

	private void startTimer() {
		if (!timerState.isTimerRunning) {
			timerState.timer = new CountDownTimer(timerState.selectedTime, 1) {
				@Override
				public void onTick(long millisUntilFinished) {
					mHour.setText(String.format("%02d", millisUntilFinished / 1000 / 60 / 60));
					mMinute.setText(String.format("%02d", millisUntilFinished / 1000 / 60));
					mSecond.setText(String.format("%02d", millisUntilFinished / 1000));
					mMillisecond.setText(String.format("%03d", millisUntilFinished % 1000));
					timerState.remainTime = millisUntilFinished;
				}

				@Override
				public void onFinish() {
					resetTimer();
					vibrate(500);
					timerState.isTimerRunning = false;
					notifyTimeOut();
					resetTimeAnimation();
					mTimeProgress.clearAnimation();
				}
			}.start();
			startAnimation(timerState.selectedTime);
			mStartIcon.setImageDrawable(getActivity().getDrawable(R.drawable.stop));
			vibrate(30);
			timerState.isTimerRunning = true;
		}
		else {
			timerState.timer.cancel();
			timerState.timer = null;
			resetTimer();
			timerState.isTimerRunning = false;
			resetTimeAnimation();
		}
	}

	private void resetTimer() {
		mHour.setText("00");
		mMinute.setText("00");
		mSecond.setText(String.format("%02d", timerState.selectedTime / 1000));
		mMillisecond.setText("000");
		mStartIcon.setImageDrawable(getActivity().getDrawable(R.drawable.start));
	}

	private void notifyTimeOut() {
		Intent intent = new Intent(this.getActivity(), ConfirmationActivity.class);
		intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
				ConfirmationActivity.SUCCESS_ANIMATION);
		intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Time Out!");
		startActivity(intent);
	}

	public void updateTimerTime(long time) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(SELECTED_TIME, time);
		editor.apply();
		timerState.selectedTime = time;
		mSecond.setText(String.format("%02d", timerState.selectedTime / 1000));
	}

	private void startAnimation(long duration) {
		mTimeProgress.clearAnimation();
		if (timerState.currentAnimator != null && timerState.currentAnimator.isRunning()) {
			timerState.currentAnimator.end();
		}
		mTimeProgress.setMax((int) duration);
		timerState.currentAnimator = ObjectAnimator.ofInt(mTimeProgress, "progress", (int) duration, 0);
		timerState.currentAnimator.setDuration(duration);
		timerState.currentAnimator.setInterpolator(new LinearInterpolator());
		timerState.currentAnimator.start();
	}

	private void resetTimeAnimation() {
		mTimeProgress.clearAnimation();
		if (timerState.currentAnimator != null && timerState.currentAnimator.isRunning()) {
			timerState.currentAnimator.end();
		}

		final int remainTime = (int) timerState.remainTime;
		int max = (int) timerState.selectedTime;
		mTimeProgress.setMax(max);
		timerState.currentAnimator = ObjectAnimator.ofInt(mTimeProgress, "progress", remainTime, max);
		timerState.currentAnimator.setDuration(500);
		timerState.currentAnimator.setInterpolator(new LinearInterpolator());
		timerState.currentAnimator.start();
	}

	private class TimerState {
		private long selectedTime = 0;
		private long remainTime = 0;
		private boolean isTimerRunning;
		private CountDownTimer timer;
		private Animator currentAnimator;
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

}
