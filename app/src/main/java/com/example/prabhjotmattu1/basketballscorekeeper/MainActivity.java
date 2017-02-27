package com.example.prabhjotmattu1.basketballscorekeeper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.icu.util.MeasureUnit;
import android.icu.util.TimeUnit;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.start;
import static com.example.prabhjotmattu1.basketballscorekeeper.R.id.quarter_number;
import static com.example.prabhjotmattu1.basketballscorekeeper.R.id.score2;
import static com.example.prabhjotmattu1.basketballscorekeeper.R.id.start_button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                points1 = 0;
                points2 = 0;
                timeouts1 = 3;
                timeouts2 = 3;
                fouls1 = 0;
                fouls2 = 0;
                quarter = 1;
                displayScore1(points1);
                displayScore2(points2);
                displayFouls1(fouls1);
                displayFouls2(fouls2);
                displayTimeouts1(timeouts1);
                displayTimeouts2(timeouts2);
                displayQuarter(quarter);
                resetTime();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Remove focus on EditText when touch outside of textbox

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    int points1 = 0;
    int points2 = 0;
    int timeouts1 = 6;
    int timeouts2 = 6;
    int fouls1 = 0;
    int fouls2 = 0;
    int quarter = 1;
    boolean timerStarted = false;
    long s1 = 0;
    CountDownTimer timeLeft;
    boolean isStartEnabled = true;
    boolean isPauseEnabled = false;

    public void vibrateDevice() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public void threePointer(View view) {
        if (view.getId() == R.id.three_button1) {
            points1 += 3;
            displayScore1(points1);
        }

        else if (view.getId() == R.id.three_button2) {
            points2 += 3;
            displayScore2(points2);
        }

    }

    public void twoPointer(View view) {
        if (view.getId() == R.id.two_button1) {
            points1 += 2;
            displayScore1(points1);
        }

        else if (view.getId() == R.id.two_button2) {
            points2 += 2;
            displayScore2(points2);
        }
    }

    public void freeThrow(View view) {
        if (view.getId() == R.id.freethrow_button1) {
            points1 += 1;
            displayScore1(points1);
        }

        else if (view.getId() == R.id.freethrow_button2) {
            points2 += 1;
            displayScore2(points2);
        }
    }

    public void timeoutTaken(View view) {
        if (view.getId() == R.id.timeout_button1) {
            if (timeouts1 > 0) {
                timeouts1 -= 1;
                displayTimeouts1(timeouts1);
                vibrateDevice();
            }
            else {
                Toast.makeText(this, "No timeouts remaining", Toast.LENGTH_SHORT).show();
            }
        }

        else if (view.getId() == R.id.timeout_button2) {
            if (timeouts2 > 0) {
                timeouts2 -= 1;
                displayTimeouts2(timeouts2);
                vibrateDevice();
            }
            else {
                Toast.makeText(this, "No timeouts remaining", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void foulCommitted(View view) {
        if (view.getId() == R.id.foul_button1) {
            fouls1 += 1;
            displayFouls1(fouls1);
            vibrateDevice();
        }

        else if (view.getId() == R.id.foul_button2) {
            fouls2 += 1;
            displayFouls2(fouls2);
            vibrateDevice();
        }
    }

    public void startTimer(View view) {
        if (isStartEnabled == false) {
            Toast.makeText(this, "Game is already started", Toast.LENGTH_SHORT).show();
        }
        else if (isStartEnabled == true) {

            isStartEnabled = false;
            isPauseEnabled = true;

            if (!timerStarted && quarter < 4) {
                vibrateDevice();
                timerStarted = true;
                timeLeft = new CountDownTimer(360000, 1000) {
                    TextView timerView = (TextView) findViewById(R.id.time_left);

                    @Override
                    public void onTick(long millisUntilFinished) {
                        s1 = millisUntilFinished;
                        long durationSeconds = millisUntilFinished / 1000;

                        timerView.setText(String.format("%02d:%02d", (durationSeconds % 3600) / 60, (durationSeconds % 60)));
                    }

                    @Override
                    public void onFinish() {
                        timerView.setText("Finish");
                        timerStarted = false;
                        quarter += 1;
                        if (quarter <=4) {
                            displayQuarter(quarter);
                        }
                    }
                }.start();
            }

            else if (timerStarted && quarter < 4) {
                vibrateDevice();
                timeLeft = new CountDownTimer(s1, 1000) {
                    TextView timerView = (TextView) findViewById(R.id.time_left);
                    @Override
                    public void onTick(long millisUntilFinished) {
                        s1 = millisUntilFinished;
                        long durationSeconds = millisUntilFinished / 1000;

                        timerView.setText(String.format("%02d:%02d", (durationSeconds % 3600) / 60, (durationSeconds % 60)));
                    }

                    @Override
                    public void onFinish() {
                        timerView.setText("Finish");
                        timerStarted = false;
                        quarter += 1;
                        if (quarter <=4) {
                            displayQuarter(quarter);
                        }
                    }
                }.start();
            }

            else if (quarter == 5) {
                Toast.makeText(this, "Game is over, reset the game.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void pauseTimer(View view) {
        if (isPauseEnabled == false) {
            Toast.makeText(this, "Game is already paused", Toast.LENGTH_SHORT).show();
        }
        else if (isPauseEnabled == true){
            vibrateDevice();
            isPauseEnabled = false;
            isStartEnabled = true;
            timeLeft.cancel();
        }
    }

    public void displayScore1(int number) {
        TextView scoreView = (TextView) findViewById(R.id.score1);
        scoreView.setText(String.valueOf(number));
    }

    public void displayScore2(int number) {
        TextView scoreView = (TextView) findViewById(R.id.score2);
        scoreView.setText(String.valueOf(number));
    }

    public void displayTimeouts1(int number) {
        TextView timeoutView = (TextView) findViewById(R.id.timeout_number1);
        timeoutView.setText(String.valueOf(number));
    }

    public void displayTimeouts2(int number) {
        TextView timeoutView = (TextView) findViewById(R.id.timeout_number2);
        timeoutView.setText(String.valueOf(number));
    }

    public void displayFouls1(int number) {
        TextView foulView = (TextView) findViewById(R.id.foul_number1);
        foulView.setText(String.valueOf(number));
    }

    public void displayFouls2(int number) {
        TextView foulView = (TextView) findViewById(R.id.foul_number2);
        foulView.setText(String.valueOf(number));
    }

    public void displayQuarter(int number) {
        TextView quarterView = (TextView) findViewById(quarter_number);
        quarterView.setText(String.valueOf(number));
    }

    public void resetTime() {
        TextView timerView = (TextView) findViewById(R.id.time_left);
        timeLeft.cancel();
        timerStarted = false;
        timerView.setText("06:00");
    }
}
