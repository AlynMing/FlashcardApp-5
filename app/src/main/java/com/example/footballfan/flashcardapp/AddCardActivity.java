package com.example.footballfan.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findViewById(R.id.removeIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.saveIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();//AddCardActivity.this, MainActivity.class);
                String question = ((EditText) findViewById(R.id.questionInput)).getText().toString();
                String answer = ((EditText) findViewById(R.id.answerInput)).getText().toString();
                intent.putExtra("question", question);
                intent.putExtra("answer", answer);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
