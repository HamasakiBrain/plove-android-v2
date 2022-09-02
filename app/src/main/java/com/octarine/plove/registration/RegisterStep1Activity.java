package com.octarine.plove.registration;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.octarine.plove.lost.PinValidateActivity;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterStep1Activity extends AppCompatActivity {

    MaskedEditText etLogin;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_step1);

        coordinatorLayout = findViewById(R.id.coordinator);
        etLogin = findViewById(R.id.editTextLogin);

        if(etLogin.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        Button btnNext = findViewById(R.id.buttonNext);

        btnNext.setOnClickListener(view -> {
            if (!checkPhone(etLogin.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_login);
            } else {
                registerAction(etLogin.getText().toString());
            }

        });

    }

    public boolean checkPhone(String phone) {
        boolean result = !phone.isEmpty();
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



    @Override
    public void onBackPressed()
    {
        //ApplicationController.getInstance().register_phone= "";
        super.onBackPressed();  // optional depending on your needs
    }

    private void registerAction(final String login)  {

        PreferenceManager.getDefaultSharedPreferences(RegisterStep1Activity.this)
                .edit()
                .putString("user_phone", login)
                .apply();

        ApplicationController.getInstance().showLoading(RegisterStep1Activity.this);
        ApplicationController.getApi().register(login).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().code == 0 || response.body().code == 502) {
                        response.body().getAuthModel().save(RegisterStep1Activity.this);
                        Intent i = new Intent(RegisterStep1Activity.this, RegisterPinValidateActivity.class);
                        startActivity(i);
                    } else {
                        if (response.body().code == 700) {
                            ApplicationController.showError(RegisterStep1Activity.this, response.body().message);
                            Intent i = new Intent(RegisterStep1Activity.this, RegisterLinkCardActivity.class);
                            i.putExtra("phone", login);
                            startActivity(i);
                        } else {
                            if (response.body().code == 600) {
                                //ApplicationController.showError(RegisterStep1Activity.this, response.body().message);
                                Intent i = new Intent(RegisterStep1Activity.this, PinValidateActivity.class);
                                i.putExtra("phone", login);
                                startActivity(i);
                            } else
                            ApplicationController.showError(RegisterStep1Activity.this, response.body().message);
                        }
                    }
                } else {
                    ApplicationController.showError(RegisterStep1Activity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(RegisterStep1Activity.this, getString(R.string.connection_error));
            }
        });
    }
}
