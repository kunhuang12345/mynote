package com.hk.mynote;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示闹钟提醒框
        new AlertDialog.Builder(AlarmActivity.this)
                .setTitle("闹钟")
                .setMessage("时间到了")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int which) {
                        AlarmActivity.this.finish();
                    }
                }).create().show();
    }
}
