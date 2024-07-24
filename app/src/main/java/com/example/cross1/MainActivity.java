package com.example.cross1;

import static com.example.cross1.R.*;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private Button retryButton;
    private TextView countdownTimerText;
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 60000; // 2 minutes in milliseconds0000; // 1 minute
    private GridLayout crosswordGrid;
    private char[][] crossword = new char[10][10];
    private char[][] solution = new char[10][10]; // Store the solution for validation
    private EditText[][] editTexts = new EditText[10][10];
    private Random random = new Random();

    private TextView randomMessageTextView;
    private Handler handler = new Handler();
    private String[] randomMessages = {

            "A programming language developed by Microsoft.",
            "A method for storing data on punched cards, widely used in early computers.",
            "An ancient rock fortress in Sri Lanka, a UNESCO World Heritage site.",
            "A procedural programming language named after a French mathematician.",
            "The central unit of a computer, which performs operations.",
            "A cloud computing service provided by Microsoft.",
            "The capital city of England.",
            "An operating system for mobile devices developed by Google.",
            "A mechanism that initiates an action based on a specific condition.",
            "An open-source electronics platform based on easy-to-use hardware and software."

    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crosswordGrid = findViewById(R.id.crosswordGrid);
        randomMessageTextView = findViewById(R.id.randomMessageTextView);
        countdownTimerText = findViewById(R.id.countdown_timer);
        retryButton = findViewById(id.retryButton);

        // Generate and initialize the crossword puzzle
        generateCrossword();
        initializeCrossword();
        scheduleRandomMessageUpdate();
        startCountdownTimer();
        checkAllWordsFilled();


    }

    //After game finished this visible the retry button
    public void Retry ()
    {
        retryButton.setVisibility(View.VISIBLE);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    private void scheduleRandomMessageUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Generate a random message
                int index = random.nextInt(randomMessages.length);
                String message = randomMessages[index];
                String hint = "🎈Hint:"; // Add a hint prefix
                // Update TextView with random message
                randomMessageTextView.setText(hint +message);

                // Schedule the next update after 10 seconds
                handler.postDelayed(this, 10000); // 10 seconds
            }
        }, 10000); // Initial delay of 10 seconds
    }

    private void generateCrossword() {
        String[] words = {
                "CSHARP", "PUNCHCARD", "SEEGIRIYA", "PASCAL", "PROCESSOR", "AZURE",
                "LONDON", "ANDROID", "TRIGGER", "ARDUINO"
        };

        // Fill crossword grid with words
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                crossword[i][j] = ' ';
            }
        }

        int wordIndex = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (wordIndex < words.length) {
                    String word = words[wordIndex];
                    if (j + word.length() <= 10) {
                        for (int k = 0; k < word.length(); k++) {
                            crossword[i][j + k] = word.charAt(k);
                            solution[i][j + k] = word.charAt(k);
                        }
                        j += word.length() - 1; // Move to the next position after the word
                        wordIndex++;
                    }
                }
            }
        }

        // Randomly blank out letters
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (crossword[i][j] != ' ' && random.nextBoolean()) {
                    crossword[i][j] = ' ';
                }
            }
        }
    }

    private void initializeCrossword() {
        // Clear any existing views
        crosswordGrid.removeAllViews();

        // Set up the crossword grid dynamically
        int boxSize = calculateBoxSize(); // Calculate box size based on screen dimensions or fixed value

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                EditText editText = new EditText(this);
                editText.setGravity(Gravity.CENTER);
                editText.setTextSize(18);
                editText.setPadding(8, 8, 8, 8);
                editText.setBackgroundResource(R.drawable.grid_boder);
                editText.setMaxLines(1); // Allow only one line of input

                if (crossword[i][j] != ' ') {
                    // Display pre-filled characters
                    editText.setText(String.valueOf(crossword[i][j]));
                    editText.setEnabled(false); // Disable editing for pre-filled characters
                } else {
                    // Set up for user input (blank space)
                    editText.setText("");
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); // Limit input to one character
                    addTextWatcher(editText, i, j); // Add TextWatcher for user input
                }

                editTexts[i][j] = editText;
                crosswordGrid.addView(editText);

                // Set fixed dimensions for each EditText
                GridLayout.LayoutParams params = (GridLayout.LayoutParams) editText.getLayoutParams();
                params.width = boxSize;
                params.height = boxSize;
                editText.setLayoutParams(params);
            }
        }
    }

    private void addTextWatcher(EditText editText, final int row, final int col) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear previous formatting
                editText.setTextColor(Color.BLACK);
                editText.setBackgroundResource(R.drawable.grid_boder);

                if (s.length() > 0) {
                    char inputChar = s.charAt(0);
                    if (inputChar == solution[row][col]) {
                        editText.setTextColor(Color.GREEN);
                        editText.setBackgroundResource(R.drawable.grid_border_correct);
                    } else {
                        editText.setTextColor(Color.RED);
                        editText.setBackgroundResource(R.drawable.grid_border_incorrect);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private int calculateBoxSize() {
        // Calculate the box size based on screen dimensions or a fixed value
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int minScreenSize = Math.min(screenWidth, screenHeight);
        int padding = 16; // Adjust padding as needed
        int gridSize = 10; // Size of the crossword grid (10x10)

        return (minScreenSize - 2 * padding) / gridSize;

    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                countdownTimerText.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                countdownTimerText.setText("Time Up You Failed");
                fillGridWithAsterisks();
            }
        }.start();
    }

    private void fillGridWithAsterisks() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (editTexts[i][j].isEnabled()) {
                    editTexts[i][j].setText("*");
                    editTexts[i][j].setEnabled(false);
                }
            }
        }

        Gamesounds();
        Retry();
    }

    private void Gamesounds(){

        MediaPlayer fail = MediaPlayer.create(this, R.raw.fail);
        fail.start();
    }


    private void checkAllWordsFilled() {
        boolean allWordsFilled = true;

        // Iterate through the crossword grid to check user inputs against the solution
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (crossword[i][j] == ' ') {
                    // If any user input doesn't match the solution, set allWordsFilled to false
                    String userInput = editTexts[i][j].getText().toString();
                    if (!userInput.equalsIgnoreCase(String.valueOf(solution[i][j]))) {
                        allWordsFilled = false;
                        break;
                    }
                }
            }
            if (!allWordsFilled) {
                break;
            }
        }

        // If all words are filled correctly, update the timer text to "You win!"
        if (allWordsFilled) {
            countdownTimerText.setText("Congratulations! You win!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

