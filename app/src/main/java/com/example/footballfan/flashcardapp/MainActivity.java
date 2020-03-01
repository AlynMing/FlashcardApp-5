package com.example.footballfan.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

        findViewById(R.id.addIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("is_editing", "false");
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });

        findViewById(R.id.editIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                String curQ = ((TextView) findViewById(R.id.questionText)).getText().toString();
                String a1 = ((TextView) findViewById(R.id.answerCorrect)).getText().toString();
                String a2 = ((TextView) findViewById(R.id.answerChoice1)).getText().toString();
                String a3 = ((TextView) findViewById(R.id.answerChoice2)).getText().toString();
                intent.putExtra("question", curQ);
                intent.putExtra("correctAnswer", a1);
                intent.putExtra("wrongAnswer1", a2);
                intent.putExtra("wrongAnswer2", a3);
                intent.putExtra("is_editing", "true");
                MainActivity.this.startActivityForResult(intent, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == 100) || (requestCode == 200))
        {
            if(!data.getExtras().containsKey("cancelled"))
            {
                String question = data.getExtras().getString("question");
                TextView q = (TextView) findViewById(R.id.questionText);
                q.setText(question);

                String correctAnswer = data.getExtras().getString("correctAnswer");
                String wrongAnswer1 = data.getExtras().getString("wrongAnswer1");
                String wrongAnswer2 = data.getExtras().getString("wrongAnswer2");
                TextView answer = (TextView) findViewById(R.id.answerText);
                answer.setText(correctAnswer);

                // place answer choices randomly
                TextView a1 = (TextView) findViewById(R.id.answerCorrect);
                a1.setText(correctAnswer);
                a1.setBackgroundColor(getResources().getColor(R.color.light_blue));
                TextView a2 = (TextView) findViewById(R.id.answerChoice1);
                a2.setText(wrongAnswer1);
                a2.setBackgroundColor(getResources().getColor(R.color.light_blue));
                TextView a3 = (TextView) findViewById(R.id.answerChoice2);
                a3.setText(wrongAnswer2);
                a3.setBackgroundColor(getResources().getColor(R.color.light_blue));

                ArrayList<TextView> answerChoices = new ArrayList<>(Arrays.asList(a1, a2, a3));
                Collections.shuffle(answerChoices);

                Snackbar.make(findViewById(R.id.questionText), "Card successfully created", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
