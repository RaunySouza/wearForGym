package br.com.rauny.wearforgym.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import br.com.rauny.wearforgym.core.api.Constants;
import br.com.rauny.wearforgym.preference.TimerPreferences;

/**
 * @author raunysouza
 */
public class WearForGymWearableListenerService extends WearableListenerService {

    private LocalBroadcastManager localBroadcastManager;
    private TimerPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mPreferences = TimerPreferences.getInstance(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.d(Constants.TAG, "Data Received");
        for (DataEvent dataEvent : dataEventBuffer) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                long time = dataMap.getLong(Constants.extra.TIME);
                mPreferences.setSelectedTime(time);

                Intent intent = new Intent(Constants.receiver.SYNC);
                intent.putExtra(Constants.extra.TIME, time);
                localBroadcastManager.sendBroadcast(intent);
            }
        }
    }
}
