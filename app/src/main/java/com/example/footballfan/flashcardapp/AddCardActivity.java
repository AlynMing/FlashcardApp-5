package com.example.footballfan.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {
    private boolean editing = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(getIntent().getExtras().containsKey("question")) {
            // editing card so fill in current question and answer choices
            String question = getIntent().getStringExtra("question");
            TextView q = (TextView) findViewById(R.id.questionInput);
            q.setText(question);

            String correctAnswer = getIntent().getStringExtra("correctAnswer");
            TextView a1 = (TextView) findViewById(R.id.answerInputCorrect);
            a1.setText(correctAnswer);

            String wrongAnswer1 = getIntent().getStringExtra("wrongAnswer1");
            TextView a2 = (TextView) findViewById(R.id.answerInputWrong1);
            a2.setText(wrongAnswer1);

            String wrongAnswer2 = getIntent().getStringExtra("wrongAnswer2");
            TextView a3 = (TextView) findViewById(R.id.answerInputWrong2);
            a3.setText(wrongAnswer2);
        }

        String is_editing = getIntent().getStringExtra("is_editing");
        if(is_editing.equals("true"))
            editing = true;
        else  // "false"
            editing = false;

        findViewById(R.id.removeIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("cancelled", "true");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.saveIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = ((EditText) findViewById(R.id.questionInput)).getText().toString();
                String correctAnswer = ((EditText) findViewById(R.id.answerInputCorrect)).getText().toString();
                String wrongAnswer1 = ((EditText) findViewById(R.id.answerInputWrong1)).getText().toString();
                String wrongAnswer2 = ((EditText) findViewById(R.id.answerInputWrong2)).getText().toString();
                if(question.equals("") || correctAnswer.equals("") || wrongAnswer1.equals("") || wrongAnswer2.equals("")){
                    // show error message in toast if either question or answer is empty
                   Toast errMsg = Toast.makeText(getApplicationContext(), "Must enter question and all answer choices", Toast.LENGTH_SHORT);
                   errMsg.setGravity(Gravity.CENTER_VERTICAL, 0, 600);
                   errMsg.show();
                }
                else {
                    Intent intent;
                    if (getEditing()) {
                        intent = new Intent(AddCardActivity.this, MainActivity.class);

                    } else {
                        intent = new Intent();
                    }
                    intent.putExtra("question", question);
                    intent.putExtra("correctAnswer", correctAnswer);
                    intent.putExtra("wrongAnswer1", wrongAnswer1);
                    intent.putExtra("wrongAnswer2", wrongAnswer2);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private boolean getEditing()
    {
        return editing;
    }
}
