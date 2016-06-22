package br.com.rauny.wearforgym.fragment;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.Util.ContextUtil;
import br.com.rauny.wearforgym.constant.Preferences;
import br.com.rauny.wearforgym.layout.CountDownTimerLayout;
import br.com.rauny.wearforgym.service.TimerService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author raunysouza
 */
public class TimerFragment extends Fragment implements ServiceConnection, TimerService.TimerServiceListener {

	private SharedPreferences mSharedPreferences;
	private boolean mStarted;
	private long mTime;
	private boolean mVisible;
	private boolean mBound;
	private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");

	private TimerService mTimerService;
	private BroadcastReceiver mBroadcastReceiver;

	@BindView(R.id.count_down_timer)
	CountDownTimerLayout mCountDownTimer;

	@BindView(R.id.donut_progress)
	DonutProgress mDonutProgress;

	@BindView(R.id.clock_text_view)
	TextView mClockText;

	public static TimerFragment newInstance() {
		TimerFragment fragment = new TimerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_timer, container, false);
		ButterKnife.bind(this, view);

		mSharedPreferences = getActivity().getSharedPreferences(Preferences.TIMER_PREFERENCE_NAME, Context.MODE_PRIVATE);
		mTime = mSharedPreferences.getLong(Preferences.SELECTED_TIME, 10000);

		mClockText.setText(mSimpleDateFormat.format(new Date()));

		mCountDownTimer.setStartTime(mTime);
		mDonutProgress.setMax((int) mTime);
		mDonutProgress.setProgress((int) mTime);

		mCountDownTimer.setOnClickListener(viewItem -> {
			if (!mStarted) {
				start();
			} else {
				stop();
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBound) {
			mTimerService.runInForeground();
		}
		getActivity().registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		mClockText.setText(mSimpleDateFormat.format(new Date()));
		mVisible = true;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent serviceIntent = new Intent(getActivity(), TimerService.class);
		getActivity().getApplicationContext().startService(serviceIntent);
		getActivity().getApplicationContext().bindService(serviceIntent, this, Service.BIND_AUTO_CREATE);

		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mClockText.setText(mSimpleDateFormat.format(new Date()));
			}
		};
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mBound) {
			mTimerService.runInBackground();
		}
		getActivity().unregisterReceiver(mBroadcastReceiver);
		mVisible = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBound) {
			getActivity().getApplicationContext().unbindService(this);
			mBound = false;
		}
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
		ContextUtil.vibrate(getActivity(), 50);
		mStarted = true;
	}

	private void stop() {
		mCountDownTimer.update(mTime);
		mDonutProgress.setProgress((int) mTime);
		mTimerService.stop();
		mStarted = false;
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
		mTimerService.setListener(null);
	}
}
