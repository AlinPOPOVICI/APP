package com.example.alin.app1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AplicatieActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicatie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent i = getIntent();
        Aplicatie dene = (Aplicatie) i.getSerializableExtra("sampleObject");
        Data da = new Data();
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                //LatLng latlng = place.getLatLng();
                // Double latitude = place.getLatLng().latitude;
                // Double longitude = place.getLatLng().longitude;
                //  da.setLocationLatitude(latitude);
                // da.setLocationLongitude(longitude);
                // dene.setData(da);
            }
        }
    }


    public void select_location(View view) {
        builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
