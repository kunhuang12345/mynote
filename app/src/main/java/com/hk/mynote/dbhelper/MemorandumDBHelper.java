package com.hk.mynote.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hk.mynote.po.Memorandum;
import com.hk.mynote.po.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

// 备忘录数据库表
public class MemorandumDBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public MemorandumDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 建表语句
        sqLiteDatabase.execSQL("CREATE TABLE memorandum(id INTEGER PRIMARY KEY AUTOINCREMENT, memorandum_time TEXT, remark TEXT, request_code INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 删除旧的表
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS memorandum");
        // 调用onCreate()方法来创建新的表
        onCreate(sqLiteDatabase);
    }

    /**
     * insert
     */
    public boolean insertData(String memorandumTime, String remark, Integer requestCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("memorandum_time", memorandumTime);
        contentValues.put("remark", remark);
        contentValues.put("request_code", requestCode);
        long insert = db.insert("memorandum", null, contentValues);
        return insert > 0;
    }

    /**
     * update
     */
    public boolean updateData(Integer id, String memorandumTime, String remark, Integer requestCode) {
        // 将需要更新的内容存入contentValues
        ContentValues contentValues = new ContentValues();
        if (memorandumTime!=null)contentValues.put("memorandum_time", memorandumTime);
        if (remark!=null)contentValues.put("remark", remark);
        if (requestCode!=null)contentValues.put("request_code", requestCode);
        int update = db.update("memorandum", contentValues, "id=?", new String[]{String.valueOf(id)});
        return update>0;
    }

    /**
     * delete
     */
    public boolean deleteData(Integer id) {
        String value = String.valueOf(id);
        int delete = db.delete("memorandum", "id=?", new String[]{value});
        return delete>0;
    }

    public List<Memorandum> queryData() {
        List<Memorandum> list = new ArrayList<>();
        Cursor memorandums = db.query("memorandum", null, null, null, null, null, null);
        while(memorandums.moveToNext()) {
            Memorandum memorandum = new Memorandum();
            memorandum.setId(memorandums.getInt(0));
            memorandum.setMemorandumTime(memorandums.getString(1));
            memorandum.setRemark(memorandums.getString(2));
            memorandum.setRequestCode(memorandums.getInt(3));
            list.add(memorandum);
        }
        return list;
    }
}
