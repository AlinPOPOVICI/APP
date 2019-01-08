package com.example.alin.app1;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;

// activitate care imi creaza datele necesare pentru a pune in bazada de date
//preferintele pentru o anumita aplicatie
public class AplicatieActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder;
    private TimePicker timePicker;
    private Data da;
    private Aplicatie dene;
    private AplicatieRepository mAplicatieRepository;
    private TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicatie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        dene = (Aplicatie) i.getSerializableExtra("AObject");
        da = new Data();

        mAppNameTextView = (TextView) findViewById(R.id.textViewAppName);
        mAppNameTextView.setText(dene.getTitle());
        mAppNameTextView.setTextColor(Color.RED);

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

    //create place picker
    public void select_location(View view) {
        builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void select_time(View view) {
        Calendar mcurrentTime = Calendar.getInstance();

        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Date t = new Date();
                t.setHours(selectedHour);
                t.setMinutes(selectedMinute);
                da.setTime(t);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    public void save(View view){
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        //dene.setData(da);
        //mAplicatieRepository.insert(dene);
    }
}
