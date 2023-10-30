package com.hk.mynote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 在这里编写你的逻辑代码
        Toast.makeText(context, "闹钟响了", Toast.LENGTH_SHORT).show();
        Log.d("AlarmReceiver", "闹钟响了");
    }
}