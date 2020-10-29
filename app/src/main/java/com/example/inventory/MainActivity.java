package com.example.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.inventory.data.ItemContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    FloatingActionButton mFab;
    private static final int LOADER_ID = 1;
    private ItemCursorAdapter itemCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        ListView listView = findViewById(R.id.list_view);
        itemCursorAdapter = new ItemCursorAdapter(this, null, true);
        listView.setAdapter(itemCursorAdapter);

        //set empty view
        TextView emptyView = findViewById(R.id.text_empty_view);
        listView.setEmptyView(emptyView);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Button saleButton = view.findViewById(R.id.button_sale);
                saleButton.setFocusable(true);
                TextView textStock = view.findViewById(R.id.text_stock);
                int stock = Integer.parseInt(textStock.getText().toString());
                saleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Button Sale Click",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "list item click", Toast.LENGTH_SHORT).show();
                Intent openDetail = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(openDetail);
            }
        });
        //add new Item
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewItem = new Intent(MainActivity.this, EditActivity.class);
                startActivity(addNewItem);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_sample_data:
                insertSampleData();
                return true;
            case R.id.delete_all:
                deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        int deletedRowCounts = getContentResolver().delete(ItemContract.ItemEntry.CONTENT_URI, null, null);
        Toast.makeText(this, "deleted items amount " + deletedRowCounts, Toast.LENGTH_SHORT).show();
    }

    private void insertSampleData() {
        //changing img from res to byteArray
        Bitmap bitmapImg = BitmapFactory.decodeResource(getResources(), R.drawable.inventory_, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, "Sample");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, 1000);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, imgByte);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_STOCK, 10);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Kyaw Ye Htut");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "09781133155");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPPLIER_MAIL, "kyawhtut342@gmail.com");
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_DETAIL, "In stock , waiting time 2 weeks");

        Uri insertUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
        int newId = (int) ContentUris.parseId(insertUri);
        if (newId == -1) {
            Toast.makeText(this, "Sample Data Insert Failed at " + newId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sample Data Inserted with id " + newId, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_ID) {
            String[] projection = {ItemContract.ItemEntry.COLUMN_ID
                    , ItemContract.ItemEntry.COLUMN_ITEM_NAME
                    , ItemContract.ItemEntry.COLUMN_ITEM_PRICE
                    , ItemContract.ItemEntry.COLUMN_ITEM_STOCK};
            return new CursorLoader(this, ItemContract.ItemEntry.CONTENT_URI, projection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        itemCursorAdapter.swapCursor(data);
        Toast.makeText(this, "Loading in Background Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        itemCursorAdapter.swapCursor(null);
    }
}