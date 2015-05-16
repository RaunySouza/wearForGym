package br.com.rauny.wearforgym.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import br.com.rauny.wearforgym.constant.Extras;

/**
 * @author raunysouza
 */
public class TimerService extends Service {

	private static final String TAG = TimerService.class.getSimpleName();

	private IBinder mBinder = new TimerBinder();
	private long mCurrentTime;
	private CountDownTimer mCountDownTimer;

	@Override
	public IBinder onBind(Intent intent) {
		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
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
	}

	public long getCurrentTime() {
		return mCurrentTime;
	}

	public class TimerBinder extends Binder {

		public TimerService getService() {
			return TimerService.this;
		}
	}
}
