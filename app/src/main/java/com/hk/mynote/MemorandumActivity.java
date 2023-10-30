package com.hk.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hk.mynote.adapter.MemorandumListAdapter;
import com.hk.mynote.po.Memorandum;

import java.util.ArrayList;
import java.util.List;

public class MemorandumActivity extends AppCompatActivity {

    private ImageView memorandum_backHome;
    private ImageView memorandum_add;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorandum);
        init();

        memorandum_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        memorandum_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemorandumActivity.this, NoticeAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    // 数据初始化
    private void init() {
        memorandum_backHome = findViewById(R.id.memorandum_backHome);
        memorandum_add = findViewById(R.id.memorandum_add);
        listView = findViewById(R.id.memorandum_list);
        loadData();
    }

    // 数据库数据加载
    private void loadData() {
        List<Memorandum> list = new ArrayList<>();
        Memorandum memorandumTail = new Memorandum();
        memorandumTail.setMemorandumTime("test");
        memorandumTail.setRemark("test");
        list.add(memorandumTail);
        Memorandum memorandumTail2 = new Memorandum();
        memorandumTail2.setMemorandumTime("test");
        memorandumTail2.setRemark("test");
        list.add(memorandumTail2);
        MemorandumListAdapter memorandumListAdapter = new MemorandumListAdapter(list, MemorandumActivity.this);
        listView.setAdapter(memorandumListAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            loadData();
        }
    }

}