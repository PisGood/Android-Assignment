package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShowQuestionLog extends AppCompatActivity {
    ListView lvQLog;
    TextView tvHeading;
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question_log);

        tvHeading = findViewById(R.id.q_log_header);
        lvQLog = findViewById(R.id.lv_q_log);


        String dataStr = String.format("%-60s %-25s %-10s \n", "Question", "Your Ans", "Correct");
        tvHeading.setText(dataStr);

        //Get Question Log from db
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from questionslog", null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_listview_qlog, cursor,
                new String[]{

                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_QUESTION,
                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_YOURANSWER,
                        QuestionsLog.QuestionsLogEntry.COLUMN_NAME_ISCORRECT
                },
                new int[]{R.id.listview_question, R.id.listview_your_answer, R.id.listview_is_correct});

        lvQLog.setAdapter(adapter);
    }
}