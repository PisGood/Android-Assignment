package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private JSONObject testQuestions;
    private JSONArray questionArray;

    FetchPageTask task = null;
    private Button btnStart;
    private TextView tvSummary;

    DbHelper dbHelper;
    SQLiteDatabase db;

    private int counter = 0;
    private double duration = 0;
    private int correctCount = 0;

    public DecimalFormat df = new DecimalFormat("#.#");

    private class FetchPageTask extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... values) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;

            try{
                url = new URL(values[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }catch (Exception e){
                result=e.getMessage();
            }
            return result;
        }


        protected void onPostExecute(String result){
            try{
                JSONObject jObj = new JSONObject(result);
                questionArray = jObj.getJSONArray("questions");

                JSONObject randomQuestionObj = questionArray.getJSONObject(new Random().nextInt(10-1) + 1);
                String randomQuestion = randomQuestionObj.getString("question");
            } catch(Exception e){ }
        }
}
    //Menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about:
                Intent i = new Intent(this, About.class);
                return true;
            case R.id.test_log:
                i = new Intent(this, ShowTestLog.class);
                startActivity(i);
                return true;
            case R.id.exit:
                this.finish();
            default:
                return super.onOptionsItemSelected (item);
        }
    }




    private void startTest(){
        try{
           // int totalQuestionNum = questionArray.length();
            int randomN = new Random().nextInt(questionArray.length());

            String randomQuestion =  questionArray.getJSONObject(randomN).getString("question");
            String answer = questionArray.getJSONObject(randomN).getString("answer");


        Intent i = new Intent(this, QuestionActivity.class);
        i.putExtra("question",randomQuestion);
        i.putExtra("answer", answer);

        startActivityForResult(i, 777);

        questionArray.remove(randomN);

        }catch(Exception e){}

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 777){
            counter++;


            //Get stat
            boolean result = data.getExtras().getBoolean("result");



            //Not yet completed 5 questions
            if (counter < 5){
                //Update duration
                duration += data.getExtras().getLong("duration");
                Log.d("extra", "onActivityResult: duration" + " += " + data.getExtras().getLong("duration"));

                //Update correctCount
                correctCount += data.getExtras().getBoolean("result")? 1:0;
                Log.d("extra", "onActivityResult: Correct Count + 1? "+ correctCount);

                startTest();
            } else{
                //Insert record to DB
                //Date & Time
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

                //Duration


                ContentValues values = new ContentValues();
                values.put(TestLog.TestLogEntry.COLUMN_NAME_TESTDATE, dateFormat.format(date));
                values.put(TestLog.TestLogEntry.COLUMN_NAME_TESTTIME, timeFormat.format(date));
                values.put(TestLog.TestLogEntry.COLUMN_NAME_DURATION, duration / 1000);
                values.put(TestLog.TestLogEntry.COLUMN_NAME_CORRECTCOUNT, correctCount);

                db.insert(TestLog.TestLogEntry.TABLE_NAME, null,values);

                //Show result on home page
                String msg = "Summary on previous test:\n\n" +
                             "Average time spend on each question: " + df.format(duration/5000) + "\n" +
                             "Total time spent: " + df.format(duration /1000) + "\n" +
                             "Correct count: " + correctCount;
                tvSummary.setText(msg);

                //Reset duration, correctCount
                duration = 0;
                correctCount = 0;
                counter = 0;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find view
        tvSummary = findViewById(R.id.tvSummary);
        btnStart = findViewById(R.id.btnStart);

        // Insert record
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        //Get Json from url
        if (task == null || task.getStatus().equals((AsyncTask.Status.FINISHED))){
            task = new FetchPageTask();
            task.execute("https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment");
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
            }
        });



    }

}
