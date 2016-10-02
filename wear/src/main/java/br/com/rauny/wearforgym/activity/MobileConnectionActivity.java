package br.com.rauny.wearforgym.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class MobileConnectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_connection);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(s -> {});
        sync();
    }

    private void sync() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MobileConnectionActivity.this, TimerActivity.class));
            finish();
        }, 2000);
    }
}
