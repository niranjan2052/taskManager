package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import kotlinx.coroutines.scheduling.Task;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskDB";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_TASK = "Tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_STATUS = "status";


    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE Tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, dueDate TEXT
        db.execSQL("CREATE TABLE " + TABLE_TASK +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TITLE + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DUE_DATE + " TEXT," +
                KEY_STATUS + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    public void addTask(String title, String description, String dueDate, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DUE_DATE, dueDate);
        values.put(KEY_STATUS, status);

        db.insert(TABLE_TASK, null, values);
    }

    public ArrayList<TaskModel> fetchTask() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASK, null);
        ArrayList<TaskModel> arrTask = new ArrayList<>();
        while (cursor.moveToNext()) {
            TaskModel model = new TaskModel();
            model.taskId = cursor.getInt(0);
            model.title = cursor.getString(1);
            model.description = cursor.getString(2);
            model.dueDate = cursor.getString(3);
            model.status = cursor.getInt(4) == 1;

            arrTask.add(model);
        }
        return arrTask;
    }

    public void updateTask(TaskModel taskModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TITLE, taskModel.title);
        cv.put(KEY_DESCRIPTION, taskModel.description);
        cv.put(KEY_DUE_DATE, taskModel.dueDate);
        cv.put(KEY_STATUS, taskModel.status);
        database.update(TABLE_TASK, cv, KEY_ID + " = " + taskModel.taskId, null);
    }

    public void deleteTask(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete(TABLE_TASK, KEY_ID + " = ? ", new String[]{String.valueOf(id)});
    }
}
