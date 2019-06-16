package com.example.android.adoptme.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static SyncAdapter syncAdapter = null;
    private static final Object lock = new Object();

    public SyncService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (syncAdapter == null) {
            syncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

}