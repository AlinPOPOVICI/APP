package com.example.alin.app1;

import android.Manifest;
import android.app.Service;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.BeaconStateResult;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class SnapshotService extends Service {
    private Data data;
    private DataRepository mDataRepository;

    private static final String TAG = "SnapshotService";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private GoogleApiClient mGoogleApiClient;
    private boolean isRunning  = false;
    private Looper looper;
    private MyServiceHandler myServiceHandler;

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
        mGoogleApiClient = new GoogleApiClient.Builder(SnapshotService.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        //getSnapshots();
        mDataRepository = new DataRepository(this.getApplication());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        getSnapshots();
        return START_NOT_STICKY;
    }



    public void getSnapshots() {
        // User Activity
        data = new Data();
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect user activity");
                            data.setActivity(-13);
                            return;
                        }
                        ActivityRecognitionResult arResult = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = arResult.getMostProbableActivity();
                        Log.i(TAG, probableActivity.toString());
                        data.setActivity(probableActivity.getType());
                    }
                });

        // Headphones
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        if (!headphoneStateResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect headphone state");
                            data.setHeadphoneState(-13);
                            return;
                        }
                        HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                        data.setHeadphoneState(headphoneState.getState());
                    }
                });

        // Time (Simply get device time)
        Calendar calendar = Calendar.getInstance();
        data.setTime(calendar.getTime());
        getFineLocationSnapshots();
        mDataRepository.insert(data);

    }

    private void getFineLocationSnapshots() {
        // Check for permission first
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
        } else {
            Log.i(TAG, "Fine Location permission already granted");

            // Location
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not detect user location");
                                data.setLocationLatitude(-13);
                                data.setLocationLongitude(-13);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            data.setLocationLatitude(location.getLatitude());
                            data.setLocationLongitude(location.getLongitude());
                        }
                    });


            // Places
            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (!placesResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get places list");
                                return;
                            }

                            List<PlaceLikelihood> placeLikelihoods = placesResult.getPlaceLikelihoods();
                            if (placeLikelihoods != null) {
                                StringBuilder places = new StringBuilder();
                                for (PlaceLikelihood place :
                                        placeLikelihoods) {
                                    Log.i(TAG, place.getPlace().getName().toString() +
                                            "[likelihood = " + place.getLikelihood() + "]");
                                    places.append(place.getPlace().getName().toString() +
                                            "[likelihood = " + place.getLikelihood() + "]\n");
                                }
                            } else {
                                Log.e(TAG, "There is no known place");
                            }
                        }
                    });



            // Weather
            Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<WeatherResult>() {
                        @Override
                        public void onResult(@NonNull WeatherResult weatherResult) {
                            if (!weatherResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not detect weather info");
                                data.setWeatherCelsius(-13);
                                data.setWeatherCondition(-13);
                                return;
                            }
                            Weather weather = weatherResult.getWeather();
                            data.setWeatherCelsius(weatherResult.getWeather().getTemperature(2));
                            data.setWeatherCondition(weatherResult.getWeather().getConditions()[0]);
                        }
                    });

            //Beacon
            List MY_BEACON_TYPE_FILTERS = Arrays.asList(
                    BeaconState.TypeFilter.with(
                            "my.beacon.com.sample.usingawarenessapi",
                            "my-attachment-type"),
                    BeaconState.TypeFilter.with(
                            "com.androidauthority.awareness",
                            "my-attachment-type"));
            Awareness.SnapshotApi.getBeaconState(mGoogleApiClient, MY_BEACON_TYPE_FILTERS)
                    .setResultCallback(new ResultCallback<BeaconStateResult>() {

                        @Override
                        public void onResult(@NonNull BeaconStateResult beaconStateResult) {
                            if (!beaconStateResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get beacon state");
                                return;
                            }
                            BeaconState beaconState = beaconStateResult.getBeaconState();
                            if(beaconState != null) {
                                List<BeaconState.BeaconInfo> beacons = beaconState.getBeaconInfo();
                                if (beacons != null) {
                                    StringBuilder beaconString = new StringBuilder();
                                    for (BeaconState.BeaconInfo info :
                                            beacons) {
                                        beaconString.append(info.toString());
                                    }
                                }
                            }
                        }
                    });
        }
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

