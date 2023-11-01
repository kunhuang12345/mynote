package com.hk.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hk.mynote.adapter.MenuAdapter;
import com.hk.mynote.constans.Constants;
import com.hk.mynote.dbhelper.MyDBHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class RecordActivity extends AppCompatActivity {
    private ListView listView;
    private MenuAdapter menuAdapter;
    private MyDBHelper myDBHelper;
    private ImageView backHome, delete, saveNote,menu,music_icon;
    private EditText et_content, title;
    private Integer sendId;
    private MediaPlayer mediaPlayer;
    private ObjectAnimator objectAnimator;
    private String[] menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        init();

        // 为控件设置监听器
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                finish();
            }
        });
        Intent intent = this.getIntent();
        sendId = intent.getIntExtra("sendId", -1);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diaryTitle = title.getText().toString();
                if (sendId != -1) {
                    myDBHelper = new MyDBHelper(RecordActivity.this, "note.db",null, 2);
                    String content = et_content.getText().toString();
                    boolean updateData = myDBHelper.updateData(sendId, diaryTitle, content);
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
                        if (diaryTitle.equals("")) {
                            diaryTitle = "Diary";
                        }
                        boolean insert = myDBHelper.insertData(diaryTitle, content);
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

        if (sendId != -1) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            et_content.setText(content);
            this.title.setText(title);
        }

        // 菜单监听器
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = listView.getVisibility();
                if (visibility == View.VISIBLE) {
                    listView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });

        // menuList栏点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = (String) menuAdapter.getItem(position);
                if (Arrays.asList(Constants.MENU).contains(item)) {
                    String[] items = Constants.MENU;
                    if (items[0].equals(item)) {
                        et_content.setCursorVisible(false);  // 隐藏光标
                        et_content.setFocusable(false);  // 失去焦点
                        et_content.setFocusableInTouchMode(false);  // 虚拟键盘隐藏
                        listView.setVisibility(View.GONE);
                    }
                    if (items[1].equals(item)) {
                        et_content.setCursorVisible(true);
                        et_content.setFocusable(true);  //
                        et_content.setFocusableInTouchMode(true);
                        listView.setVisibility(View.GONE);
                    }
                    if (items[2].equals(item)) {

                    }
                    if (items[3].equals(item)) {

                    }
                }
            }
        });

        // music_icon
        music_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    objectAnimator.cancel();
                } else {
                    mediaPlayer.start();
                    objectAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 设置动画重复次数为无限次
                    objectAnimator.setRepeatMode(ObjectAnimator.RESTART); // 设置动画重复模式为重新开始
                    objectAnimator.setDuration(4000);
                    objectAnimator.start(); // 开始动画
                }
            }
        });

    }

    // 获取控件对象
    private void init() {
        backHome = findViewById(R.id.backHome);
        delete = findViewById(R.id.delete);
        saveNote = findViewById(R.id.save_note);
        title = findViewById(R.id.title);
        et_content = findViewById(R.id.content);
        menuList = getResources().getStringArray(R.array.menus);
        listView = findViewById(R.id.menu_list);
        menu = findViewById(R.id.menu);
        music_icon = findViewById(R.id.music_icon);
        mediaPlayer = MediaPlayer.create(RecordActivity.this, R.raw.the_true);
        objectAnimator = ObjectAnimator.ofFloat(music_icon, "rotation", 0f, 360f);

        menuAdapter = new MenuAdapter(menuList, RecordActivity.this);
        listView.setAdapter(menuAdapter);
    }


    // 触摸事件，当触摸非listView时，listView菜单框关闭
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (listView.getVisibility() == View.VISIBLE) {
                Rect outRect = new Rect();
                listView.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    listView.setVisibility(View.GONE);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


}