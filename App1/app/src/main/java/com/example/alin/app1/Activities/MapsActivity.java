package com.example.alin.app1.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.DB.DataViewModel;
import com.example.alin.app1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Circle circle;
    private DataViewModel mDataViewModel;
    private DataRepository mDataRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mDataRepository = new DataRepository(this.getApplication());
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Data> list = mDataRepository.getAllData();
            if(list != null ) {
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getLocationLatitude() != -13 && list.get(i).getLocationLongitude() != -13){
                        Log.i("MAP_SETUP ", ""+i + "   "+list.get(i).getLocationLatitude()+"   "+list.get(i).getLocationLongitude());
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                .radius(1000)
                                .strokeWidth(10)
                                .strokeColor(Color.GREEN)
                                .fillColor(Color.argb(128, 255, 0, 0))
                                .clickable(true));
                    }
                }
            }



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(37.422, -122.084);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mDataViewModel.getAllData();
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(37.422, -122.084))
                .radius(1000)
                .strokeWidth(10)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 255, 0, 0))
                .clickable(true));

    }
}
