package br.com.rauny.wearforgym.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.Util.ContextUtil;
import br.com.rauny.wearforgym.activity.TimerActivity;
import br.com.rauny.wearforgym.core.api.Constants;

/**
 * @author raunysouza
 */
public class TimerService extends Service {

	public static final int NOTIFICATION_ID = 1;

	private TimerServiceListener mListener;

	private IBinder mBinder = new TimerBinder();
	private NotificationManagerCompat notificationManager;

	private CountDownTimer mCountDownTimer;
	private long mCurrentTime;
	private boolean mCountDownTimerRunning;
	private boolean mNotified;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        Log.v(Constants.TAG, "Creating Service: " + this);
		notificationManager = NotificationManagerCompat.from(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        Log.v(Constants.TAG, "Destroying Service: " + this);
		cancelNotification();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(Constants.TAG, "Binding Service");

		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.v(Constants.TAG, "Unbinding Service");
		return super.onUnbind(intent);
	}

    public boolean isRunning() {
        return mCountDownTimerRunning;
    }

	public void runInBackground() {
		if (mCountDownTimerRunning) {
			notificationManager.notify(NOTIFICATION_ID, createRunningTimeNotification());
			mNotified = true;
		}
	}

	public void runInForeground() {
		if (mNotified) {
			cancelNotification();
		}
	}

	public void start(long time) {
		if (mCountDownTimerRunning) {
			stop();
		}

		createCountDownTimer(time);

		Log.v(Constants.TAG, "Stating CountDown Timer");
		mCountDownTimer.start();
		if (mListener != null) {
			mListener.onStartCountDown();
		}
		mCountDownTimerRunning = true;
	}

	public void stop() {
		Log.v(Constants.TAG, "Stopping CountDown Timer");
		mCountDownTimer.cancel();
		mCountDownTimer = null;
		if (mNotified) {
			cancelNotification();
		}

		mCountDownTimerRunning = false;
	}

	private void createCountDownTimer(long time) {
		mCountDownTimer = new CountDownTimer(time, 1) {
			@Override
			public void onTick(long l) {
				mCurrentTime = l;
				if (mListener != null) {
					mListener.onTick(l);
				}
			}

			@Override
			public void onFinish() {
				Log.v(Constants.TAG, "Timer finished");
				if (mListener != null) {
					mListener.onFinishCountDown();
				}
				if (mNotified) {
					cancelNotification();
					notificationManager.notify(NOTIFICATION_ID, createTimeOutNotification());
				}
				ContextUtil.vibrate(TimerService.this, new long[]{0, 200, 500, 200}, -1);
				mCountDownTimerRunning = false;
			}
		};
	}

	public void setListener(TimerServiceListener listener) {
		this.mListener = listener;
	}

	private void cancelNotification() {
		notificationManager.cancel(NOTIFICATION_ID);
		mNotified = false;
	}

	private Notification createRunningTimeNotification() {
		Intent mainAppIntent = new Intent(this, TimerActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainAppIntent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.timer_remaining))
				.setSmallIcon(R.drawable.app_icon)
				.setContentIntent(pendingIntent)
				.setUsesChronometer(true)
				.setWhen(System.currentTimeMillis() + mCurrentTime)
				.setOngoing(true)
				.setAutoCancel(true);

		return builder.build();
	}

	private Notification createTimeOutNotification() {
		Intent mainAppIntent = new Intent(this, TimerActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainAppIntent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.time_out))
				.setSmallIcon(R.drawable.app_icon)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true);

		return builder.build();
	}

	public class TimerBinder extends Binder {

		public TimerService getService() {
			return TimerService.this;
		}
	}

	public interface TimerServiceListener {
		void onTick(long remaining);
		void onFinishCountDown();
		void onStartCountDown();
	}
}
