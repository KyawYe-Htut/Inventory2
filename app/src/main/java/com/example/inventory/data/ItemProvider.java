package com.example.inventory.data;

import android.app.slice.Slice;
import android.content.ClipData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import com.example.inventory.data.ItemContract.ItemEntry;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.Objects;

public class ItemProvider extends ContentProvider {
    private static final String LOG_TAG = ItemProvider.class.getSimpleName();
    private ItemDbHelper mItemDbHelper;
    public static final int ITEMS = 100;
    public static final int ITEMS_ID = 101;

    public static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ItemEntry.CONTENT_AUTHORITY, ItemEntry.ITEMS_PATH, ITEMS);
        sUriMatcher.addURI(ItemEntry.CONTENT_AUTHORITY, ItemEntry.ITEMS_PATH + "#", ITEMS_ID);
    }

    @Override
    public boolean onCreate() {
        mItemDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = mItemDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = sqLiteDatabase.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEMS_ID:
                selection = ItemEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri");

        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid or unknown Uri for getting type");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sUriMatcher.match(uri);
        if (match == ITEMS) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
            assert values != null;
            return insertItems(uri, values);
        }
        throw new IllegalArgumentException("invalid Uri to Insert");

    }

    private Uri insertItems(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = mItemDbHelper.getWritableDatabase();
        //data validation
        String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name can't be null");
        }
        byte[] imageByte = values.getAsByteArray(ItemEntry.COLUMN_ITEM_IMAGE);
        if (imageByte == null) {
            Toast.makeText(getContext(),"You should insert an Image",Toast.LENGTH_SHORT).show();
        }
        Integer price = values.getAsInteger(ItemEntry.COLUMN_ITEM_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("price can't be null");
        }
        Integer stock = values.getAsInteger(ItemEntry.COLUMN_ITEM_STOCK);
        if (stock == null) {
            throw new IllegalArgumentException("stock can't be null");
        }
        String supplier = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME);
        if (supplier == null) {
            throw new IllegalArgumentException("supplier can't be null");
        }
        String supplierPhone = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("supplier phone can't be null");
        }
        String supplierMail = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER_MAIL);
        if (supplierMail == null) {
            throw new IllegalArgumentException("mail can't be null");
        }
        String detail = values.getAsString(ItemEntry.COLUMN_ITEM_DETAIL);
        if (detail == null) {
            detail = "";
        }
        //here using nullColumnHack for null imageByte
        long return_ID = sqLiteDatabase.insert(ItemEntry.TABLE_NAME, ItemEntry.COLUMN_ITEM_IMAGE, values);
        if (return_ID == -1) {
            Log.e(LOG_TAG, "This is Invalid Uri" + uri);
        }
        return ContentUris.withAppendedId(uri, return_ID);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowDeleted;
        SQLiteDatabase sqLiteDatabase = mItemDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                rowDeleted = sqLiteDatabase.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                selection = ItemEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = sqLiteDatabase.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri At Deleting" + uri);
        }
        if (rowDeleted == 0) {
            Toast.makeText(getContext(), "Delete Fail , 0 item deleted", Toast.LENGTH_SHORT).show();
        } else {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                assert values != null;
                return updateItem(uri, values, selection, selectionArgs);
            case ITEMS_ID:
                selection = ItemEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                assert values != null;
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri To Update");
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mItemDbHelper.getWritableDatabase();

        //data validation

        assert values != null;
        if (values.containsKey(ItemEntry.COLUMN_ITEM_NAME)) {
            String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("name can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_PRICE)) {
            Integer price = values.getAsInteger(ItemEntry.COLUMN_ITEM_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("price can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_IMAGE)) {
            byte[] image = values.getAsByteArray(ItemEntry.COLUMN_ITEM_IMAGE);
            if (image == null) {
                throw new IllegalArgumentException("image can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_STOCK)) {
            Integer stock = values.getAsInteger(ItemEntry.COLUMN_ITEM_STOCK);
            if (stock == null) {
                throw new IllegalArgumentException("stock can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME)) {
            String supplier = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME);
            if (supplier == null) {
                throw new IllegalArgumentException("supplier can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE)) {
            Integer supplierPhone = values.getAsInteger(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("supplier phone can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_SUPPLIER_MAIL)) {
            String supplierMail = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER_MAIL);
            if (supplierMail == null) {
                throw new IllegalArgumentException("mail can't be null");
            }
        }
        if (values.containsKey(ItemEntry.COLUMN_ITEM_DETAIL)) {
            String detail = values.getAsString(ItemEntry.COLUMN_ITEM_DETAIL);
            if (detail == null) {
                detail = "";
            }
        }
        int updateRow = sqLiteDatabase.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        if (updateRow == 0) {
            Toast.makeText(getContext(), "Update Fail , 0 item updated", Toast.LENGTH_SHORT).show();
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return updateRow;
    }
}
