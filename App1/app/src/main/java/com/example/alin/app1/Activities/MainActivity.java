package com.example.alin.app1.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.Job.JobSC;
import com.example.alin.app1.R;
import com.example.alin.app1.Services.AwarenessService;

public class MainActivity extends AppCompatActivity {
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = new Intent(MainActivity.this, AwarenessService.class);
        startService(intent);
        send_broadcast();

       // Intent inten = new Intent(MainActivity.this, SnapshotService.class);
      //  startService(inten);
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
