package com.almog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.almog.Task;
import com.almog.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by almog on 3/8/16.
 */
public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TO-tasks.db";

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_DESC = "desc";
    public static final String COLUMN_TASK_DUE = "due";
    public static final String COLUMN_TASK_PRIO = "prio";
    public static final String COLUMN_TASK_MEMBER = "member";
    public static final String COLUMN_TASK_LOCATION = "location";
    public static final String COLUMN_TASK_CATEGORY = "category";
    public static final String COLUMN_TASK_ASTATUS = "Astatus"; //accepted status
    public static final String COLUMN_TASK_CSTATUS = "Cstatus";//current status

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_MAIL = "email";
    private List<User> users;


    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String tasks = "CREATE TABLE " + TABLE_TASKS + "(" +
                COLUMN_TASK_ID + " TEXT PRIMARY KEY, " +
                COLUMN_TASK_DESC + " TEXT, " +
                COLUMN_TASK_DUE + " TEXT, " +
                COLUMN_TASK_PRIO + " TEXT, " +
                COLUMN_TASK_MEMBER + " TEXT, " +
                COLUMN_TASK_LOCATION + " TEXT, " +
                COLUMN_TASK_CATEGORY + " TEXT, " +
                COLUMN_TASK_ASTATUS + " TEXT, " +
                COLUMN_TASK_CSTATUS + " TEXT " +
                ")";
        String users = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_MAIL + " TEXT " +
                ")";
        db.execSQL(tasks);
        db.execSQL(users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_ID, task.getId());
        values.put(COLUMN_TASK_DESC, task.getDecs());
        values.put(COLUMN_TASK_DUE, task.getDue());
        values.put(COLUMN_TASK_PRIO, task.getPrio());
        values.put(COLUMN_TASK_LOCATION, task.getLocation());
        values.put(COLUMN_TASK_MEMBER, task.getMember());
        values.put(COLUMN_TASK_CATEGORY, task.getCategory());
        values.put(COLUMN_TASK_ASTATUS, task.getAstatus());
        values.put(COLUMN_TASK_CSTATUS, task.getCstatus());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            Task t = new Task(res.getString(res.getColumnIndex(COLUMN_TASK_ID)), res.getString(res.getColumnIndex(COLUMN_TASK_DESC)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_DUE)), res.getString(res.getColumnIndex(COLUMN_TASK_PRIO)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_MEMBER)), res.getString(res.getColumnIndex(COLUMN_TASK_LOCATION)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_CATEGORY)), res.getString(res.getColumnIndex(COLUMN_TASK_ASTATUS)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_CSTATUS)));
            tasks.add(t);
        }
        return tasks;
    }
    public List<Task> getTasksWait() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TASKS+" WHERE "+ COLUMN_TASK_ASTATUS+" = 'waiting'", null);
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            Task t = new Task(res.getString(res.getColumnIndex(COLUMN_TASK_ID)), res.getString(res.getColumnIndex(COLUMN_TASK_DESC)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_DUE)), res.getString(res.getColumnIndex(COLUMN_TASK_PRIO)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_MEMBER)), res.getString(res.getColumnIndex(COLUMN_TASK_LOCATION)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_CATEGORY)), res.getString(res.getColumnIndex(COLUMN_TASK_ASTATUS)),
                    res.getString(res.getColumnIndex(COLUMN_TASK_CSTATUS)));
            tasks.add(t);
        }
        return tasks;
    }

    public void delTask(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_ID + "=\"" + id + "\";");
    }

    public void updateTask(Task task) {
        Integer.parseInt(task.getId());
        //if task id exist in db then override this task
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getIdUser());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_MAIL, user.getMailUser());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void dropDB(){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
    }

    public List<User> getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> users = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            User u = new User();
            u.setUsername(res.getString(res.getColumnIndex(COLUMN_USER_NAME)));
            u.setMailUser(res.getString(res.getColumnIndex(COLUMN_USER_MAIL)));
            users.add(u);
        }
        return users;
    }


}