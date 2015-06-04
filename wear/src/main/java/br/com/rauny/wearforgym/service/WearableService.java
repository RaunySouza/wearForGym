package br.com.rauny.wearforgym.service;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * @author raunysouza
 */
public class WearableService extends WearableListenerService {

	private static final String TAG = WearableService.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		super.onMessageReceived(messageEvent);
		Log.i(TAG, "Message Received " + messageEvent.getPath());
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {

		for (DataEvent event : dataEvents) {

			// Check the data type
			if (event.getType() == DataEvent.TYPE_CHANGED) {
				// Check the data path
				Log.v(TAG, "Data received on watch: " + event.getDataItem().getUri().getPath());
			}
		}
	}




}
