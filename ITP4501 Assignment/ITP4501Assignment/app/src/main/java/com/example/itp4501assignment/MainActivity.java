package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private JSONObject testQuestions;
    private int counter = 0;
    private JSONArray questionArray;

    FetchPageTask task = null;
    private Button btnStart;
    private TextView tvSummary;

    DbHelper dbHelper;
    SQLiteDatabase db;

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
        counter++;
        }catch(Exception e){}

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 777){
            boolean result = data.getExtras().getBoolean("result");
            if (counter < 5){
                startTest();

            } else{

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
