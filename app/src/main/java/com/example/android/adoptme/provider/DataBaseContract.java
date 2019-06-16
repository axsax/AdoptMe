package com.example.android.adoptme.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataBaseContract {

    // --------------------------------------------------
    // Constants
    // --------------------------------------------------
    public final static String AUTHORITY = "com.example.android";

    public static final String TABLE_NAME = "SERVICES";

    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    public final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY + TABLE_NAME;

    public final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLE_NAME;

    public static final UriMatcher uriMatcher;

    public static final int ALLROWS = 1;

    public static final int SINGLE_ROW = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, ALLROWS);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", SINGLE_ROW);
    }

    public static final int STATUS_SYNC = 0;
    public static final int STATUS_PEND = 1;

}
