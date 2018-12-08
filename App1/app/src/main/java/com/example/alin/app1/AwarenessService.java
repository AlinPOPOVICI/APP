package com.example.alin.app1;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class AwarenessService extends Service {
    private static final String TAG = "FenceAwarenessService";

    private android.content.Context context;
    private GoogleApiClient client;
    private FenceBroadcastReceiver mFenceReceiver;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private static final String MY_FENCE_RECEIVER_ACTION = "MY_FENCE_ACTION";
    public static final String HEADPHONE_FENCE_KEY = "HeadphoneFenceKey";


    public AwarenessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        client = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        client.connect();

        mFenceReceiver = new FenceBroadcastReceiver();
        registerReceiver(mFenceReceiver, new IntentFilter(MY_FENCE_RECEIVER_ACTION));

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