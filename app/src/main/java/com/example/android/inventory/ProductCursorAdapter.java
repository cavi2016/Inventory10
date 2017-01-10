package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.example.android.inventory.data.InventoryContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView salesTextView = (TextView) view.findViewById(R.id.sales);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        Button saleButton = (Button) view.findViewById(R.id.sale_button);

        final String idColumn = cursor.getString(cursor.getColumnIndexOrThrow(
                ProductEntry._ID));
        final String nameColumn = cursor.getString(cursor.getColumnIndexOrThrow(
                ProductEntry.COLUMN_PRODUCT_NAME));
        int salesColumn = cursor.getInt(cursor.getColumnIndexOrThrow(
                ProductEntry.COLUMN_PRODUCT_SALES));
        int quantityColumn = cursor.getInt(cursor.getColumnIndexOrThrow(
                ProductEntry.COLUMN_PRODUCT_QUANTITY));
        final double priceColumn = cursor.getDouble(cursor.getColumnIndexOrThrow(
                ProductEntry.COLUMN_PRODUCT_PRICE));
        final String photoColumn = cursor.getString(cursor.getColumnIndexOrThrow(
                ProductEntry.COLUMN_PRODUCT_PHOTO));

        nameTextView.setText(nameColumn);
        salesTextView.setText(Integer.toString(salesColumn));
        quantityTextView.setText(Integer.toString(quantityColumn));
        priceTextView.setText(Double.toString(priceColumn));
        final Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Long.parseLong(idColumn));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sales;
                if (salesTextView.getText().toString().isEmpty()) {
                    sales = 0;
                } else {
                    sales = Integer.parseInt(salesTextView.getText().toString());
                }

                int quantity;
                if (quantityTextView.getText().toString().isEmpty()) {
                    quantity = 0;
                } else {
                    quantity = Integer.parseInt(quantityTextView.getText().toString());
                }
                if (quantity > 0) {
                    sales = sales + 1;
                    quantity = quantity - 1;
                    salesTextView.setText(String.valueOf(sales));
                    quantityTextView.setText(String.valueOf(quantity));

                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_NAME, nameColumn);
                    values.put(ProductEntry.COLUMN_PRODUCT_SALES, sales);
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, priceColumn);
                    values.put(ProductEntry.COLUMN_PRODUCT_PHOTO, photoColumn);

                    int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.error_updating_product), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), "Sale Product " + nameColumn, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Order Product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}