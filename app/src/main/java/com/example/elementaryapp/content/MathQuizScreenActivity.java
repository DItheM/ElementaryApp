package com.example.elementaryapp.content;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elementaryapp.R;
import com.example.elementaryapp.classes.Lesson;
import com.example.elementaryapp.services.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MathQuizScreenActivity extends AppCompatActivity {

    TextView num_1, num_2, expression, ans_1, ans_2, ans_3;
    CardView expression_bg, ans_1_bg, ans_2_bg, ans_3_bg;
    Button skip_btn;
    String correctAnswer;

    String[] types = {
            "+", "-", "/", "x"
    };

    Random random = new Random();
    private static final int TIMEOUT_DURATION = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz_screen);

        Services.onPressBack(this);

        num_1 = findViewById(R.id.num_1);
        num_2 = findViewById(R.id.num_2);
        expression = findViewById(R.id.expression);
        ans_1 = findViewById(R.id.ans_1);
        ans_2 = findViewById(R.id.ans_2);
        ans_3 = findViewById(R.id.ans_3);

        expression_bg = findViewById(R.id.expression_bg);
        ans_1_bg = findViewById(R.id.ans_1_bg);
        ans_2_bg = findViewById(R.id.ans_2_bg);
        ans_3_bg = findViewById(R.id.ans_3_bg);

        skip_btn = findViewById(R.id.skip_btn);
        skip_btn.setOnClickListener(v -> {
            createQuiz();
            Toast.makeText(this, "Skipped", Toast.LENGTH_SHORT).show();
        });

        createQuiz();
    }

    public int getRandomType() {
        return random.nextInt(4);
    }

    public int getRandomNumber(int bound) {
        return random.nextInt(bound);
    }

    public int generateFakeAnswers(int ref, int trueAnswer) {
        int ans = getRandomNumber(10);
        if (ref == -1 && ans != trueAnswer) {
            return ans;
        } else {
            while(ans == ref || ans == trueAnswer) {
                ans = getRandomNumber(10);
            }
            return ans;
        }
    }

    public void createQuiz() {
        addClickListeners();

        String type = types[getRandomType()];
        int n_1, n_2, trueAnswer, fake_ans_1, fake_ans_2;

        if (type.equals("+")) {
            n_1 = getRandomNumber(10);
            n_2 = getRandomNumber(10);
            trueAnswer = n_1 + n_2;
        } else if (type.equals("-")) {
            n_1 = getRandomNumber(10) + 1;
            n_2 = getRandomNumber(n_1);
            trueAnswer = n_1 - n_2;
        } else if (type.equals("/")) {
            n_1 = getRandomNumber(10);
            n_2 = random.nextInt(9) + 1;
            int remainder = n_1 % n_2;

            while (remainder != 0) {
                n_2 = random.nextInt(9) + 1;
                remainder = n_1 % n_2;
            }
            trueAnswer = n_1 / n_2;
        } else {
            n_1 = getRandomNumber(10);
            n_2 = getRandomNumber(10);
            trueAnswer = n_1 * n_2;
        }

        fake_ans_1 = generateFakeAnswers(-1, trueAnswer);
        fake_ans_2 = generateFakeAnswers(fake_ans_1, trueAnswer);

        showQuiz(n_1, type, n_2, trueAnswer, fake_ans_1, fake_ans_2);
        resetAnswers();
    }

    public void showQuiz(int n_1, String exp, int n_2, int trueAnswer, int fake_ans_1, int fake_ans_2) {
        num_1.setText(String.valueOf(n_1));
        num_2.setText(String.valueOf(n_2));
        expression.setText(exp);

        correctAnswer = String.valueOf(trueAnswer);

        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(trueAnswer));
        list.add(String.valueOf(fake_ans_1));
        list.add(String.valueOf(fake_ans_2));

        // Shuffle the list
        Collections.shuffle(list);
                
        ans_1.setText(list.get(0));
        ans_2.setText(list.get(1));
        ans_3.setText(list.get(2));
    }

    public void checkAnswer(TextView textView, CardView cardView, String answerName, String clickedAnswerName) {
        CharSequence val = textView.getText();
        if (val.equals(correctAnswer)) {
            cardView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.correct)));
        }
        if (answerName.equals(clickedAnswerName)) {
            if (!val.equals(correctAnswer)) {
                cardView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.incorrect)));
                changeBtn(false);
            } else {
                changeBtn(true);
            }
        }
    }

    public void checkAllAnswers(String clickedAnswerName) {
        checkAnswer(ans_1, ans_1_bg, "ans_1", clickedAnswerName);
        checkAnswer(ans_2, ans_2_bg, "ans_2", clickedAnswerName);
        checkAnswer(ans_3, ans_3_bg, "ans_3", clickedAnswerName);
        removeClickListeners();
        startTimeout();
    }

    public void changeBtn(Boolean isCorrect) {
        skip_btn.setEnabled(false);
        skip_btn.setTextColor(getResources().getColor(R.color.white));
        if (isCorrect) {
            skip_btn.setBackgroundColor(getResources().getColor(R.color.correct));
            skip_btn.setText("Correct!");
        } else {
            skip_btn.setBackgroundColor(getResources().getColor(R.color.incorrect));
            skip_btn.setText("Incorrect!");
        }

    }

    public void resetAnswers() {
        ans_1_bg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bgClr_1)));
        ans_2_bg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bgClr_1)));
        ans_3_bg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bgClr_1)));
        skip_btn.setBackgroundColor(getResources().getColor(R.color.btnBack));
        skip_btn.setText("Skip");
        skip_btn.setEnabled(true);
    }

    public void addClickListeners() {
        ans_1_bg.setOnClickListener(v -> checkAllAnswers("ans_1"));
        ans_2_bg.setOnClickListener(v -> checkAllAnswers("ans_2"));
        ans_3_bg.setOnClickListener(v -> checkAllAnswers("ans_3"));
    }

    public void removeClickListeners() {
        ans_1_bg.setOnClickListener(null);
        ans_2_bg.setOnClickListener(null);
        ans_3_bg.setOnClickListener(null);
    }

    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            stopTimeout();
            createQuiz();
        }
    };

    private void startTimeout() {
        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_DURATION);
    }

    private void stopTimeout() {
        timeoutHandler.removeCallbacks(timeoutRunnable);
    }



}