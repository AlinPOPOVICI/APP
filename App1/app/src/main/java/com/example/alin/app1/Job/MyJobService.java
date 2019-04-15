package com.example.alin.app1.Job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.alin.app1.Services.SnapshotService;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public class MyJobService  extends JobService {
        private static final String TAG = "SyncService";

        @Override
        public boolean onStartJob(JobParameters params) {
            Intent service = new Intent(getApplicationContext(), SnapshotService.class);
            getApplicationContext().startService(service);
            JobSC.scheduleJob(getApplicationContext()); // reschedule the job
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return true;
        }

    }