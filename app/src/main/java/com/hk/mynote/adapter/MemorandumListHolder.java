package com.hk.mynote.adapter;

import android.view.View;
import android.widget.TextView;

import com.hk.mynote.R;

public class MemorandumListHolder {
    private TextView itemClock;
    private TextView itemRemark;

    public MemorandumListHolder(View view) {
        itemClock = view.findViewById(R.id.item_clock);
        itemRemark = view.findViewById(R.id.item_remark);
    }

    public TextView getItemClock() {
        return itemClock;
    }

    public void setItemClock(TextView itemClock) {
        this.itemClock = itemClock;
    }

    public TextView getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(TextView itemRemark) {
        this.itemRemark = itemRemark;
    }
}
