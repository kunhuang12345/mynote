package com.hk.mynote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk.mynote.po.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyDBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    // 创建数据库和表
    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表语句
        db.execSQL("CREATE TABLE note(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 删除旧的表
        db.execSQL("DROP TABLE IF EXISTS note");
        // 调用onCreate()方法来创建新的表
        onCreate(db);
    }

    // 下面为对表的操作部分

    /**
     * insert
     */
    public boolean insertData(String title, String content) {
        ContentValues contentValues = new ContentValues();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time = sdf.format(date);
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("time", time);
        long insert = db.insert("note", null, contentValues);
        return insert > 0;
    }

    /**
     * delete
     */
    public boolean deleteData(Integer id) {
        String value = String.valueOf(id);
        int delete = db.delete("note", "id=?", new String[]{value});
        return delete>0;
    }

    /**
     * update
     */
    public boolean updateData(Integer id, String title, String content) {
        // 将需要更新的内容存入contentValues
        ContentValues contentValues = new ContentValues();
        if (title!=null)contentValues.put("title", title);
        if (content!=null)contentValues.put("content", content);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String time = sdf.format(date);
        contentValues.put("time", time);
        int update = db.update("note", contentValues, "id=?", new String[]{String.valueOf(id)});
        return update>0;
    }

    /**
     * query
     */
    public List<Note> queryData() {
        List<Note> list = new ArrayList<>();
        Cursor notes = db.query("note", null, null, null, null, null, null);
        while(notes.moveToNext()) {
            Note note = new Note();
            note.setId(notes.getInt(0));
            note.setTitle(notes.getString(1));
            note.setContent(notes.getString(2));
            note.setNote_time(notes.getString(3));
            list.add(note);
        }
        return list;
    }

}
