package com.example.alin.app1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.DB.Data;
import com.example.alin.app1.DB.DataRepository;
import com.example.alin.app1.DB.MyDataAdapter;
import com.example.alin.app1.R;
import com.example.alin.app1.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDataActivity extends AppCompatActivity {
    // private final PackageManager pm = getPackageManager();
    private List<String> strList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyDataAdapter mAdapter;
    private DataRepository mDataRepository ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dataactivity);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        this.mDataRepository = new DataRepository(this.getApplication());

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new MyDataAdapter(strList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               // Data selected_app = dataList.get(position);
               // Toast.makeText(getApplicationContext(), selected_app.get + " is selected!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {
               // Aplicatie app = appList.get(position);
               // start_applicatie(view,app);
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

        List<Data> dataList = (List<Data>) mDataRepository.getAllData();
        if(dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                String appTime = dataList.get(i).getTime().toString();
                Log.i("APP_SETUP " + Integer.toString(i), appTime);

                strList.add(appTime);

            }
        }else{
            strList.add("NULL");
        }

        mAdapter.notifyDataSetChanged();
    }
}