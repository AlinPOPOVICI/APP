package com.example.alin.app1.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alin.app1.DB.AplicatieData;
import com.example.alin.app1.DB.AplicatieDataRepository;
import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.R;
import com.example.alin.app1.Widget.WidgetProvider;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;

// activitate care imi creaza datele necesare pentru a pune in bazada de date
//preferintele pentru o anumita aplicatie
public class AplicatieActivity extends AppCompatActivity {

    private static final String TAG = "AplicatieActivity";
    private static final int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder;
    private TimePickerDialog mTimePicker;
    private Data da;
    private AplicatieData dene;
    private AplicatieDataRepository mAplicatieRepository = new AplicatieDataRepository(this.getApplication());
    private DataRepository mDataRepository;
    private TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicatie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        //get app name not Aplicatie obj.
        dene = new AplicatieData();
        dene.setName((String) i.getSerializableExtra("AObject"));
        da = new Data();

        mAppNameTextView = (TextView) findViewById(R.id.textViewAppName);
        mAppNameTextView.setText(dene.getName());
        mAppNameTextView.setTextColor(Color.BLACK);

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
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                dene.setLocationLatitude(latitude);
                dene.setLocationLongitude(longitude);
                Log.e(TAG, "LAT:"+dene.getLocationLatitude()+"\n");
                Log.e(TAG, "LON:"+dene.getLocationLongitude()+"\n");
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
        //TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Date t = new Date();
                t.setHours(selectedHour);
                t.setMinutes(selectedMinute);
                 //da.setTime(t);
                dene.setTime(t);
                Log.i(TAG, "Time:"+dene.getTime().toString()+"\n");
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
//        Log.i(TAG, "Time:"+dene.getTime().toString()+"\n");

    }
    public void save(View view){
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        mAplicatieRepository.insert(dene);
        WidgetProvider.sendRefreshBroadcast(getApplicationContext());
        //mDataRepository.insert(dene);
    }
}



