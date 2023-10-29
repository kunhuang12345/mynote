package com.hk.mynote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hk.mynote.R;

public class MenuAdapter extends BaseAdapter {

    private String[] menus;
    private LayoutInflater layoutInflater;

    public MenuAdapter(String[] menus, Context context) {
        this.menus = menus;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menus.length;
    }

    @Override
    public Object getItem(int i) {
        return menus[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MenuHolder menuHolder;
        if (convertView == null) {
            // 解析menu_list视图对象
            convertView = layoutInflater.inflate(R.layout.menu_list, null, false);
            menuHolder = new MenuHolder(convertView);

            convertView.setTag(menuHolder);
        } else {
            menuHolder = (MenuHolder) convertView.getTag();
        }
        String menuContext = (String) getItem(position);
        menuHolder.getItem().setText(menuContext);
        return convertView;
    }
}
