package com.example.alin.app1.Activities;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.MyAdapter;
import com.example.alin.app1.R;
import com.example.alin.app1.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class AppSetupActivity extends AppCompatActivity {
   // private final PackageManager pm = getPackageManager();
    private List<Aplicatie> appList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new MyAdapter(appList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Aplicatie selected_app = appList.get(position);
                Toast.makeText(getApplicationContext(), selected_app.getName() + " is selected!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {
                Aplicatie app = appList.get(position);
                start_applicatie(view,app);
            }
        }));

        prepareMovieData();
    }
    public void start_applicatie(View view, Aplicatie app) {

        Intent i = new Intent(this , AplicatieActivity.class);
      //  i.putExtra("AObject", app);
        i.putExtra("AObject", app.getName());
        startActivity(i);
    }

    private void prepareMovieData() {

        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
           // if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
          //  {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.i("APP_SETUP " + Integer.toString(i), appName);
                Aplicatie app = new Aplicatie();
                app.setName(appName);
                appList.add(app);
          //  }
        }

        mAdapter.notifyDataSetChanged();
    }
}