package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BarChart extends AppCompatActivity {
    ListView lisView;

    DbHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from testlog", null);
        MyAdapter adapter = new MyAdapter(this, R.layout.activity_listview, cursor,
                new String[]{
                        TestLog.TestLogEntry.COLUMN_NAME_TESTDATE,
                        TestLog.TestLogEntry.COLUMN_NAME_TESTTIME,
                        TestLog.TestLogEntry.COLUMN_NAME_DURATION,
                        TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT
                },
                new int[]{R.id.tvDate, R.id.tvTime, R.id.tvDuration, R.id.tvCount});
        lisView.setAdapter(adapter);
    }
}

class MyAdapter extends SimpleCursorAdapter {
    Context context;
    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cursor cursor = (Cursor) getItem(position);
        int correctCount = cursor.getInt(cursor.getColumnIndex((TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT)));

        View rowView = new Panel(context, correctCount);
        rowView.setWillNotDraw(false);
        return rowView;
    }

}
class Panel extends View {
    int correctCount;

    public Panel(Context context, int correctCount) {
        super(context);
        this.correctCount = correctCount;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        setLayoutParams(params);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = getHeight();
        int w = getWidth();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        float ratio = (float) correctCount / (float) 5;
        int l = Math.round(w * ratio);
        //if (l > 0) {
            canvas.drawRect(20, 20, l, h, paint);
       // }

    }
}