package br.com.rauny.wearforgym.fragment;

import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.constant.Preferences;
import br.com.rauny.wearforgym.layout.CountDownTimerLayout;
import br.com.rauny.wearforgym.service.TimerService;

/**
 * @author raunysouza
 */
public class TimerFragment extends Fragment implements ServiceConnection, TimerService.TimerServiceListener {

	private static final String TAG = TimerFragment.class.getSimpleName();

	private SharedPreferences mSharedPreferences;
	private boolean mStarted;
	private long mTime;
	private boolean mVisible;
	private boolean mBound;

	private TimerService mTimerService;

	private CountDownTimerLayout mCountDownTimer;
	private Chronometer mTrainingTimeChronometer;
	private ImageButton mStartStopTrainingButton;
	private DonutProgress mDonutProgress;
	private ImageButton mPreviousTrainingButton;
	private ImageButton mNextTrainingButton;
	private TextView mCurrentExerciseText;
	private TextView mCurrentRepetitionsText;

	public static TimerFragment newInstance() {
		TimerFragment fragment = new TimerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_timer, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		mVisible = true;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent serviceIntent = new Intent(getActivity(), TimerService.class);
		getActivity().getApplicationContext().startService(serviceIntent);
		getActivity().getApplicationContext().bindService(serviceIntent, this, Service.BIND_AUTO_CREATE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSharedPreferences = getActivity().getSharedPreferences(Preferences.TIMER_PREFERENCE_NAME, Context.MODE_PRIVATE);
		mTime = mSharedPreferences.getLong(Preferences.SELECTED_TIME, 10000);

		mCountDownTimer = findViewById(R.id.count_down_timer);
		mTrainingTimeChronometer = findViewById(R.id.training_time_chronometer);
		mStartStopTrainingButton = findViewById(R.id.start_stop_training_button);
		mDonutProgress = findViewById(R.id.donut_progress);
		mPreviousTrainingButton = findViewById(R.id.previous_training_button);
		mNextTrainingButton = findViewById(R.id.next_training_button);
		mCurrentExerciseText = findViewById(R.id.current_exercise_text);
		mCurrentRepetitionsText = findViewById(R.id.current_repetitions_text);

		mCountDownTimer.setStartTime(mTime);
		mDonutProgress.setMax((int) mTime);
		mDonutProgress.setProgress((int) mTime);

		mCountDownTimer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!mStarted) {
					start();
				} else {
					stop();
				}
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mBound) {
			getActivity().getApplicationContext().unbindService(this);
			mBound = false;
		}
		mVisible = false;
	}

	private void vibrate(int milliseconds) {
		Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}

	public void updateTimerTime(long time) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putLong(Preferences.SELECTED_TIME, time);
		editor.apply();
		mTime = time;
		mCountDownTimer.setStartTime(time);
		mDonutProgress.setMax((int) time);
		mDonutProgress.setProgress((int) time);
	}

	private void start() {
		mTimerService.start(mTime);
		vibrate(10);
		mStarted = true;
	}

	private void stop() {
		mCountDownTimer.update(mTime);
		mDonutProgress.setProgress((int) mTime);
		mTimerService.stop();
		mStarted = false;
	}

	private <T extends View> T findViewById(int id) {
		return (T) getActivity().findViewById(id);
	}

	@Override
	public void onTick(long remaining) {
		if (mVisible) {
			mCountDownTimer.update(remaining);
			mDonutProgress.setProgress((int) remaining);
		}
	}

	@Override
	public void onFinishCountDown() {
		if (mVisible) {
			stop();
			Intent intent = new Intent(this.getActivity(), ConfirmationActivity.class);
			intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
			intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.time_out));
			startActivity(intent);
		}
		mStarted = false;
	}

	@Override
	public void onStartCountDown() {

	}

	@Override
	public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
		this.mTimerService = ((TimerService.TimerBinder) iBinder).getService();
		mTimerService.setListener(this);
		mBound = true;
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		mBound = false;
	}
}
