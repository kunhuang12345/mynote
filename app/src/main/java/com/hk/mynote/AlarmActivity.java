package com.hk.mynote;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hk.mynote.util.DataShareUtil;

public class AlarmActivity extends AppCompatActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //显示闹钟提醒框
        //显示闹钟提醒框
        AlertDialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("又是美好的一天~")
                .setMessage("该写日记啦！！！")
                .setPositiveButton("现在就去", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MediaPlayer mediaPlayer = DataShareUtil.getMediaPlayer();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("稍后再去", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MediaPlayer mediaPlayer = DataShareUtil.getMediaPlayer();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        AlarmActivity.this.finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
