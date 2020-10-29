package com.example.inventory;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.inventory.data.ItemContract;

class ItemCursorAdapter extends CursorAdapter {
    TextView mTextName, mTextPrice, mTextStock;
    Button mButtonSale;
    Context mContext;
    Cursor myCursor;


    public ItemCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        this.myCursor = cursor;
        int mStock;
        mTextName = view.findViewById(R.id.text_product_name);
        mTextPrice = view.findViewById(R.id.text_price);
        mTextStock = view.findViewById(R.id.text_stock);
        mButtonSale = view.findViewById(R.id.button_sale);

        String name = myCursor.getString(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
        int price = myCursor.getInt(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE));
        mStock = myCursor.getInt(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_STOCK));

        mTextName.setText(name);
        mTextPrice.setText(String.valueOf(price));
        mTextStock.setText(String.valueOf(mStock));
    }

   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
        }

        myCursor = getCursor();
        if (myCursor == null) {
            Log.v("cursor", "Null");
        }
        mTextName = rootView.findViewById(R.id.text_product_name);
        mTextPrice = rootView.findViewById(R.id.text_price);
        mTextStock = rootView.findViewById(R.id.text_stock);
        mButtonSale = rootView.findViewById(R.id.button_sale);

        if (myCursor.moveToFirst()) {
            String name = myCursor.getString(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME));
            int price = myCursor.getInt(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE));
            int mStock = myCursor.getInt(myCursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_STOCK));

            mTextName.setText(name);
            mTextPrice.setText(String.valueOf(price));
            mTextStock.setText(String.valueOf(mStock));
        }
        mButtonSale = rootView.findViewById(R.id.button_sale);
        mButtonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextStock.setText("9");
                Toast.makeText(mContext, "Sale Button click", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }*/
}
