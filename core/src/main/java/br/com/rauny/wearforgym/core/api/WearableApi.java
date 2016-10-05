package br.com.rauny.wearforgym.core.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

/**
 * @author raunysouza
 */
public class WearableApi {

    private static WearableApi instance;

    private GoogleApiClient mGoogleApiClient;

    public static WearableApi getInstance(Context context) {
        if (instance == null) {
            instance = new WearableApi(context);
        }

        return instance;
    }

    private WearableApi(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public void getConnectedNodes(@NonNull ResultCallback<? super NodeApi.GetConnectedNodesResult> callback) {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(callback, 60000, TimeUnit.MILLISECONDS);
    }

    public void sendMessage(@NonNull Node node, @NonNull String message, byte[] data) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), message, data);
    }

    public void sendMessage(@NonNull Node node, @NonNull String message) {
        sendMessage(node, message, null);
    }

    public void sendMessage(@Nullable String message, byte[] data) {
        getConnectedNodes(result -> {
            for (Node node : result.getNodes()) {
                sendMessage(node, message, data);
            }
        });
    }

    public void sendMessage(@Nullable String message) {
        sendMessage(message, null);
    }

    public void sendData(String path, Bundle data) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
        putDataMapRequest.getDataMap().putAll(DataMap.fromBundle(data));
        Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest().setUrgent())
            .setResultCallback(dataItemResult -> {
                if (dataItemResult.getStatus().isSuccess())
                    Log.v(Constants.TAG, "Data sent");
                else
                    Log.d(Constants.TAG, dataItemResult.getStatus().getStatusMessage());
            });
    }
}
