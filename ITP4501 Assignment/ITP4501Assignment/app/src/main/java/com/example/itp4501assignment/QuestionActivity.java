package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    RadioGroup rgAnswer;
    RadioButton rbA, rbB, rbC, rbD;
    Button btnCheck, btnNext;
    TextView tvQuestion, tvProgress;
    static int qNum;
    long startTime;
    long endTime;
    boolean isCorrect = false;
    Intent data;
    DbHelper dbHelper;
    SQLiteDatabase db;
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.question);
        rgAnswer = findViewById(R.id.rgAnswer);
        rbA = findViewById(R.id.A);
        rbB = findViewById(R.id.B);
        rbC = findViewById(R.id.C);
        rbD = findViewById(R.id.D);
        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setEnabled(false);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        tvProgress = findViewById(R.id.progress);
        startTime = Calendar.getInstance().getTimeInMillis();
        qNum++;
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();



        //State the current question no. in textview
        int qRemain = 5 - qNum;
        String msg = "You are on Q" + qNum + ".\n " + qRemain + " more to go.";
        if (qNum == 5){
            msg = "Final Question!";
            qNum = 0;
        }
        tvProgress.setText(msg);

        //Get String Extra from intent
        question = getIntent().getStringExtra("question");
        final String answer = getIntent().getStringExtra("answer");

        rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnCheck.setEnabled(true);
            }
        });

        //Set Text: Question & Answer
        tvQuestion.setText(question);
        int correctQ = new Random().nextInt(3);
        switch (correctQ){
            case 0:
                rbA.setText(getIntent().getStringExtra("answer"));
                rbB.setText(String.valueOf((new Random().nextInt(Integer.parseInt(answer) - 1))));
                rbC.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                rbD.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                break;

            case 1:
                rbB.setText(getIntent().getStringExtra("answer"));
                rbA.setText(String.valueOf((new Random().nextInt(Integer.parseInt(answer) - 1))));
                rbC.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                rbD.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                break;

            case 2:
                rbC.setText(getIntent().getStringExtra("answer"));
                rbB.setText(String.valueOf((new Random().nextInt(Integer.parseInt(answer) - 1))));
                rbA.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                rbD.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                break;

            case 3:
                rbD.setText(getIntent().getStringExtra("answer"));
                rbB.setText(String.valueOf((new Random().nextInt(Integer.parseInt(answer) - 1))));
                rbC.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                rbA.setText(String.valueOf(new Random().nextInt(Integer.parseInt(answer) - 1)));
                break;

        }



        //Check answer
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rbA.setTextColor(Color.BLACK);
                rbB.setTextColor(Color.BLACK);
                rbC.setTextColor(Color.BLACK);
                rbD.setTextColor(Color.BLACK);

                //Disable "Check" button & enable "Next" button
                btnNext.setEnabled(true);
                btnCheck.setEnabled(false);

                //Change Text Color & update result
                int selectedRb = rgAnswer.getCheckedRadioButtonId();
                String selectedAns = ((RadioButton)findViewById(selectedRb)).getText().toString();
                if (selectedAns.equals(answer)){
                    isCorrect = true;
                    ((RadioButton)findViewById(selectedRb)).setTextColor(Color.GREEN);
                    Toast.makeText(QuestionActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                } else{
                    ((RadioButton)findViewById(selectedRb)).setTextColor(Color.BLACK);
                    ((RadioButton)findViewById(selectedRb)).setTextColor(Color.RED);
                }
                //Lock Radio Button
                for (int i = 0; i < rgAnswer.getChildCount(); i++) {
                    rgAnswer.getChildAt(i).setEnabled(false);
                }


                ContentValues values = new ContentValues();

                values.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_QUESTION, question);
                values.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_YOURANSWER,selectedAns);
                values.put(QuestionsLog.QuestionsLogEntry.COLUMN_NAME_ISCORRECT, isCorrect);

                db.insert(QuestionsLog.QuestionsLogEntry.TABLE_NAME, null,values);


            }
        });
        //Send back result & call startTest()
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = Calendar.getInstance().getTimeInMillis();
                long duration = endTime - startTime;


                data = new Intent();
                //Put extra for Test Log --> duration, result, testDate
                data.putExtra("duration", duration);
                data.putExtra("result", isCorrect);


                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
}
