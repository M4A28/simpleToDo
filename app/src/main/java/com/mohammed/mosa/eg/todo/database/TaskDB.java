package com.mohammed.mosa.eg.todo.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;
import java.util.Date;

public class TaskDB extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "todo.db";
    public static final String DB_TABLE_TODO_NAME = "Todo";
    public static final String DB_TB_ID = "id";
    public static final String DB_TB_TASK = "task";
    public static final String DB_TB_PRIORITY = "priority"; // 4 side 3 low, 2 medum, 1 high
    public static final String DB_TB_IS_DONE= "finish";
    public static final String DB_TB_DATE = "date";

    public TaskDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + DB_TABLE_TODO_NAME + " ( "
                + DB_TB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DB_TB_TASK + " TEXT, "
                + DB_TB_PRIORITY + " INTEGER, "
                + DB_TB_IS_DONE + " TEXT, "
                + DB_TB_DATE + " REAL"
                + " )";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // until now do nothing
    }


    public boolean insertTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_TB_TASK, task.getTask());
        values.put(DB_TB_PRIORITY, task.getPriority());
        values.put(DB_TB_IS_DONE, Utility.toInt(task.isDone()));
        values.put(DB_TB_DATE, Utility.toLong(task.getFinishDate()));
        long result = db.insert(DB_TABLE_TODO_NAME, null, values);
        db.close();
        return  result != -1;
    }

    public boolean updateTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_TB_TASK, task.getTask());
        values.put(DB_TB_PRIORITY, task.getPriority());
        values.put(DB_TB_IS_DONE, task.isDone());
        values.put(DB_TB_DATE, task.getFinishDate().getTime());
        String[] args = {""+task.getId()};
        int result = db.update(DB_TABLE_TODO_NAME,
                values, DB_TB_ID + "=?", args);
        db.close();
        return result > 0;
    }

    public long getTasksCount(){
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, DB_TABLE_TODO_NAME);

    }

    public ArrayList<Task> getAllUnfinishedTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql= "SELECT * FROM " + DB_TABLE_TODO_NAME + " WHERE " + DB_TB_IS_DONE +" =0";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DB_TB_ID));
                @SuppressLint("Range") String _task = cursor.getString(cursor.getColumnIndex(DB_TB_TASK));
                @SuppressLint("Range") boolean isDone = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DB_TB_IS_DONE)));
                @SuppressLint("Range") int priority = cursor.getInt(cursor.getColumnIndex(DB_TB_PRIORITY));
                @SuppressLint("Range") Date date = Utility.toDate(cursor.getLong(cursor.getColumnIndex(DB_TB_DATE)));

                Task task = new Task(id, _task, isDone, priority, date);
                tasks.add(task);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public ArrayList<Task> getAllFinishedTask(){
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql= "SELECT * FROM " + DB_TABLE_TODO_NAME + " WHERE " + DB_TB_IS_DONE +" =1";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DB_TB_ID));
                @SuppressLint("Range") String _task = cursor.getString(cursor.getColumnIndex(DB_TB_TASK));
                @SuppressLint("Range") boolean isDone = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DB_TB_IS_DONE)));
                @SuppressLint("Range") int priority = cursor.getInt(cursor.getColumnIndex(DB_TB_PRIORITY));
                @SuppressLint("Range") Date date = Utility.toDate(cursor.getLong(cursor.getColumnIndex(DB_TB_DATE)));

                Task task = new Task(id, _task, isDone, priority, date);
                tasks.add(task);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public boolean deleteAllTasks(){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(DB_TABLE_TODO_NAME, null, null);
        db.close();
        return result > 0;

    }

    public boolean deleteAllFinishedTasks(){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(DB_TABLE_TODO_NAME, DB_TB_IS_DONE+ "=1", null);
        db.close();
        return result > 0;

    }

    public boolean makeTaskComplete(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_TB_IS_DONE, Utility.toInt(true));
        String[] args = {""+task.getId()};
        int result = db.update(DB_TABLE_TODO_NAME,
                values, DB_TB_ID + "=?", args);
        db.close();
        return result > 0;
    }

    public boolean makeTaskUnComplete(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_TB_IS_DONE, Utility.toInt(false));
        values.put(DB_TB_DATE, task.getFinishDate().getTime());
        String[] args = {""+task.getId()};
        int result = db.update(DB_TABLE_TODO_NAME,
                values, DB_TB_ID + "=?", args);
        db.close();
        return result > 0;
    }

    public boolean deleteTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(task.getId())};
        int result = db.delete(DB_TABLE_TODO_NAME, DB_TB_ID + "=?", args);
        db.close();
        return result > 0;

    }


}
