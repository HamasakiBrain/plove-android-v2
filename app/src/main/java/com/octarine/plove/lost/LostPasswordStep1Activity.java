package com.octarine.plove.lost;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostPasswordStep1Activity extends AppCompatActivity {

    MaskedEditText etLogin;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lost_password_step1);

        coordinatorLayout = findViewById(R.id.coordinator);
        etLogin =  findViewById(R.id.editTextLogin);

        Button btnNext = findViewById(R.id.buttonNext);

        btnNext.setOnClickListener(view -> {
            if (!checkPhone(etLogin.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_login);
            } else {
                recoveryPassword(etLogin.getText().toString());
            }
        });
    }

    public void recoveryPassword(String phone) {

        PreferenceManager.getDefaultSharedPreferences(LostPasswordStep1Activity.this)
                .edit()
                .putString("user_phone", phone)
                .apply();

        ApplicationController.getInstance().showLoading(LostPasswordStep1Activity.this);
        ApplicationController.getApi().recovery(phone).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().code == 0) {
                        ApplicationController.showError(LostPasswordStep1Activity.this, response.body().message);
                        Intent i = new Intent(LostPasswordStep1Activity.this, PinValidateActivity.class);
                        i.putExtra("phone", phone);
                        startActivity(i);
                    } else ApplicationController.showError(LostPasswordStep1Activity.this, response.body().message);
                } else ApplicationController.showError(LostPasswordStep1Activity.this, getString(R.string.connection_error));
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(LostPasswordStep1Activity.this, getString(R.string.connection_error));
            }
        });
    }

    public boolean checkPhone(String phone) {
        boolean result = true;
        if (phone.isEmpty()) result = false;
        if (phone.length() < 15) result = false;
        return result;
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
}
