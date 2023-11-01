package com.hk.mynote;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hk.mynote.adapter.MenuAdapter;
import com.hk.mynote.constans.Constants;
import com.hk.mynote.dbhelper.MyDBHelper;
import com.hk.mynote.util.CopyFileByUri;

import java.io.IOException;
import java.util.Arrays;

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
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri musicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent intent = this.getIntent();
        sendId = intent.getIntExtra("sendId", -1);
        init();

        // 为控件设置监听器
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                if (objectAnimator != null) {
                    objectAnimator.cancel();
                    objectAnimator = null;
                }
                finish();
            }
        });
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
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        if (objectAnimator != null) {
                            objectAnimator.cancel();
                            objectAnimator = null;
                        }
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
                    // 阅读
                    if (items[0].equals(item)) {
                        et_content.setCursorVisible(false);  // 隐藏光标
                        et_content.setFocusable(false);  // 失去焦点
                        et_content.setFocusableInTouchMode(false);  // 虚拟键盘隐藏
                        listView.setVisibility(View.GONE);
                    }
                    // 编辑
                    if (items[1].equals(item)) {
                        et_content.setCursorVisible(true);
                        et_content.setFocusable(true);  //
                        et_content.setFocusableInTouchMode(true);
                        listView.setVisibility(View.GONE);
                    }
                    // 手写
                    if (items[2].equals(item)) {

                    }
                    // 切歌
                    if (items[3].equals(item)) {
                        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        mediaIntent.setType("audio/*");
                        activityResultLauncher.launch(mediaIntent);
                        listView.setVisibility(View.GONE);
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

//    private boolean checkPermission() {
//        int permission = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO);
//        }
//        return permission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 权限已授予，现在可以调用takePersistableUriPermission
//                getContentResolver().takePersistableUriPermission(musicUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            } else {
//                // 权限被拒绝，需要处理这种情况
//                Toast.makeText(RecordActivity.this, "无法获取存储权限",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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
        objectAnimator = ObjectAnimator.ofFloat(music_icon, "rotation", 0f, 360f);

        menuAdapter = new MenuAdapter(menuList, RecordActivity.this);
        listView.setAdapter(menuAdapter);

        String uriString = Constants.MUSIC_MAP.get(String.valueOf(sendId));

        mediaPlayer = MediaPlayer.create(RecordActivity.this, R.raw.the_true);
        if (uriString != null) {
            mediaPlayer.reset();
//            Uri uri = Uri.parse(uriString);
            try {
                mediaPlayer.setDataSource(uriString);
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(RecordActivity.this, "音乐播放异常", Toast.LENGTH_SHORT).show();
            }
        }
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent date = result.getData();
                            if (date != null) {
                                musicUri = date.getData();

//                                // 检查是否具有权限，如果没有，请求它。
//                                if (checkPermission()) {
//                                    // 你有权限，继续你的操作
//                                    getContentResolver().takePersistableUriPermission(this.musicUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                } else {
//                                    // 请求权限
//                                    requestPermission();
//                                }

                                mediaPlayer.reset();
                                try {
                                    if (musicUri != null) {
                                        mediaPlayer.setDataSource(RecordActivity.this, musicUri);
                                        mediaPlayer.prepare();
                                        String path = CopyFileByUri.getPath(RecordActivity.this, musicUri);
                                        Constants.MUSIC_MAP.put(String.valueOf(sendId), path);
                                        mediaPlayer.start();
                                        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 设置动画重复次数为无限次
                                        objectAnimator.setRepeatMode(ObjectAnimator.RESTART); // 设置动画重复模式为重新开始
                                        objectAnimator.setDuration(4000);
                                        objectAnimator.start(); // 开始动画
                                    } else {
                                        Toast.makeText(RecordActivity.this, "切换音乐失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(RecordActivity.this, "切换音乐失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

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