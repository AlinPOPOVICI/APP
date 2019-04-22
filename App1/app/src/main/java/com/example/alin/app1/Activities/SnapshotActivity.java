package com.example.alin.app1.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.DB.DataViewModel;
import com.example.alin.app1.R;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SnapshotActivity extends AppCompatActivity {

    //private DataViewModel myViewModel;
    private Data data;
    private DataViewModel mDataViewModel;
    private DataRepository mDataRepository;

    private static final String TAG = "SnapshotActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private GoogleApiClient mGoogleApiClient;
    private Button mSnapshotButton;
    private TextView mUserActivityTextView;
    private TextView mLocationTextView;
    private TextView mBeaconTextView;
    private TextView mPlacesTextView;
    private TextView mTimeTextView;
    private TextView mWeatherTextView;
    private TextView mHeadphonesTextView;
    private int flag_done = 0;
    private PendingResult.StatusListener mLisener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot);
        mGoogleApiClient = new GoogleApiClient.Builder(SnapshotActivity.this)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();
        mDataRepository = new DataRepository(this.getApplication());

        //myViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        mUserActivityTextView = (TextView) findViewById(R.id.userActivityTextView);
        mLocationTextView = (TextView) findViewById(R.id.locationTextView);
        mBeaconTextView = (TextView) findViewById(R.id.beaconTextView);
        mPlacesTextView = (TextView) findViewById(R.id.placesTextView);
        mTimeTextView = (TextView) findViewById(R.id.timeTextView);
        mWeatherTextView = (TextView) findViewById(R.id.weatherTextView);
        mHeadphonesTextView = (TextView) findViewById(R.id.action1TextView);
        mSnapshotButton = (Button) findViewById(R.id.snapshotButton);
        mSnapshotButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getSnapshots();
            }
        });
        mDataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
    }


    @SuppressLint("SimpleDateFormat")
    public void getSnapshots() {
        // User Activity
        data = new Data();

        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect user activity");
                            mUserActivityTextView.setText("--Could not detect user activity--");
                            mUserActivityTextView.setTextColor(Color.RED);
                            data.setActivity(-13);
                            flag_done=1;
                            return;
                        }
                        ActivityRecognitionResult arResult = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = arResult.getMostProbableActivity();
                        Log.i(TAG, probableActivity.toString());
                        mUserActivityTextView.setText(probableActivity.toString());
                        mUserActivityTextView.setTextColor(Color.GREEN);
                        data.setActivity(probableActivity.getType());
//                        Log.i("MAP_SETUP_DATA_A",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getHeadphoneState());


                    }
                });


        // Headphones
        //PendingResult.StatusListener mLisener
        Awareness.SnapshotApi.getHeadphoneState(mGoogleApiClient)
                .setResultCallback(new ResultCallback<HeadphoneStateResult>() {
                    @Override
                    public void onResult(@NonNull HeadphoneStateResult headphoneStateResult) {
                        if (!headphoneStateResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not detect headphone state");
                            mHeadphonesTextView.setText("Could not detect headphone state");
                            mHeadphonesTextView.setTextColor(Color.RED);
                            data.setHeadphoneState(-13);
                            return;
                        }

                        HeadphoneState headphoneState = headphoneStateResult.getHeadphoneState();
                        data.setHeadphoneState(headphoneState.getState());

//                        Log.i("MAP_SETUP_DATA_HP",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getHeadphoneState());

                        if (headphoneState.getState() == HeadphoneState.PLUGGED_IN) {
                            mHeadphonesTextView.setText("Headphones plugged in");
                            mHeadphonesTextView.setTextColor(Color.GREEN);
                        } else {

                            mHeadphonesTextView.setText("Headphones NOT plugged in");
                            mHeadphonesTextView.setTextColor(Color.BLACK);
                        }
                    }
                });



        // Time (Simply get device time)
      Calendar calendar = Calendar.getInstance();
     mTimeTextView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime()));
       mTimeTextView.setTextColor(Color.GREEN);
     // data.setTime(calendar.getTime());
        getFineLocationSnapshots();
        //mDataViewModel.insert(data);

        //Log.i("MAP_SETUP_DATA_009",    data.getTime().toString()+"    "+data.getLocationLatitude()+"   "+data.getLocationLatitude()+"  "+ data.getActivity()+"   "+data.getWeatherCelsius());


    }

    private void getFineLocationSnapshots() {
        // Check for permission first
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.i(TAG, "Fine Location permission already granted");

            // Location
            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {
                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not detect user location");
                                mLocationTextView.setText("Could not detect user location");
                                data.setLocationLatitude(-13);
                                data.setLocationLongitude(-13);
                                mLocationTextView.setTextColor(Color.RED);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            mLocationTextView.setText(location.toString());
                            mLocationTextView.setTextColor(Color.GREEN);
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
                                mPlacesTextView.setText("Could not get places list");
                                mPlacesTextView.setTextColor(Color.RED);
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
                                mPlacesTextView.setText(places.toString());
                            } else {
                                Log.e(TAG, "There is no known place");
                                mPlacesTextView.setText("There is no known place");
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
                                mWeatherTextView.setText("Could not detect weather info");
                                mWeatherTextView.setTextColor(Color.RED);
                                data.setWeatherCelsius(-13);
                                data.setWeatherCondition(-13);
                                return;
                            }

                            Weather weather = weatherResult.getWeather();
                            if(weather != null) {
                                mWeatherTextView.setText(weather.toString());
                                data.setWeatherCelsius(weatherResult.getWeather().getTemperature(2));
                                data.setWeatherCondition(weatherResult.getWeather().getConditions()[0]);

                            }else{
                                mWeatherTextView.setText("Could not detect weather info");
                                mWeatherTextView.setTextColor(Color.RED);
                                data.setWeatherCelsius(-13);
                                data.setWeatherCondition(-13);
                            }
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
                                mBeaconTextView.setText("Could not get beacon state");
                                mBeaconTextView.setTextColor(Color.RED);
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
                                    mBeaconTextView.setText(beaconString.toString());
                                } else {
                                    mBeaconTextView.setText("There are no beacons available");
                                }
                            }
                            else {
                                mBeaconTextView.setText("The beacon state is empty");
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFineLocationSnapshots();
                } else {
                    // Do nothing
                }
                return;
            }
        }
    }

}