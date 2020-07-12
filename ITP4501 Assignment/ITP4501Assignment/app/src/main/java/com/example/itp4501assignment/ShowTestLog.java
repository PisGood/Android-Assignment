package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShowTestLog extends AppCompatActivity {
    TextView tvHeading;
    ListView myListView;
    DbHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_log);

        tvHeading = findViewById(R.id.heading);
        myListView = findViewById(R.id.listview1);

        String dataStr = String.format("%-35s %-27s %-20s %s\n", "Start Date", "Start Time", "Finish Time", "Correct Count");
        tvHeading.setText(dataStr);



        //Get data from db
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from testlog", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_listview, cursor,
                new String[]{
                        TestLog.TestLogEntry.COLUMN_NAME_TESTDATE,
                        TestLog.TestLogEntry.COLUMN_NAME_TESTTIME,
                        TestLog.TestLogEntry.COLUMN_NAME_DURATION,
                        TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT
                },
                new int[]{R.id.tvDate, R.id.tvTime, R.id.tvDuration, R.id.tvCount});

        myListView.setAdapter(adapter);


    }
}