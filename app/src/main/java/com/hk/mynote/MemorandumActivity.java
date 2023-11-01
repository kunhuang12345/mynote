package com.hk.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hk.mynote.adapter.MemorandumListAdapter;
import com.hk.mynote.dbhelper.MemorandumDBHelper;
import com.hk.mynote.dbhelper.MyDBHelper;
import com.hk.mynote.po.Memorandum;
import com.hk.mynote.po.Note;

import java.util.ArrayList;
import java.util.List;

public class MemorandumActivity extends AppCompatActivity {

    private ImageView memorandum_backHome;
    private ImageView memorandum_add;
    private ListView listView;
    private List<Memorandum> resultList;
    private MemorandumListAdapter memorandumListAdapter;
    private MemorandumDBHelper memorandumDBHelper;

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

        // 长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                // 删除对话框
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(MemorandumActivity.this);

                builder.setTitle("取消此闹钟提示")
                        .setMessage("你确定要取消此闹钟提示吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Memorandum memorandum = (Memorandum) memorandumListAdapter.getItem(position);
                                Integer requestCode = memorandum.getRequestCode();

                                Intent intent = new Intent(MemorandumActivity.this, AlarmBroadcast.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MemorandumActivity.this,requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

                                Log.d("MemorandumActivity", pendingIntent.toString());

                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                                Integer memorandumId = memorandum.getId();
                                boolean deleteData = memorandumDBHelper.deleteData(memorandumId);
                                if (deleteData) {
                                    Toast.makeText(MemorandumActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MemorandumActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                }
                                loadData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();


                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MemorandumActivity.this, NoticeAddActivity.class);
                Memorandum memorandum = (Memorandum) memorandumListAdapter.getItem(position);
                Integer sendId = memorandum.getId();
                String memorandumTime = memorandum.getMemorandumTime();
                String remark = memorandum.getRemark();
                Integer requestCode = memorandum.getRequestCode();
                intent.putExtra("sendId", sendId);
                intent.putExtra("remark", remark);
                intent.putExtra("memorandumTime", memorandumTime);
                intent.putExtra("requestCode", requestCode);
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

        if (resultList != null) {
            resultList.clear();
        }
        memorandumDBHelper = new MemorandumDBHelper(MemorandumActivity.this, "memorandum.db",null, 1);
        resultList = memorandumDBHelper.queryData();

        memorandumListAdapter = new MemorandumListAdapter(resultList, MemorandumActivity.this);
        listView.setAdapter(memorandumListAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            loadData();
        }
    }

}