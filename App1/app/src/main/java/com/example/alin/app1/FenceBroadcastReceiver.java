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

    }
}