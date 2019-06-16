package com.example.android.adoptme.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.android.adoptme.R;
import com.example.android.adoptme.provider.DataBaseContract;
import com.example.android.adoptme.utils.Const;
import com.example.android.adoptme.utils.Utilities;
import com.example.android.adoptme.web.VolleySingleton;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver resolver;
    private Gson gson;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
        gson = new Gson();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, final SyncResult syncResult) {
        boolean onlyUpload = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (onlyUpload) {
            uploadData();
        }
    }

    public static void startSyncAdapter(Context context) {
        ContentResolver.setSyncAutomatically(getAccounts(context), context.getString(R.string.provider_authority), true);
    }

    @Nullable
    private static Account getAccounts(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(context.getString(R.string.app_name), context.getString(R.string.account_type));

        if (null == accountManager.getPassword(account)) {
            if (!accountManager.addAccountExplicitly(account, "", null)) {
                return null;
            }
        }

        return account;
    }

}