package com.example.alin.app1.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
        LatLng start = new LatLng(0,-90);
        List<Data> list = mDataRepository.getAllData();
            if(list != null ) {
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getLocationLatitude() != -13 && list.get(i).getLocationLongitude() != -13){
                        if(i == 1){
                            start = new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude());
                        }
                        Log.i("MAP_SETUP ", ""+i + "   "+list.get(i).getLocationLatitude()+"   "+list.get(i).getLocationLongitude());
                        switch(list.get(i).getActivity()) {
                            case 3: //STILL
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.BLUE)
                                        .clickable(true));
                                break;
                            case 0: //IN Vehicle
                                    circle = mMap.addCircle(new CircleOptions()
                                            .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                            .radius(100)
                                            .strokeWidth(5)
                                            .strokeColor(Color.GREEN)
                                            .fillColor(Color.GRAY)
                                            .clickable(true));
                                    break;
                            case 1: //On Bicycle
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.GREEN)
                                        .clickable(true));
                                break;
                            case 2: //On Foot
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.WHITE)
                                        .clickable(true));
                                break;
                            case 4: //Unknown
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.BLACK)
                                        .clickable(true));
                                break;
                            case 5: //Tilting
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.YELLOW)
                                        .clickable(true));
                                break;
                            case 6: //??
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.TRANSPARENT)
                                        .clickable(true));
                                break;
                            case 7: //Walking
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.LTGRAY)
                                        .clickable(true));
                                break;
                            case 8: //Running
                                circle = mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                        .radius(100)
                                        .strokeWidth(5)
                                        .strokeColor(Color.GREEN)
                                        .fillColor(Color.RED)
                                        .clickable(true));
                                break;
                        }
                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(list.get(i).getLocationLatitude(), list.get(i).getLocationLongitude()))
                                .radius(100)
                                .strokeWidth(10)
                                .strokeColor(Color.GREEN)
                                .fillColor(Color.argb(128, 255, 0, 0))
                                .clickable(true));
                    }
                }
            }



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(37.422, -122.084);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        //mDataViewModel.getAllData();
        /*circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(37.422, -122.084))
                .radius(1000)
                .strokeWidth(10)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 255, 0, 0))
                .clickable(true));
*/
    }
}
