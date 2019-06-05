package com.example.alin.app1.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.Job.JobSC;
import com.example.alin.app1.R;
import com.example.alin.app1.Services.AwarenessService;
import com.example.alin.app1.Services.ToCSVService;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10001;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE= 10002;
    private static final String TAG = "Main";
    private static final String CUSTOM_BROADCAST_ACTION = "CUSTOM_BROADCAST_ACTION";
    // private GoogleApiClient client;
   private DataRepository mDataRepository ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*client = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        client.connect();*/


        this.mDataRepository = new DataRepository(this.getApplication());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = new Intent(MainActivity.this, AwarenessService.class);
        permit();
        startService(intent);
        send_broadcast();

       // Intent inten = new Intent(MainActivity.this, SnapshotService.class);
      //  startService(inten);
    }

    private void permit(){
        //Location
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Fine Location Permission not yet granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

    }

    public void data_file(View view) {
        Intent intent = new Intent(this, ToCSVService.class);
        startService(intent);
    }

    public void permission_setting(View view) {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    private void send_broadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobSC.scheduleJob(this.getApplicationContext());
        }
    }

    public void start_Maps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void start_View_data(View view) {
        Intent intent = new Intent(this, ViewDataActivity.class);
        startActivity(intent);
    }

    public void start_AppSetup(View view) {
        Intent intent = new Intent(this, AppSetupActivity.class);
        startActivity(intent);
    }

    public void start_Snapshot(View view) {
        Intent intent = new Intent(this, SnapshotActivity.class);
        startActivity(intent);
    }

    public void delete_Data(View view) {
       mDataRepository.deleteAll();
    }

}
