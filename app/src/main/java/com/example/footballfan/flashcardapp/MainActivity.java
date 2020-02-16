package com.example.footballfan.flashcardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean showingAnswers = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.questionText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.questionText).setVisibility(View.INVISIBLE);
                findViewById(R.id.answerText).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.answerText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answerText).setVisibility(View.INVISIBLE);
                findViewById(R.id.questionText).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.answerCorrect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answerCorrect).setBackgroundColor(getResources().getColor(R.color.correct_answer));
            }
        });

        findViewById(R.id.answerChoice1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answerChoice1).setBackgroundColor(getResources().getColor(R.color.wrong_answer));
                findViewById(R.id.answerCorrect).setBackgroundColor(getResources().getColor(R.color.correct_answer));
            }
        });

        findViewById(R.id.answerChoice2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answerChoice2).setBackgroundColor(getResources().getColor(R.color.wrong_answer));
                findViewById(R.id.answerCorrect).setBackgroundColor(getResources().getColor(R.color.correct_answer));
            }
        });

        findViewById(R.id.toggleVisibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingAnswers)
                {
                    findViewById(R.id.answerCorrect).setVisibility(View.INVISIBLE);
                    findViewById(R.id.answerChoice1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.answerChoice2).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.toggleVisibility)).setImageResource(R.drawable.visible_icon);
                    showingAnswers = false;
                }
                else
                {
                    findViewById(R.id.answerCorrect).setVisibility(View.VISIBLE);
                    findViewById(R.id.answerChoice1).setVisibility(View.VISIBLE);
                    findViewById(R.id.answerChoice2).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.toggleVisibility)).setImageResource(R.drawable.invisible_icon);
                    showingAnswers = true;
                }

            }
        });

    }
}
