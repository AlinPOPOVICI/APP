package com.example.alin.app1.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.alin.app1.FenceBroadcastReceiver;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class AwarenessService extends Service {
    private static final String TAG = "Fence_AwarenessService";
    private Looper looper;
    private MyServiceHandler myServiceHandler;

    private android.content.Context context;
    private GoogleApiClient client;
    private FenceBroadcastReceiver mFenceReceiver;
    private boolean isRunning  = false;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private static final String MY_FENCE_RECEIVER_ACTION = "MY_FENCE_ACTION";
    public static final String HEADPHONE_FENCE_KEY = "HeadphoneFenceKey";
    public static final String WALKING_FENCE_KEY = "WalkingFenceKey";
    public static final String RUNNING_FENCE_KEY = "RunningFenceKey";
    public static final String CYCLING_FENCE_KEY = "CyclingFenceKey";
    public static final String STATIONARY_FENCE_KEY = "StationaryFenceKey";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        HandlerThread handlerthread = new HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND);
        handlerthread.start();
        looper = handlerthread.getLooper();
        myServiceHandler = new MyServiceHandler(looper);
        isRunning = true;

        client = new GoogleApiClient.Builder(AwarenessService.this)
                .addApi(Awareness.API)
                .build();
        client.connect();

        mFenceReceiver = new FenceBroadcastReceiver();
        //registerReceiver(mFenceReceiver, new IntentFilter(MY_FENCE_RECEIVER_ACTION));
        addHeadphoneFence();
        addCyclingFence();
        addRunningFence();
        addStationaryFence();
        addWalkingFence();
        addCustom();

    }
    @Override
    public void onDestroy() {
        unregisterReceiver(mFenceReceiver);
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = myServiceHandler.obtainMessage();
        msg.arg1 = startId;
        myServiceHandler.sendMessage(msg);
        Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.
        return android.app.Service.START_STICKY;

    }
    private void addCustom(){
        IntentFilter filter = new IntentFilter(MY_FENCE_RECEIVER_ACTION);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(mFenceReceiver, filter);
        Log.i(TAG, "Boot Fence was successfully registered.");

    }


    private void addHeadphoneFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(AwarenessService.this, 10001, intent, 0);
        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(HEADPHONE_FENCE_KEY, headphoneFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Headphone Fence was successfully registered.");
                        } else {
                            Log.e(TAG, "Headphone Fence could not be registered: " + status);
                        }
                    }
                });
    }

    private void addWalkingFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(AwarenessService.this, 10001, intent, 0);
        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(WALKING_FENCE_KEY, activityFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, " Walking Fence was successfully registered.");
                        } else {
                            Log.e(TAG, " Walking Fence could not be registered: " + status);
                        }
                    }
                });

    }

    private void addRunningFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(AwarenessService.this, 10001, intent, 0);
        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.RUNNING);
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(RUNNING_FENCE_KEY, activityFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, " Running Fence was successfully registered.");
                        } else {
                            Log.e(TAG, " Running Fence could not be registered: " + status);
                        }
                    }
                });

    }


    private void addCyclingFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(AwarenessService.this, 10001, intent, 0);
        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE);
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(CYCLING_FENCE_KEY, activityFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, " Cycling Fence was successfully registered.");
                        } else {
                            Log.e(TAG, " Cycling Fence could not be registered: " + status);
                        }
                    }
                });

    }

    private void addStationaryFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);
        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(AwarenessService.this, 10001, intent, 0);
        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.STILL);
        Awareness.FenceApi.updateFences(
                client,
                new FenceUpdateRequest.Builder()
                        .addFence(STATIONARY_FENCE_KEY, activityFence, mFencePendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, " Stationary Fence was successfully registered.");
                        } else {
                            Log.e(TAG, " Stationary Fence could not be registered: " + status);
                        }
                    }
                });

    }



private final class MyServiceHandler extends Handler {
    private static final String TAG = "MyServiceHandler";
    public MyServiceHandler(Looper looper) {
        super(looper);
    }
    @Override
    public void handleMessage(Message msg) {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                try {
                    Log.i(TAG, "MyService running...");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
                if(!isRunning){
                    break;
                }
            }
        }
        //stops the service for the start id.
        stopSelfResult(msg.arg1);
    }
}
	}

