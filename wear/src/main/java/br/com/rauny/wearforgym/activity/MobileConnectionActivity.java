package br.com.rauny.wearforgym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;

import java.util.Date;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.constant.TimerPreferences;

/**
 * @author raunysouza
 */
public class MobileConnectionActivity extends Activity {

    private TimerPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = TimerPreferences.getInstance(this);
        long lastSync = mPreferences.getLastSync();
        //Only update after 30 minutes
        if ((new Date().getTime() - lastSync) >= 1800000) {
            setContentView(R.layout.activity_mobile_connection);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(s -> {
            });
            sync();
        } else {
            startTimerActivity();
        }
    }

    private void sync() {
        mPreferences.setLastSync(new Date().getTime());
        new Handler().postDelayed(this::startTimerActivity, 2000);
    }

    private void startTimerActivity() {
        startActivity(new Intent(MobileConnectionActivity.this, TimerActivity.class));
        finish();
    }
}
