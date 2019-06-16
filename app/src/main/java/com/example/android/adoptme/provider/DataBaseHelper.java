package com.example.android.adoptme.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    // --------------------------------------------------
    // Constants
    // --------------------------------------------------
    private static final String DATABASE_NAME = "DBSETA.SQLITE";

    private static final int DATABASE_VERSION = 1;

    // --------------------------------------------------
    // Constructors
    // --------------------------------------------------

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // --------------------------------------------------
    // Methods
    // --------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE " + DataBaseContract.TABLE_NAME);
        } catch (SQLiteException e) {

        }
        onCreate(db);
    }

}
