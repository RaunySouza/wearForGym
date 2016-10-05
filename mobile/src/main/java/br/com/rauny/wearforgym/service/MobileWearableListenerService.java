package br.com.rauny.wearforgym.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import br.com.rauny.wearforgym.core.api.Constants;
import br.com.rauny.wearforgym.core.api.WearableApi;
import br.com.rauny.wearforgym.model.Time;
import br.com.rauny.wearforgym.model.Time_Table;

/**
 * @author raunysouza
 */
public class MobileWearableListenerService extends WearableListenerService {

    private WearableApi mWearableApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mWearableApi = WearableApi.getInstance(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v(Constants.TAG, "Message Received: " + messageEvent.getPath());
        if (messageEvent.getPath().equals(Constants.path.SYNC)) {
            Time time = SQLite.select().from(Time.class).where(Time_Table.selected.eq(true)).querySingle();
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.extra.TIME, time.getMillis());
            mWearableApi.sendData(Constants.path.SYNC, bundle);
        }
    }
}
