package com.example.itp4501assignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static String SQL_CREATE_TESTLOG =
            "CREATE TABLE " + TestLog.TestLogEntry.TABLE_NAME + " (" +
                    TestLog.TestLogEntry._ID + " INTEGER PRIMARY KEY," +
                    TestLog.TestLogEntry.COLUMN_NAME_TESTDATE + " TEXT," +
                    TestLog.TestLogEntry.COLUMN_NAME_TESTTIME + " TEXT," +
                    TestLog.TestLogEntry.COLUMN_NAME_DURATION + " INTEGER," +
                    TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT + " INTEGER)";

    private static final String SQL_DELETE_TESTLOG =
            "DROP TABLE IF EXISTS " + TestLog.TestLogEntry.TABLE_NAME;

    private static final String SQL_CREATE_QUESTIONLOG =
            "CREATE TABLE " + QuestionsLog.QuestionsLogEntry.TABLE_NAME + " ("+
                    QuestionsLog.QuestionsLogEntry._ID + " INTEGER PRIMARY KEY," +
                    QuestionsLog.QuestionsLogEntry.COLUMN_NAME_QUESTION + " TEXT," +
                    QuestionsLog.QuestionsLogEntry.COLUMN_NAME_YOURANSWER + " INTEGER," +
                    QuestionsLog.QuestionsLogEntry.COLUMN_NAME_ISCORRECT + " TEXT)";

    private static final String SQL_DELETE_QUESTIONSLOG =
            "DROP TABLE IF EXISTS " + QuestionsLog.QuestionsLogEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IQTestdb";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DbHelper", "creating tables");
        db.execSQL(SQL_CREATE_TESTLOG);
        db.execSQL(SQL_CREATE_QUESTIONLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TESTLOG);
        db.execSQL(SQL_DELETE_QUESTIONSLOG);
        onCreate(db);
    }


}
