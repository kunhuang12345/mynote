package com.hk.mynote.adapter;

import android.view.View;
import android.widget.TextView;

import com.hk.mynote.R;

// ViewHolder用于给item的视图加载数据内容
public class ViewHolder {
    private TextView t_content, t_time;
    public ViewHolder(View view) {
        t_content = view.findViewById(R.id.item_content);
        t_time = view.findViewById(R.id.item_time);
    }

    public ViewHolder() {
    }

    public TextView getT_content() {
        return t_content;
    }

    public void setT_content(TextView t_content) {
        this.t_content = t_content;
    }

    public TextView getT_time() {
        return t_time;
    }

    public void setT_time(TextView t_time) {
        this.t_time = t_time;
    }
}
