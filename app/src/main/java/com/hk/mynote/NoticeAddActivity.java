package com.hk.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hk.mynote.dbhelper.MemorandumDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoticeAddActivity extends AppCompatActivity {

    private Button clockButton;
    private EditText clockRemark;
    private ImageView clockBack;
    private ImageView clockSave;
    private AlarmManager alarmManager; //闹钟管理器
    //获取当前系统时间
    private  Calendar calendar;
    // 延迟意图设置
    private PendingIntent pendingIntent;
    private Intent intent;
    private MemorandumDBHelper memorandumDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);
        init();

        Intent dataIntent = this.getIntent();
        int sendId = dataIntent.getIntExtra("sendId", -1);
        if (sendId != -1) {
            String remark = dataIntent.getStringExtra("remark");
            String memorandumTime = dataIntent.getStringExtra("memorandumTime");
            clockButton.setText(memorandumTime);
            if ("你还没有备注哦~".equals(remark)) {
                clockRemark.setText("");
                clockRemark.setHint("你还没有备注哦~");
            } else {
                clockRemark.setText(remark);
            }
        }


        clockBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 保存或者更新闹钟
        clockSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendId == -1) {
                    int requestCode = (int) System.currentTimeMillis();
                    String clockTime = clockButton.getText().toString();
                    String remark = clockRemark.getText().toString();
                    if (clockTime.equals("")) {
                        Toast.makeText(NoticeAddActivity.this, "必须设置时间！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (remark.equals("")) {
                            remark = "你还没有备注哦~";
                        }
                        memorandumDBHelper = new MemorandumDBHelper(NoticeAddActivity.this, "memorandum.db", null, 1);
                        boolean insertData = memorandumDBHelper.insertData(clockTime, remark, requestCode);
                        if (!insertData) {
                            Toast.makeText(NoticeAddActivity.this, "闹钟设置失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            pendingIntent = PendingIntent.getBroadcast(NoticeAddActivity.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
                            Log.d("MemorandumActivity", pendingIntent.toString());
                            //设置闹钟
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            Toast.makeText(NoticeAddActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }
                    }
                }else {
                    memorandumDBHelper = new MemorandumDBHelper(NoticeAddActivity.this, "memorandum.db", null, 1);
                    String clockTime = clockButton.getText().toString();
                    String remark = clockRemark.getText().toString();
                    if (remark.equals(""))remark = "你还没有备注哦~";
                    int requestCode = dataIntent.getIntExtra("requestCode", -1);
                    boolean updateData = memorandumDBHelper.updateData(sendId, clockTime, remark, requestCode);
                    if (!updateData) {
                        Toast.makeText(NoticeAddActivity.this, "闹钟更新失败", Toast.LENGTH_SHORT).show();
                    } else {
                        if (calendar != null) {
                            pendingIntent = PendingIntent.getBroadcast(NoticeAddActivity.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
                            Log.d("MemorandumActivity", pendingIntent.toString());
                            //设置闹钟
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        Toast.makeText(NoticeAddActivity.this, "闹钟更新成功", Toast.LENGTH_SHORT).show();
                        setResult(2);
                        finish();
                    }
                }
            }
        });


        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClock(view);
            }
        });
    }

    //设置闹钟
    private void setClock(View view) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //弹出日期选择框
        DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year); //设置年份
                calendar.set(Calendar.MONTH, month); //设置月份
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); //设置日期

                //弹出时间选择框
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay); //设置小时数
                        calendar.set(Calendar.MINUTE, minute); //设置分钟数
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 \nHH:mm");
                        String dateTime = sdf.format(calendar.getTime());

                        intent = new Intent(NoticeAddActivity.this, AlarmBroadcast.class); //创建pendingIntent

                        clockButton.setText(dateTime);

                    }
                },hour,minute,true);

                timePickerDialog.show();
            }
        },year,month,day);

        datePickerDialog.show();
    }

    private void  init() {
        clockButton = findViewById(R.id.clock_time);
        clockRemark = findViewById(R.id.clock_remark);
        clockBack = findViewById(R.id.clock_back);
        clockSave = findViewById(R.id.clock_save);
        //获取闹钟管理器
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }
}