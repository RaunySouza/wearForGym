package br.com.rauny.wearforgym.activity;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.Util.ContextUtil;
import br.com.rauny.wearforgym.constant.Constants;
import br.com.rauny.wearforgym.layout.CountDownTimerLayout;
import br.com.rauny.wearforgym.service.TimerService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author raunysouza
 */
public class TimerActivity extends Activity implements ServiceConnection, TimerService.TimerServiceListener {

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");

    private TimerService mTimerService;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mClockText.setText(mSimpleDateFormat.format(new Date()));
        }
    };
    private BroadcastReceiver mSyncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTime = intent.getLongExtra("value", Constants.defaults.TIME);
            mCountDownTimer.setStartTime(mTime);
            mDonutProgress.setMax((int) mTime);
            mDonutProgress.setProgress((int) mTime);
        }
    };
    private long mTime;
    private boolean mStarted;
    private boolean mVisible;
    private LocalBroadcastManager mLocalBroadcastManager;

    @BindView(R.id.count_down_timer)
    CountDownTimerLayout mCountDownTimer;

    @BindView(R.id.donut_progress)
    DonutProgress mDonutProgress;

    @BindView(R.id.clock_text_view)
    TextView mClockText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mTime = getIntent().getLongExtra(Constants.extra.TIME, Constants.defaults.TIME);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(s -> {
            ButterKnife.bind(TimerActivity.this);

            mCountDownTimer.setStartTime(mTime);
            mDonutProgress.setMax((int) mTime);
            mDonutProgress.setProgress((int) mTime);
            mClockText.setText(mSimpleDateFormat.format(new Date()));
        });

        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
        bindService(intent, this, Service.BIND_AUTO_CREATE);
        mLocalBroadcastManager.registerReceiver(mSyncBroadcastReceiver, new IntentFilter("sync"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVisible = true;
        if (isBound()) {
            mTimerService.runInForeground();
        }
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVisible = false;
        if (isBound()) {
            mTimerService.runInBackground();
            mTimerService.setListener(null);
        }

        unbindService(this);
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        Log.d(TimerActivity.class.getSimpleName(), "Activity Destroyed: " + this);
        mLocalBroadcastManager.unregisterReceiver(mSyncBroadcastReceiver);
        super.onDestroy();
    }

    @OnClick(R.id.count_down_timer)
    public void onCountdownTimeClicked() {
        if (!mStarted) {
            start();
        } else {
            stop();
        }
    }

    private void start() {
        mTimerService.start(mTime);
        ContextUtil.vibrate(this, 50);
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
            Intent intent = new Intent(this, ConfirmationActivity.class);
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
        mStarted = mTimerService.isRunning();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mTimerService.setListener(null);
        mTimerService = null;
    }

    private boolean isBound() {
        return mTimerService != null;
    }
}
