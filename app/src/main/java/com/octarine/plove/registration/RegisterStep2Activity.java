package com.octarine.plove.registration;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;
import java.util.Timer;
import java.util.TimerTask;


public class RegisterStep2Activity extends AppCompatActivity {

    MaskedEditText etLogin;
    CoordinatorLayout coordinatorLayout;
    String session_id;
    int countDownTime = 0;
    TextView buttonTicker;
    Button buttonResent;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_step2);

        buttonTicker = findViewById(R.id.buttonTiker);
        buttonTicker.setText("00:00");
        buttonResent = findViewById(R.id.buttonResent);
        buttonResent.setTextColor(Color.GRAY);
        buttonResent.setEnabled(false);
        buttonResent.setOnClickListener(view -> {
            Intent i = new Intent(RegisterStep2Activity.this, RegisterStep1Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        coordinatorLayout = findViewById(R.id.coordinator);
        etLogin = findViewById(R.id.editTextLogin);

        final Button btnNext = findViewById(R.id.buttonNext);

        btnNext.setOnClickListener(view -> {
            if (!checkCode(etLogin.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_code);
            } else {
                activateAction(etLogin.getText().toString(), session_id);
            }

        });

        countDownTime = 10;
        timer = new Timer();
        TimerTask updateBall = new UpdateTimerTask();
        timer.scheduleAtFixedRate(updateBall, 0, 1000);
    }

    public boolean checkCode(String code) {
        boolean result = true;
        if (code.isEmpty()) result = false;
        if (code.length() < 4) result = false;
        return result;
    }

    private class UpdateTimerTask extends TimerTask {
        public void run() {
            synchronized (this) {
                runOnUiThread(() -> {
                    if (countDownTime > 0) {
                        countDownTime--;
                        int minutes = countDownTime / 60;
                        int seconds = countDownTime % 60;
                    buttonTicker.setText(String.format("%02d:%02d", minutes, seconds));
                    } else {
                        buttonResent.setEnabled(true);
                        buttonResent.setTextColor(Color.BLACK);
                        timer.cancel();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_auth, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void activateAction(final String code, final String session_id) {



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
