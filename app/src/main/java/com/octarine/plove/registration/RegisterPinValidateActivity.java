package com.octarine.plove.registration;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterPinValidateActivity extends AppCompatActivity {

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
                getNext(etLogin.getText().toString());
            }
        });


    }


    public void getNext(String code) {
        ApplicationController.getInstance().showLoading(RegisterPinValidateActivity.this);

        ApplicationController.getApi().registerActivate(AuthModel.getInstance(RegisterPinValidateActivity.this).sessionId, code).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    response.body().getAuthModel().save(RegisterPinValidateActivity.this);
                    if (response.body().code == 0) {
                        Intent i = new Intent(RegisterPinValidateActivity.this, RegisterStep3Activity.class);
                        startActivity(i);
                    } else {
                        ApplicationController.getInstance().hideLoading();
                        ApplicationController.showError(RegisterPinValidateActivity.this, response.body().message);
                    }
                } else {
                    ApplicationController.showError(RegisterPinValidateActivity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(RegisterPinValidateActivity.this, getString(R.string.connection_error));
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
