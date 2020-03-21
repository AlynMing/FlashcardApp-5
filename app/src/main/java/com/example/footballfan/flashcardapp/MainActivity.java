package com.example.footballfan.flashcardapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean showingAnswers = true;
    private FlashcardDatabase flashcardDB;
    private List<Flashcard> flashcards;
    private Flashcard cardEditing = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDB = new FlashcardDatabase(getApplicationContext());
        flashcards = flashcardDB.getAllCards();
        if((flashcards != null) && (flashcards.size() > 0))
        {
            int cardIdx = getRandNum(0, flashcards.size() - 1);
            ((TextView) findViewById(R.id.questionText)).setText(flashcards.get(cardIdx).getQuestion());
            ((TextView) findViewById(R.id.answerText)).setText(flashcards.get(cardIdx).getAnswer());
            ((TextView) findViewById(R.id.answerCorrect)).setText(flashcards.get(cardIdx).getAnswer());
            ((TextView) findViewById(R.id.answerChoice1)).setText(flashcards.get(cardIdx).getWrongAnswer1());
            ((TextView) findViewById(R.id.answerChoice2)).setText(flashcards.get(cardIdx).getWrongAnswer2());
        }

        findViewById(R.id.questionText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View questionSideView = findViewById(R.id.questionText);
                questionSideView.setVisibility(View.INVISIBLE);
                View answerSideView = findViewById(R.id.answerText);
                answerSideView.setVisibility(View.VISIBLE);

                // transition to display answer: circular

                // get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);
                anim.setDuration(1000);
                anim.start();
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                cardEditing = findCard(curQ);
                MainActivity.this.startActivityForResult(intent, 200);
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);  // remove old card
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);  // show next card

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        if(flashcards.size() > 0) {
                            showNextCard();
                        }
                        else {
                            showEmptyState();
                        }
                        findViewById(R.id.questionText).startAnimation(rightInAnim);  // show next card
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });



                findViewById(R.id.questionText).startAnimation(leftOutAnim);
            }
        });

        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDB.deleteCard(((TextView) findViewById(R.id.questionText)).getText().toString());
                flashcards = flashcardDB.getAllCards();
                // if any cards left in database, show next card; otherwise, show "empty state"
                if(flashcards.size() > 0) {
                    showNextCard();
                }
                else {
                    showEmptyState();
                }
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

                if(cardEditing == null) {
                    // creating new flashcard
                    flashcardDB.insertCard(new Flashcard(question, correctAnswer, wrongAnswer1, wrongAnswer2));
                }
                else {
                    // editing existing flashcard
                    cardEditing.setQuestion(question);
                    cardEditing.setAnswer(correctAnswer);
                    cardEditing.setWrongAnswer1(wrongAnswer1);
                    cardEditing.setWrongAnswer2(wrongAnswer2);
                    flashcardDB.updateCard(cardEditing);
                    cardEditing = null;
                }
                flashcards = flashcardDB.getAllCards();

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

    private void resetView()
    {
        // show flashcard with question side up, answer on back
        if(findViewById(R.id.questionText).getVisibility() == View.INVISIBLE) {
            findViewById(R.id.questionText).setVisibility(View.VISIBLE);
            findViewById(R.id.answerText).setVisibility(View.INVISIBLE);
        }
    }

    private void showNextCard()
    {
        int cardIdx = getRandNum(0, flashcards.size() - 1);
        ((TextView) findViewById(R.id.questionText)).setText(flashcards.get(cardIdx).getQuestion());
        ((TextView) findViewById(R.id.answerText)).setText(flashcards.get(cardIdx).getAnswer());
        ((TextView) findViewById(R.id.answerCorrect)).setText(flashcards.get(cardIdx).getAnswer());
        ((TextView) findViewById(R.id.answerChoice1)).setText(flashcards.get(cardIdx).getWrongAnswer1());
        ((TextView) findViewById(R.id.answerChoice2)).setText(flashcards.get(cardIdx).getWrongAnswer2());
        resetView();
    }

    private void showEmptyState()
    {
        ((TextView) findViewById(R.id.questionText)).setText("Your flashcard deck is currently empty :(");
        ((TextView) findViewById(R.id.answerText)).setText("Add a card!");
        ((TextView) findViewById(R.id.answerCorrect)).setText("");
        ((TextView) findViewById(R.id.answerChoice1)).setText("");
        ((TextView) findViewById(R.id.answerChoice2)).setText("");
        resetView();
    }

    private int getRandNum(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private Flashcard findCard(String question)
    {
        for(Flashcard card : flashcards)
        {
            if(card.getQuestion().equals(question))
            {
                return card;
            }
        }
        return flashcards.get(0);
    }
}
