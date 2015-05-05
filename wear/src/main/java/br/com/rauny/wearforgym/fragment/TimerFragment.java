package br.com.rauny.wearforgym.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class TimerFragment extends Fragment {

	public static final String TIMER_PREFERENCE_NAME = "timerPrefs";
	public static final String SELECTED_TIME = "selectedTime";

	private OnFragmentInteractionListener mListener;
	private SharedPreferences sharedPreferences;

	private long selectedTime;
	private boolean isTimerRunning;
	private CountDownTimer timer;

	private TextView mHour;
	private TextView mMinute;
	private TextView mSecond;
	private TextView mMillisecond;
	private LinearLayout mStartStopButton;
	private ImageView mStartIcon;
	private AnimatedVectorDrawable timerVector;

	public static TimerFragment newInstance() {
		TimerFragment fragment = new TimerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TimerFragment() {

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
		sharedPreferences = getActivity().getSharedPreferences(TIMER_PREFERENCE_NAME, Context.MODE_PRIVATE);

		selectedTime = sharedPreferences.getLong(SELECTED_TIME, 10000);
		mSecond.setText(String.format("%02d", selectedTime / 1000));

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

	private void setAnimatorSet(AnimatorSet set) {
		ImageView i = (ImageView) getActivity().findViewById(R.id.vector);
		timerVector = (AnimatedVectorDrawable) i.getDrawable();
		Drawable.ConstantState c = timerVector.getConstantState();
		try {
			Field f = c.getClass().getDeclaredField("mAnimators");
			f.setAccessible(true);
			List<AnimatorSet> l = (List<AnimatorSet>) f.get(c);
			/*AnimatorSet oldSet = l.get(0);
			l.clear();
			l.add(set);*/
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void vibrate() {
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		long[] vibrationPattern = {0, 500, 50, 300};
		//-1 - don't repeat
		final int indexInPatternToRepeat = -1;
		vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
	}

	private void startTimer() {
		if (!isTimerRunning) {
			timer = new CountDownTimer(selectedTime, 1) {
				@Override
				public void onTick(long millisUntilFinished) {
					mHour.setText(String.format("%02d", millisUntilFinished / 1000 / 60 / 60));
					mMinute.setText(String.format("%02d", millisUntilFinished / 1000 / 60));
					mSecond.setText(String.format("%02d", millisUntilFinished / 1000));
					mMillisecond.setText(String.format("%03d", millisUntilFinished % 1000));
				}

				@Override
				public void onFinish() {
					resetTimer();
					vibrate();
					isTimerRunning = false;
					notifyTimeOut();
				}
			}.start();
			mStartIcon.setImageDrawable(getActivity().getDrawable(R.drawable.stop));
			vibrate();
			isTimerRunning = true;
		}
		else {
			timer.cancel();
			timer = null;
			resetTimer();
			isTimerRunning = false;
		}
	}

	private void resetTimer() {
		mHour.setText("00");
		mMinute.setText("00");
		mSecond.setText(String.format("%02d", selectedTime / 1000));
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
		editor.commit();
		selectedTime = time;
		mSecond.setText(String.format("%02d", selectedTime / 1000));
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

}
