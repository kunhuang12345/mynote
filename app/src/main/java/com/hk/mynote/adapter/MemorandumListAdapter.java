package com.hk.mynote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hk.mynote.po.Memorandum;
import com.hk.mynote.R;

import java.util.List;

public class MemorandumListAdapter extends BaseAdapter {

    private List<Memorandum> memorandumTailList;

    private LayoutInflater layoutInflater;

    public MemorandumListAdapter(List<Memorandum> memorandumList, Context context) {
        this.memorandumTailList = memorandumList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return memorandumTailList.size();
    }

    @Override
    public Object getItem(int i) {
        return memorandumTailList.get(memorandumTailList.size() - i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MemorandumListHolder memorandumListHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.memorandum_list, null, false);
            memorandumListHolder = new MemorandumListHolder(convertView);
            convertView.setTag(memorandumListHolder);
        } else {
            memorandumListHolder = (MemorandumListHolder) convertView.getTag();
        }
        Memorandum memorandumTail = (Memorandum) getItem(position);
        memorandumListHolder.getItemClock().setText(memorandumTail.getMemorandumTime());
        memorandumListHolder.getItemRemark().setText(memorandumTail.getRemark());
        return convertView;
    }
}
