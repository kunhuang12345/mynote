package com.hk.mynote.adapter;

import android.view.View;
import android.widget.TextView;

import com.hk.mynote.R;

public class MenuHolder {

    private TextView item;

    public TextView getItem() {
        return item;
    }

    public void setItem(TextView item) {
        this.item = item;
    }

    public MenuHolder() {
    }

    public MenuHolder(View view) {
        this.item = view.findViewById(R.id.menu_item);
    }
}
