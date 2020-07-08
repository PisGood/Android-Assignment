package com.example.itp4501assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionActivity extends AppCompatActivity {
    RadioButton rbA, rbB, rbC, rbD;
    TextView tvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.question);
        rbA = findViewById(R.id.A);
        rbB = findViewById(R.id.B);
        rbC = findViewById(R.id.C);
        rbD = findViewById(R.id.D);



        tvQuestion.setText(getIntent().getStringExtra("question"));
    }
}
