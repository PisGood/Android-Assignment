package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private String[] iqTest;
    FetchPageTask task = null;

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

                int qNum = jObj.getJSONArray("questions").length();
                JSONArray questions = jObj.getJSONArray("questions");

                int index = 0;



            } catch(Exception e){ }
        }
    }


    public void setTest(int index){
        Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
        i.putExtra("question", )

        startActivityForResult();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(i);
            }
        });

        if (task == null || task.getStatus().equals((AsyncTask.Status.FINISHED))){
            task = new FetchPageTask();
            task.execute("https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
