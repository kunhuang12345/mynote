package com.hk.mynote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hk.mynote.R;
import com.hk.mynote.po.Note;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Note> noteList;

    // 用于将某个布局转换为view的对象
    private LayoutInflater layoutInflater;

    public MyAdapter(List<Note> list, Context context) {
        noteList = list;
        // 从传入的上下对象拿到布局解析器
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteList.get(noteList.size() - 1 - i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // 解析itemlayout视图对象
            convertView = layoutInflater.inflate(R.layout.itemlayout, null, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = (Note) getItem(position);
        viewHolder.getT_content().setText(note.getTitle());
        viewHolder.getT_time().setText(note.getNote_time());
        return convertView;
    }



}
