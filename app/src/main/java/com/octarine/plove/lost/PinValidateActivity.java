package com.octarine.plove.lost;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.ProfileResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PinValidateActivity extends AppCompatActivity {

    MaskedEditText etLogin;
    CoordinatorLayout coordinatorLayout;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lost_password_step2);
        coordinatorLayout = findViewById(R.id.coordinator);
        etLogin =  findViewById(R.id.editTextLogin);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(view -> {
            if (!checkCode(etLogin.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_code);
            } else {
                String phone = PreferenceManager.getDefaultSharedPreferences(PinValidateActivity.this).getString("user_phone", "");
                getAuth(phone, etLogin.getText().toString());
            }
        });


    }

    public void getProfile() {
        ApplicationController.getInstance().showLoading(PinValidateActivity.this);
        ApplicationController.getApi().profile(AuthModel.getInstance(PinValidateActivity.this).sessionId).enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                Log.d("LoginProfile", response.raw().toString());
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    response.body().getProfileModel().save(PinValidateActivity.this);
                    Intent i = new Intent(PinValidateActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    ApplicationController.getInstance().hideLoading();
                    ApplicationController.showError(PinValidateActivity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Log.d("LoginProfile", t.toString());
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(PinValidateActivity.this, getString(R.string.connection_error));
            }
        });
    }

    public void getAuth(String login, String password) {
        ApplicationController.getInstance().showLoading(PinValidateActivity.this);
        ApplicationController.getApi().auth(login, password).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                AuthResponseModel authResponseModel = response.body();
                response.body().getAuthModel().save(PinValidateActivity.this);
                Log.d("Login", response.body().toString());
                if (authResponseModel.code == 0) {
                    getProfile();
                } else {
                    ApplicationController.getInstance().hideLoading();
                    ApplicationController.showError(PinValidateActivity.this, authResponseModel.message);
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(PinValidateActivity.this, getString(R.string.connection_error));
            }
        });
    }

    public boolean checkCode(String code) {
        boolean result = true;
        if (code.isEmpty()) result = false;
        if (code.length() < 4) result = false;
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
