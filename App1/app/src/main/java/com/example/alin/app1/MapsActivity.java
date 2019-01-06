package com.example.alin.app1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Data> list = (List<Data>) mDataViewModel.getAllData();
            if(list != null ) {
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getLocationLatitude() != -13 && list.get(i).getLocationLongitude() != -13){
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
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //mDataViewModel.getAllData();
       /* circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(37.422, -122.084))
                .radius(1000)
                .strokeWidth(10)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 255, 0, 0))
                .clickable(true));*/

    }
}
