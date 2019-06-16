package com.example.android.adoptme.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {

    private ExpenseAuthenticator autenticator;

    @Override
    public void onCreate() {
        autenticator = new ExpenseAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return autenticator.getIBinder();
    }

}