package com.example.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {
    private ItemContract() {
    }

    public static final class ItemEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.inventory";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String ITEMS_PATH = "items";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, ITEMS_PATH);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + ITEMS_PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + ITEMS_PATH;

        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_NAME = "Product_Name";
        public static final String COLUMN_ITEM_PRICE = "Product_Price";
        public static final String COLUMN_ITEM_IMAGE = "Image";//type must be Blob---byte
        public static final String COLUMN_ITEM_STOCK = "Stock";
        public static final String COLUMN_ITEM_SUPPLIER_NAME = "Supplier_Name";
        public static final String COLUMN_ITEM_SUPPLIER_PHONE = "Phone";
        public static final String COLUMN_ITEM_SUPPLIER_MAIL = "Mail";
        public static final String COLUMN_ITEM_DETAIL = "Detail";

    }


}