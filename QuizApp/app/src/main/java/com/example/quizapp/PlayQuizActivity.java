package com.example.quizapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.ConfirmationPrompt;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayQuizActivity extends AppCompatActivity {

    private TextView questionTextView, questionAttemptedTextView, quizFinalScoreTextView;
    private Button option01Button, option02Button, option03Button, option04Button, correctOptionButton;
    private Button restartQuizButton, endQuizButton;

    private int delay = 1000;

    ArrayList<QuizDataModel> quizDataModelArrayList;

    private int correctAnswers = 0, wrongAnswers = 0;
    private int questionAttempted = 1;
    private int currentPosition = 0;

    private boolean isQuizFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        questionAttemptedTextView = findViewById(R.id.questionsAttemptedID);
        questionTextView = findViewById(R.id.questionID);

        option01Button = findViewById(R.id.option01ID);
        option02Button = findViewById(R.id.option02ID);
        option03Button = findViewById(R.id.option03ID);
        option04Button = findViewById(R.id.option04ID);

        quizFinalScoreTextView = findViewById(R.id.quizScoreID);

        restartQuizButton = findViewById(R.id.restartQuizButtonID);
        endQuizButton = findViewById(R.id.endQuizButtonID);

        restartQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PlayQuizActivity.this)
                        .setTitle("Wanna play this quiz again?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initializeValues();
                                setDataToViews(currentPosition);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        endQuizButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
//                initializeValues();

                new AlertDialog.Builder(PlayQuizActivity.this)
                        .setTitle("Do you really want to quit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PlayQuizActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                }
//                Toast toast=Toast.makeText(getApplicationContext(),"Quiz Ended!",Toast.LENGTH_LONG);
//                toast.setMargin(50,50);
//                toast.show();
        });

        quizFinalScoreTextView.setText(new StringBuilder().append("Current Score: ").append(correctAnswers).toString());

        quizDataModelArrayList = getQuestionsAsModelArrayList();

        addOnClickListenerToButtons(option01Button);
        addOnClickListenerToButtons(option02Button);
        addOnClickListenerToButtons(option03Button);
        addOnClickListenerToButtons(option04Button);

        restartQuizButton.setVisibility(View.GONE);
        endQuizButton.setVisibility(View.GONE);

        setDataToViews(currentPosition);

    }

    private ArrayList<QuizDataModel> getQuestionsAsModelArrayList() {
        ArrayList<QuizDataModel> buildQuizDataModelArrayList = new ArrayList<>();

        buildQuizDataModelArrayList.add(new QuizDataModel("What is the full Kalima-e-Shahaadah?", "Laa Ilaaha Illallaah", "Laa Ilaaha Illallaahu Muhammaddar-Rasoolullaah", "Ash-hadu Allaa Ilaaha Illallaahu Muhammadur-Rasoolullaah", "All of the above", "Ash-hadu Allaa Ilaaha Illallaahu Muhammadur-Rasoolullaah"));
        buildQuizDataModelArrayList.add(new QuizDataModel("How many Surahs there in The Holy Quran?", "112", "115", "113", "114", "114"));
        buildQuizDataModelArrayList.add(new QuizDataModel("What is the longest Surah of The Glorious Quran?", "Surah Kauthar", "Surah Ikhlaas", "Surah Baqarah", "Surah Ale 'Imran", "Surah Baqarah"));
        buildQuizDataModelArrayList.add(new QuizDataModel("How many Ayahs there in Surah Fatiha?", "7", "8", "5", "6", "7"));
        buildQuizDataModelArrayList.add(new QuizDataModel("What is the shortest Surah of The Glorious Quran?", "Surah Kauthar", "Surah Ikhlaas", "Surah Baqarah", "Surah Ale 'Imran", "Surah Kauthar"));

        return buildQuizDataModelArrayList;
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViews(int currentPosition){
        if(isQuizFinished) {
            currentPosition = quizDataModelArrayList.size()-1;
            questionAttempted = quizDataModelArrayList.size();

            setOptionButtonsClickableState(option01Button, false);
            setOptionButtonsClickableState(option02Button, false);
            setOptionButtonsClickableState(option03Button, false);
            setOptionButtonsClickableState(option04Button, false);

            quizFinalScoreTextView.setText(new StringBuilder()
                    .append("Quiz Finished!!!\n")
                    .append("Your Scored ")
                    .append(correctAnswers)
                    .append(" out of 5")
                    .toString());
            quizFinalScoreTextView.setVisibility(View.VISIBLE);
            restartQuizButton.setVisibility(View.VISIBLE);
            endQuizButton.setVisibility(View.VISIBLE);
        }
        else {
            questionAttemptedTextView.setText("Questions Attempted: " + questionAttempted + "/5");

            questionTextView.setText(quizDataModelArrayList.get(currentPosition).getQuestion().toString());

            option01Button.setText((quizDataModelArrayList.get(currentPosition).getOption01().toString()));
            option02Button.setText((quizDataModelArrayList.get(currentPosition).getOption02().toString()));
            option03Button.setText((quizDataModelArrayList.get(currentPosition).getOption03().toString()));
            option04Button.setText((quizDataModelArrayList.get(currentPosition).getOption04().toString()));

            quizFinalScoreTextView.setText(new StringBuilder().append("Current Score: ").append(correctAnswers).toString());
        }
    }

    private void addOnClickListenerToButtons(Button optionButton) {
        optionButton.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View v){
                // First find the Option Button with the Correct Answer
                correctOptionButton = findCorrectOptionButton();

                // If the answer is Correct
                if(quizDataModelArrayList.get(currentPosition).getAnswer().trim().toLowerCase().equals(optionButton.getText().toString().trim().toLowerCase())){
                    correctAnswers++;
                }
                // Else the answer is Wrong
                else{
                    wrongAnswers++;
                    optionButton.setBackgroundColor(optionButton.getContext().getResources().getColor(R.color.red));
                }

                correctOptionButton.setBackgroundColor(optionButton.getContext().getResources().getColor(R.color.green));

                quizFinalScoreTextView.setText(new StringBuilder().append("Current Score: ").append(correctAnswers).toString());
                questionAttempted++;
                currentPosition++;

                // If we've reached to the last question of the list
                if(currentPosition == quizDataModelArrayList.size()){
                    isQuizFinished = true;
                }

                // Assign a delay for the user to see if the answer was correct or not visually
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Change the Option Buttons backgrounds to their previous color
                        correctOptionButton.setBackgroundColor(correctOptionButton.getContext().getResources().getColor(R.color.purple_500));
                        optionButton.setBackgroundColor(optionButton.getContext().getResources().getColor(R.color.purple_500));

                        // And move to the next question
                        setDataToViews(currentPosition);
                    }
                }, delay);
            }

            private Button findCorrectOptionButton() {
                String answer = quizDataModelArrayList.get(currentPosition).getAnswer().toString().trim().toLowerCase();

                if(option01Button.getText().toString().trim().toLowerCase().equals(answer)){
                    return option01Button;
                }
                else if(option02Button.getText().toString().trim().toLowerCase().equals(answer)){
                    return option02Button;
                }
                else if(option03Button.getText().toString().trim().toLowerCase().equals(answer)){
                    return option03Button;
                }
                return option04Button;
            }
        });
    }

    // A method to change the Button's Clickable state
    private void setOptionButtonsClickableState(Button optionButton, boolean state) {
        optionButton.setClickable(state);
    }

    private void initializeValues() {
        correctAnswers = 0;
        questionAttempted = 1;
        currentPosition = 0;
        delay = 1000;
        isQuizFinished = false;

        quizFinalScoreTextView.setText("");

        setOptionButtonsClickableState(option01Button, true);
        setOptionButtonsClickableState(option02Button, true);
        setOptionButtonsClickableState(option03Button, true);
        setOptionButtonsClickableState(option04Button, true);

        restartQuizButton.setVisibility(View.GONE);
        endQuizButton.setVisibility(View.GONE);
    }
    
}