package com.hk.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hk.mynote.dbhelper.MyDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {

    private MyDBHelper myDBHelper;
    private ImageView backHome, delete, saveNote;
    private TextView title,showTime;
    private EditText et_content;
    private Integer sendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        init();

        // 为控件设置监听器
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = this.getIntent();
        sendId = intent.getIntExtra("sendId", -1);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendId != -1) {
                    myDBHelper = new MyDBHelper(RecordActivity.this, "note.db",null, 2);
                    String top = title.getText().toString();
                    String content = et_content.getText().toString();
                    boolean updateData = myDBHelper.updateData(sendId, top, content);
                    if (!updateData) {
                        Toast.makeText(RecordActivity.this, "日记保存失败！", Toast.LENGTH_LONG).show();
                    } else {
                        setResult(2);
                        finish();
                    }
                } else {
                    // 获取编辑框中输入的内容
                    String content = et_content.getText().toString();
                    if (content.equals("")) {
                        Toast.makeText(RecordActivity.this, "内容不能为空！", Toast.LENGTH_LONG).show();
                    } else {
                        myDBHelper = new MyDBHelper(RecordActivity.this, "note.db",null, 2);
                        boolean insert = myDBHelper.insertData("Note", content);
                        if (!insert) {
                            Toast.makeText(RecordActivity.this, "日记保存失败！", Toast.LENGTH_LONG).show();
                        } else {
                            setResult(2);
                            finish();
                        }
                    }
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_content.setText("");
            }
        });

        if (sendId == -1) {
            System.out.println(111);
        } else {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            et_content.setText(content);
            this.title.setText(title);
        }

    }

    // 获取控件对象
    private void init() {
        backHome = findViewById(R.id.backHome);
        delete = findViewById(R.id.delete);
        saveNote = findViewById(R.id.save_note);
        title = findViewById(R.id.title);
        showTime = findViewById(R.id.showTime);
        et_content = findViewById(R.id.content);
    }

}