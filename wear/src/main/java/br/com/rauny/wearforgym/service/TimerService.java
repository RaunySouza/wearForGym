package br.com.rauny.wearforgym.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import br.com.rauny.wearforgym.MainActivity;
import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class TimerService extends Service {

	public static final int NOTIFICATION_ID = 001;

	private static final String TAG = TimerService.class.getSimpleName();

	private IBinder mBinder = new TimerBinder();
	private long mCurrentTime;
	private CountDownTimer mCountDownTimer;
	private NotificationManagerCompat notificationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = NotificationManagerCompat.from(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		stop();
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "Unbinding Service");
		return super.onUnbind(intent);
	}

	public void startTimer(long initialTime) {
		Log.i(TAG, "Starting Timer at " + initialTime + "ms");
		mCountDownTimer = new CountDownTimer(initialTime, 1) {
			@Override
			public void onTick(long millisUntilFinished) {
				mCurrentTime = millisUntilFinished;
			}

			@Override
			public void onFinish() {
				Log.i(TAG, "Timer in service finishes!");
				mCurrentTime = 0;
			}
		}.start();
		createNotification(initialTime);
	}

	public void stop() {
		if (mCountDownTimer != null) {
			mCountDownTimer.onFinish();
			mCountDownTimer.cancel();
			notificationManager.cancel(NOTIFICATION_ID);
		}
	}

	public long getCurrentTime() {
		return mCurrentTime;
	}

	private void createNotification(long duration) {
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		NotificationCompat.Builder wearableNotificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.app_icon)
				.setContentTitle(getString(R.string.timer_remaining))
				.setContentText(getString(R.string.timer_remaining))
				.setContentIntent(pendingIntent)
				.setOngoing(true)
				.setUsesChronometer(true)
				.setWhen(System.currentTimeMillis() + duration)
				.setLocalOnly(true);

		notificationManager.notify(NOTIFICATION_ID, wearableNotificationBuilder.build());
	}

	public class TimerBinder extends Binder {

		public TimerService getService() {
			return TimerService.this;
		}
	}
}
