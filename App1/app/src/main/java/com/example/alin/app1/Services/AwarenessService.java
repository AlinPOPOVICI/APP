package com.example.alin.app1.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.os.Process;
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

        //mFenceReceiver = new FenceBroadcastReceiver();
       // registerReceiver(mFenceReceiver, new IntentFilter(MY_FENCE_RECEIVER_ACTION));
        addHeadphoneFence();
        addCyclingFence();
        addRunningFence();
        addStationaryFence();
        addWalkingFence();

    }
    @Override
    public void onDestroy() {
      //  unregisterReceiver(mFenceReceiver);
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
                            Log.i(TAG, "Fence was successfully registered.");
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
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

/*

public class FenceActivity extends AppCompatActivity {

    private static final String TAG = "FenceActivity";



    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private static final String MY_FENCE_RECEIVER_ACTION = "MY_FENCE_ACTION";
    public static final String HEADPHONE_FENCE_KEY = "HeadphoneFenceKey";
    public static final String HEADPHONE_AND_WALKING_FENCE_KEY = "HeadphoneAndLocationFenceKey";
    public static final String HEADPHONE_OR_WALKING_FENCE_KEY = "HeadphoneOrLocationFenceKey";


    private GoogleApiClient mGoogleApiClient;
    private FenceBroadcastReceiver mFenceReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(FenceActivity.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        mFenceReceiver = new FenceBroadcastReceiver();

    }



    @Override
    protected void onStart() {
        super.onStart();

        // We want to receive Broadcasts when activity is paused
        registerReceiver(mFenceReceiver, new IntentFilter(MY_FENCE_RECEIVER_ACTION));

    }



    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mFenceReceiver);
    }


    private void addHeadphoneFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);

        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(FenceActivity.this,

                10001,

                intent,

                0);



        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);

        Awareness.FenceApi.updateFences(

                mGoogleApiClient,

                new FenceUpdateRequest.Builder()

                        .addFence(HEADPHONE_FENCE_KEY, headphoneFence, mFencePendingIntent)

                        .build())

                .setResultCallback(new ResultCallback<Status>() {

                    @Override

                    public void onResult(@NonNull Status status) {

                        if (status.isSuccess()) {

                            Log.i(TAG, "Fence was successfully registered.");

                        } else {

                            Log.e(TAG, "Fence could not be registered: " + status);

                        }

                    }

                });

    }



    private void addHeadphoneAndLocationFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);

        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(FenceActivity.this,

                10001,

                intent,

                0);



        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);

        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);

        AwarenessFence jointFence = AwarenessFence.and(headphoneFence, activityFence);

        Awareness.FenceApi.updateFences(

                mGoogleApiClient,

                new FenceUpdateRequest.Builder()

                        .addFence(HEADPHONE_AND_WALKING_FENCE_KEY,

                                jointFence, mFencePendingIntent)

                        .build())

                .setResultCallback(new ResultCallback<Status>() {

                    @Override

                    public void onResult(@NonNull Status status) {

                        if (status.isSuccess()) {

                            Log.i(TAG, "Headphones AND Walking Fence was successfully registered.");

                        } else {

                            Log.e(TAG, "Headphones AND Walking Fence could not be registered: " + status);

                        }

                    }

                });

    }



    private void addHeadphoneOrLocationFence() {

        Intent intent = new Intent(MY_FENCE_RECEIVER_ACTION);

        PendingIntent mFencePendingIntent = PendingIntent.getBroadcast(FenceActivity.this,

                10001,

                intent,

                0);



        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);

        AwarenessFence activityFence = DetectedActivityFence.during(DetectedActivityFence.WALKING);



        AwarenessFence orFence = AwarenessFence.or(headphoneFence, activityFence);

        Awareness.FenceApi.updateFences(

                mGoogleApiClient,

                new FenceUpdateRequest.Builder()

                        .addFence(HEADPHONE_OR_WALKING_FENCE_KEY,

                                orFence, mFencePendingIntent)

                        .build())

                .setResultCallback(new ResultCallback<Status>() {

                    @Override

                    public void onResult(@NonNull Status status) {

                        if (status.isSuccess()) {

                            Log.i(TAG, "Headphones OR Walking Fence was successfully registered.");

                        } else {

                            Log.e(TAG, "Headphones OR Walking Fence could not be registered: " + status);

                        }

                    }

                });

    }



    private void removeFence(final String fenceKey) {

        Awareness.FenceApi.updateFences(

                mGoogleApiClient,

                new FenceUpdateRequest.Builder()

                        .removeFence(fenceKey)

                        .build()).setResultCallback(new ResultCallbacks<Status>() {

            @Override

            public void onSuccess(@NonNull Status status) {

                String info = "Fence " + fenceKey + " successfully removed.";

                Log.i(TAG, info);

                Toast.makeText(FenceActivity.this, info, Toast.LENGTH_LONG).show();

            }



            @Override

            public void onFailure(@NonNull Status status) {

                String info = "Fence " + fenceKey + " could NOT be removed.";

                Log.i(TAG, info);

                Toast.makeText(FenceActivity.this, info, Toast.LENGTH_LONG).show();

            }

        });

    }

}
*/