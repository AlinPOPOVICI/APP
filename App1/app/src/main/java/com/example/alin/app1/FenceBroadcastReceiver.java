package com.example.alin.app1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.awareness.fence.FenceState;



public class FenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "FenceBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        Log.d(TAG, "Received a Fence Broadcast");

        if (TextUtils.equals(fenceState.getFenceKey(), AwarenessService.HEADPHONE_FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Received a FenceUpdate -  Headphones are plugged in.");
                    Toast.makeText(context, "Your headphones are plugged in",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  Headphones are NOT plugged in.");
                    Toast.makeText(context, "Your headphones are NOT plugged in",
                            Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "You are Walking",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT Walking.");
                    Toast.makeText(context, "Your are NOT Walking",
                            Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "You are RUNNING",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT RUNNING.");
                    Toast.makeText(context, "Your are NOT RUNNING",
                            Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "You are CYCLING",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT CYCLING.");
                    Toast.makeText(context, "Your are NOT CYCLING",
                            Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "You are STATIONARY",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.FALSE:
                    Log.i(TAG, "Received a FenceUpdate -  You are NOT STATIONARY.");
                    Toast.makeText(context, "Your are NOT STATIONARY",
                            Toast.LENGTH_LONG).show();
                    break;

                case FenceState.UNKNOWN:
                    Log.i(TAG, "Received a FenceUpdate -  STATIONARY fence is in an unknown state.");
                    break;
            }
        }

    }
}