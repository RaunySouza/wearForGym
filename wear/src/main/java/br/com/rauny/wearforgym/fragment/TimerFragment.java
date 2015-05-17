package br.com.rauny.wearforgym.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.rauny.wearforgym.MainActivity;
import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.constant.Preferences;
import br.com.rauny.wearforgym.layout.AnimatedProgressBar;
import br.com.rauny.wearforgym.layout.CountDownTimerLayout;
import br.com.rauny.wearforgym.service.TimerService;

/**
 * @author raunysouza
 */
public class TimerFragment extends Fragment implements ServiceConnection {

	private OnFragmentInteractionListener mListener;
	private SharedPreferences mSharedPreferences;
	private TimerService mTimerService;

	private TimerState mTimerState;

	private LinearLayout mStartStopButton;
	private ImageView mStartIcon;
	private AnimatedProgressBar mAnimatedProgressBar;

	private CountDownTimerLayout mCountDownTimer;

	public static TimerFragment newInstance() {
		TimerFragment fragment = new TimerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TimerFragment() {
		mTimerState = new TimerState();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_timer, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mSharedPreferences = getActivity().getSharedPreferences(Preferences.TIMER_PREFERENCE_NAME, Context.MODE_PRIVATE);
		mTimerState.selectedTime = mSharedPreferences.getLong(Preferences.SELECTED_TIME, 10000);

		mStartStopButton = (LinearLayout) getActivity().findViewById(R.id.startStopButton);
		mStartIcon = (ImageView) getActivity().findViewById(R.id.startIcon);
		mAnimatedProgressBar = (AnimatedProgressBar) getActivity().findViewById(R.id.time_progress);
		mAnimatedProgressBar.setStartTime(mTimerState.selectedTime);

		mCountDownTimer = (CountDownTimerLayout) getActivity().findViewById(R.id.count_down_timer);
		mCountDownTimer.setStartTime(mTimerState.selectedTime);
		mCountDownTimer.addCountDownTimerListener(new CountDownTimerListenerImpl());

		mStartStopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCountDownTimer.start();
			}
		});

		Intent intent = new Intent(getActivity(), TimerService.class);
		getActivity().getApplicationContext().startService(intent);
		getActivity().getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
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
	public void onDestroy() {
		getActivity().getApplicationContext().unbindService(this);
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onStop() {
		if (mTimerState.isTimerRunning) {
			mTimerService.startTimer(mTimerState.remainTime);
			createNotification();
		}
		mCountDownTimer.stop();
		super.onStop();
	}

	private void createNotification() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

		NotificationCompat.WearableExtender wearableExtender =
				new NotificationCompat.WearableExtender();

		NotificationCompat.Builder wearableNotificationBuilder = new NotificationCompat.Builder(getActivity())
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Title wearable")
				.setContentText("Text wearable")
				.setOngoing(false)
				.setOnlyAlertOnce(true)
				.setGroup("GROUP")
				.setGroupSummary(false);
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
		notificationManager.notify(12345, wearableNotificationBuilder.build());
	}


	private void vibrate(int milliseconds) {
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}

	private void notifyTimeOut() {
		Intent intent = new Intent(this.getActivity(), ConfirmationActivity.class);
		intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
		intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Time Out!");
		startActivity(intent);
	}

	public void updateTimerTime(long time) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(Preferences.SELECTED_TIME, time);
		editor.apply();
		mCountDownTimer.stop();
		mTimerState.selectedTime = time;
		mAnimatedProgressBar.setStartTime(time);
		mCountDownTimer.setStartTime(time);
		mCountDownTimer.reset();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		TimerService.TimerBinder binder = (TimerService.TimerBinder) service;
		mTimerService = binder.getService();
		long timeInService = mTimerService.getCurrentTime();
		if (timeInService > 0) {
			mCountDownTimer.startFrom(timeInService);
		}
		mTimerService.stop();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mTimerService = null;
	}

	private class TimerState {
		private long selectedTime = 0;
		private long remainTime = 0;
		private boolean isTimerRunning;
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

	private class CountDownTimerListenerImpl implements CountDownTimerLayout.CountDownTimerListener {

		@Override
		public void onTick(long millisUntilFinished) {
			mTimerState.remainTime = millisUntilFinished;
		}

		@Override
		public void onFinish() {
			vibrate(500);
			notifyTimeOut();
			mTimerState.remainTime = 0;
		}

		@Override
		public void onStart(long initial) {
			mAnimatedProgressBar.startFrom(initial);
			mStartIcon.setImageDrawable(getActivity().getDrawable(R.drawable.stop));
			vibrate(30);
			mTimerState.isTimerRunning = true;
		}

		@Override
		public void onResetTimer() {
			mStartIcon.setImageDrawable(getActivity().getDrawable(R.drawable.start));
			if (mTimerState.isTimerRunning) {
				mAnimatedProgressBar.reverse();
				mTimerState.isTimerRunning = false;
				mTimerState.remainTime = 0;
			}
		}
	}
}
