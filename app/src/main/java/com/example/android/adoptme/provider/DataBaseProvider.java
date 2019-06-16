package com.example.android.adoptme.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class DataBaseProvider extends ContentProvider {

    private ContentResolver resolver;

    private DataBaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DataBaseHelper(getContext());

        resolver = getContext().getContentResolver();

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (DataBaseContract.uriMatcher.match(uri)) {
            case DataBaseContract.ALLROWS:
                return DataBaseContract.MULTIPLE_MIME;
            case DataBaseContract.SINGLE_ROW:
                return DataBaseContract.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de dato desconocido: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Validar la uri
        if (DataBaseContract.uriMatcher.match(uri) != DataBaseContract.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }

        ContentValues contentValues;

        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long rowId = db.insert(DataBaseContract.TABLE_NAME, null, contentValues);

        if (rowId > 0) {
            Uri newUri = ContentUris.withAppendedId(DataBaseContract.CONTENT_URI, rowId);
            resolver.notifyChange(newUri, null, false);
            return newUri;
        }

        throw new SQLException("Worng to inset column: " + uri);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Comparar Uri
        int match = DataBaseContract.uriMatcher.match(uri);

        Cursor cursor;

        switch (match) {
            case DataBaseContract.ALLROWS:
                cursor = db.query(DataBaseContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(resolver, DataBaseContract.CONTENT_URI);
                break;
            case DataBaseContract.SINGLE_ROW:
                long idUri = ContentUris.parseId(uri);
                cursor = db.query(DataBaseContract.TABLE_NAME, projection, DataBaseContract.Columns.COLUMN_ID + " = " + idUri, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(resolver, DataBaseContract.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no support: " + uri);
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = DataBaseContract.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case DataBaseContract.ALLROWS:
                affected = db.delete(DataBaseContract.TABLE_NAME, selection, selectionArgs);
                break;
            case DataBaseContract.SINGLE_ROW:
                long idUri = ContentUris.parseId(uri);
                affected = db.delete(DataBaseContract.TABLE_NAME, DataBaseContract.Columns.COLUMN_ID + "=" + idUri + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown element: " + uri);
        }
        return affected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (DataBaseContract.uriMatcher.match(uri)) {
            case DataBaseContract.ALLROWS:
                affected = db.update(DataBaseContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DataBaseContract.SINGLE_ROW:
                String idUri = uri.getPathSegments().get(1);
                affected = db.update(DataBaseContract.TABLE_NAME, values, DataBaseContract.Columns.COLUMN_ID + "=" + idUri + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }

}
