package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    RadioGroup rgAnswer;
    RadioButton rbA, rbB, rbC, rbD;
    Button btnCheck, btnNext;
    TextView tvQuestion;

    boolean isCorrect = false;

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
        btnNext = findViewById(R.id.btnNext);


        String question = getIntent().getStringExtra("question");
        final String answer = getIntent().getStringExtra("answer");

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






        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedRb = rgAnswer.getCheckedRadioButtonId();

                rbA.setTextColor(Color.BLACK);
                rbB.setTextColor(Color.BLACK);
                rbC.setTextColor(Color.BLACK);
                rbD.setTextColor(Color.BLACK);

                //Change Text Color & update result
                if (((RadioButton)findViewById(selectedRb)).getText().toString().equals(answer)){
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
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("result", isCorrect);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
}
