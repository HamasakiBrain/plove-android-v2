package com.octarine.plove.auth;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.ProfileResponseModel;
import com.octarine.plove.lost.LostPasswordStep1Activity;
import com.octarine.plove.registration.RegisterStep1Activity;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    MaskedEditText etLogin;
    EditText etPassword;
    CoordinatorLayout coordinatorLayout;

    public void getProfile() {
        ApplicationController.getInstance().showLoading(AuthActivity.this);
        ApplicationController.getApi().profile(AuthModel.getInstance(AuthActivity.this).sessionId).enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                Log.d("LoginProfile", response.raw().toString());
                ApplicationController.getInstance().hideLoading();
                response.body().getProfileModel().save(AuthActivity.this);
                Intent i = new Intent(AuthActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Log.d("LoginProfile", t.toString());
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(AuthActivity.this, getString(R.string.connection_error));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_auth);

        // Если авторизован то кидаем на главную
        if (AuthModel.getInstance(AuthActivity.this).sessionId != null &&
                !AuthModel.getInstance(AuthActivity.this).sessionId.isEmpty()) {
            Intent i = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(i);
        }

        coordinatorLayout = findViewById(R.id.coordinator);
        etLogin = findViewById(R.id.editTextLogin);
        etPassword = findViewById(R.id.editTextPassword);

        Button btnAuth = findViewById(R.id.buttonAuth);

        btnAuth.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etLogin.getText())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_login);
            } else {
                if (TextUtils.isEmpty(etPassword.getText())) {
                    ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_password);
                } else {
                    ApplicationController.getInstance().showLoading(AuthActivity.this);
                    ApplicationController.getApi().auth(etLogin.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<AuthResponseModel>() {
                        @Override
                        public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                            AuthResponseModel authResponseModel = response.body();
                            Log.d("TAG", response.raw().toString());
                            if (response.isSuccessful()) {
                                response.body().getAuthModel().save(AuthActivity.this);
                                Log.d("Login", response.body().toString());
                                if (authResponseModel.code == 0) {
                                    getProfile();
                                } else {
                                    ApplicationController.getInstance().hideLoading();
                                    ApplicationController.showError(AuthActivity.this, authResponseModel.message);
                                }
                            } else {
                                ApplicationController.getInstance().hideLoading();
                                ApplicationController.showError(AuthActivity.this, getString(R.string.connection_error));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                            ApplicationController.getInstance().hideLoading();
                            ApplicationController.showError(AuthActivity.this, getString(R.string.connection_error));
                            ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.connection_error);
                        }
                    });
                }
            }
        });

        Button btnRegister = findViewById(R.id.buttonReg);
        btnRegister.setOnClickListener(view -> {
            Intent i = new Intent(AuthActivity.this, RegisterStep1Activity.class);
            startActivity(i);
        });

        Button buttonLostPassword = findViewById(R.id.textViewLostPassword);
        buttonLostPassword.setOnClickListener(view -> {
            Intent i = new Intent(AuthActivity.this, LostPasswordStep1Activity.class);
            startActivity(i);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
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
