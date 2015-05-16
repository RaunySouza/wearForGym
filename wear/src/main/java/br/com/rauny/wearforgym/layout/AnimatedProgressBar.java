package br.com.rauny.wearforgym.layout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

/**
 * @author raunysouza
 */
public class AnimatedProgressBar extends ProgressBar implements Animatable {

	private Animator mAnimator;
	private long mStartTime;
	private boolean mRunning;

	private long mCurrentTime;
	private CountDownTimer mInternalCounter;

	public AnimatedProgressBar(Context context) {
		this(context, null);
	}

	public AnimatedProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void startFrom(long start) {
		stop();

		mInternalCounter = new CountDownTimer(start, 1) {
			@Override
			public void onTick(long millisUntilFinished) {
				mCurrentTime = millisUntilFinished;
			}

			@Override
			public void onFinish() {
				mCurrentTime = mStartTime;
			}
		};

		mAnimator = createAnimator(start, (int) start, 0);
		mAnimator.start();
		mInternalCounter.start();
		mRunning = true;
	}

	/**
	 * Start animation with defined start time and duration
	 */
	@Override
	public void start() {
		startFrom(mStartTime);
	}

	@Override
	public void stop() {
		if (mRunning && mAnimator != null) {
			mAnimator.end();
			mRunning = false;
			stopInternalCounter();
		}
	}

	@Override
	public boolean isRunning() {
		return mRunning;
	}

	public void reverse() {
		if (mRunning && mAnimator != null) {
			mAnimator.end();
			mRunning = false;
		}

		mAnimator = createAnimator(500, (int) mCurrentTime, (int) mStartTime);
		mAnimator.start();

		stopInternalCounter();
	}

	private void stopInternalCounter() {
		if (mInternalCounter != null) {
			mInternalCounter.onFinish();
			mInternalCounter.cancel();
		}
	}

	private Animator createAnimator(long duration, int from, int to) {
		Animator animator = ObjectAnimator.ofInt(this, "progress", from, to);
		animator.setDuration(duration);
		animator.setInterpolator(new LinearInterpolator());
		return animator;
	}

	public void setStartTime(long time) {
		mStartTime = time;
		setMax((int) time);
		setProgress((int) time);
	}
}
