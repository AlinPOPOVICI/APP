package com.example.alin.app1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.alin.app1.Services.AwarenessService;
import com.example.alin.app1.Services.SnapshotService;
import com.google.android.gms.awareness.fence.FenceState;

import java.util.Calendar;

/*
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, ProximityAlertService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Log.d("Main",String.valueOf( cal.getTimeInMillis()));
        //make the alarm goes off every 10 sec (not exact help to save battery life)
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10000, pintent);
*/

public class FenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "FenceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        Log.d(TAG, "Received a Fence Broadcast");
        String action = intent.getAction();
      // if (true){//Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
     //       Intent intent1 = new Intent(context.getApplicationContext(), DummyService.class);
     //       context.startForegroundService(intent1);
      //  }

        if(Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
            Log.i(TAG, "Received a FenceUpdate -  Boot ");
            start_alarm(context, 40000);
        }

        if(SnapshotService.CUSTOM_BROADCAST_ACTION.equals(action)) {
            Log.i(TAG, "Received a FenceUpdate -  Custom ");
            stop_alarm(context);
            start_alarm(context, 40000);
        }

            if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.HEADPHONE_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  Headphones are plugged in.");
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  Headphones are NOT plugged in.");
                    start_alarm(context,60000);
                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate -  The headphone fence is in an unknown state.");
                    break;
            }
        }

        if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.WALKING_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  You are Walking.");

                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT Walking.");

                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate -  Walking fence is in an unknown state.");
                    break;
            }
        }

        if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.RUNNING_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  You are RUNNING.");

                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT RUNNING.");

                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate -  RUNNING fence is in an unknown state.");
                    break;
            }
        }

        if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.CYCLING_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  You are CYCLING.");
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT CYCLING.");

                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate - CYCLING fence is in an unknown state.");
                    break;
            }
        }

        if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.STATIONARY_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  You are STATIONARY.");
                   // start_alarm(context,300);

                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT STATIONARY.");

                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate -  STATIONARY fence is in an unknown state.");
                    break;
            }
        }


    }
    private void start_alarm(Context context, int t){
        Log.i(TAG, "Start_alarm.");
       // Intent schedule_intent = new Intent(context, SnapshotService.class);
       // context.startService(schedule_intent);
        Calendar cal = Calendar.getInstance();
        Intent schedule_intent = new Intent(context, SnapshotService.class);
        PendingIntent pintent = PendingIntent.getService(context, 1, schedule_intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), t, pintent);
       // Toast.makeText(context, "Getting data", Toast.LENGTH_LONG).show();

    }
    private void stop_alarm(Context context)
    {
        Log.i(TAG, "Stop_alarm.");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, SnapshotService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, myIntent,0);
        alarmManager.cancel(pendingIntent);
    }
}