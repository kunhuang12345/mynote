package com.hk.mynote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NoticeAddActivity extends AppCompatActivity {

    private Button clockButton;
    private EditText clockRemark;
    private ImageView clockBack;
    private ImageView clockSave;
    private AlarmManager alarmManager; //闹钟管理器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);
        init();

        clockBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clockSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClock(view);
            }

            private void setClock(View view) {
                    //获取当前系统时间
                    final Calendar calendar = Calendar.getInstance();
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

                                    Intent intent = new Intent(NoticeAddActivity.this, AlarmReceiver.class); //创建pendingIntent
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NoticeAddActivity.this,0X102, intent, PendingIntent.FLAG_IMMUTABLE);

                                    //设置闹钟
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                    Toast.makeText(NoticeAddActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
                                }
                            },hour,minute,true);

                            timePickerDialog.show();
                        }
                    },year,month,day);

                    datePickerDialog.show();
                }

        });
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