package com.example.alin.app1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.common.api.GoogleApiClient;

public class AwarenessService extends Service {

    private static final Object Awareness ;
    private android.content.Context context;
    private GoogleApiClient client;

    public AwarenessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        client = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        client.connect();

    }
}
