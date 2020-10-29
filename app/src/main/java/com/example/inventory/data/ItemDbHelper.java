package com.example.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.inventory.data.ItemContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = ItemDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_STATEMENT = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME
                + " ("
                + ItemEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_PRICE+" INTEGER NOT NULL DEFAULT 0, "
                + ItemEntry.COLUMN_ITEM_IMAGE + " BLOB , "
                + ItemEntry.COLUMN_ITEM_STOCK + " INTEGER NOT NULL DEFAULT 0, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_MAIL + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_DETAIL + " TEXT );";
        db.execSQL(SQL_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(LOG_TAG,"Updating Database");
    }
}
