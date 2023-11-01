package com.hk.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.mynote.adapter.MyAdapter;
import com.hk.mynote.constans.Constants;
import com.hk.mynote.dbhelper.MyDBHelper;
import com.hk.mynote.po.Note;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView noticeView;
    private ImageView add;
    private MyDBHelper myDBHelper;
    private MyAdapter myAdapter;
    private List<Note> resultList;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("data", MODE_PRIVATE);
        String musicMap = sp.getString("music_map", null);
        if (musicMap != null) {
            // 使用Gson将JSON字符串转换回Map集合
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            Constants.MUSIC_MAP = gson.fromJson(musicMap, type);
        }

        noticeView = findViewById(R.id.notice);
        listView = findViewById(R.id.listview);
        add = findViewById(R.id.image);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击添加按钮
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });
        init();

        // 设置列表项的点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 当列表项点击时，对该项的内容进行修改操作
                Note note = (Note) myAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                Integer sendId = note.getId();
                String sendContent = note.getContent();
                String sendTitle = note.getTitle();
                intent.putExtra("sendId", sendId);
                intent.putExtra("title", sendTitle);
                intent.putExtra("content", sendContent);
                startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 删除对话框
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("删除记录")
                        .setMessage("你确定要删除这条记录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Note note = (Note) myAdapter.getItem(position);
                                Integer noteId = note.getId();
                                boolean deleteData = myDBHelper.deleteData(noteId);
                                if (deleteData) {
                                    Constants.MUSIC_MAP.remove(noteId);
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                }
                                init();
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

        // 备忘录监听器
        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemorandumActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    // 查询数据库中的内容，将表中的数据显示在ListView上面
    private void init() {
        if (resultList != null) {
            resultList.clear();
        }
        myDBHelper = new MyDBHelper(MainActivity.this, "note.db",null, 2);
        resultList = myDBHelper.queryData();
        myAdapter = new MyAdapter(resultList, MainActivity.this);
        listView.setAdapter(myAdapter);

        String json = new Gson().toJson(Constants.MUSIC_MAP);
        sp.edit().putString("music_map", json).apply();
    }

    // 数据回传时自动执行，接收回传数据
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            // 数据库内容更新，主页内容同时更新
            init();
        }
    }
}