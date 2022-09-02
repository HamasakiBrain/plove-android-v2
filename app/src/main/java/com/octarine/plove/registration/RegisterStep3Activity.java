package com.octarine.plove.registration;

import android.content.Intent;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.ProfileResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.auth.AuthActivity;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterStep3Activity extends AppCompatActivity {

    EditText etLastName;
    EditText etFistName;
    MaskedEditText etDBay;
    ImageView btnMale;
    ImageView btnWomen;
    CoordinatorLayout coordinatorLayout;
    int selected_sex = 1;
    String session_id = "";
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_step3);

        coordinatorLayout = findViewById(R.id.coordinator);
        etLastName = findViewById(R.id.editTextLastName);
        etFistName = findViewById(R.id.editTextFirstName);
        etDBay = findViewById(R.id.editTextBDay);

        String phone = PreferenceManager.getDefaultSharedPreferences(RegisterStep3Activity.this).getString("user_phone", "");


        Button buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(view -> {
            if (!checkData(etLastName.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_lastname);
            } else if (!checkData(etFistName.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_firstname);
            } else if (!checkDataBirthDay(etDBay.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_bday);
            } else {
                updateAction(etLastName.getText().toString(), etFistName.getText().toString(), etDBay.getText().toString(), selected_sex, phone);
            }
        });

        btnMale = findViewById(R.id.imageViewMCheck);
        btnWomen = findViewById(R.id.imageViewWCheck);
        btnMale.setOnClickListener(view -> {
            if (selected_sex == 0) {
                btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
                btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
                selected_sex = 1;
            }
        });
        btnWomen.setOnClickListener(view -> {
            if (selected_sex == 1) {
                btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
                btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
                selected_sex = 0;
            }
        });
    }


    public boolean checkData(String value) {
        boolean result = true;
        String valueTrimmed = value.trim();
        if (valueTrimmed.isEmpty()) result = false;
        return result;
    }

    public boolean checkDataBirthDay(String value) {
        boolean result = true;
        String valueTrimmed = value.trim();
        if (valueTrimmed.isEmpty()) result = false;
        if (valueTrimmed.length() < 10) result = false;
        return result;
    }


    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(RegisterStep3Activity.this, AuthActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        super.onBackPressed();  // optional depending on your needs
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

    private void updateAction(final String lname, final String fname, final String bday, final int sex, String phone)  {

        Log.d("UpdateAction", "lname = " + lname);
        Log.d("UpdateAction", "fname" + fname);
        Log.d("UpdateAction", "bday" + bday);
        Log.d("UpdateAction", "sex" + String.valueOf(sex));
        Log.d("UpdateAction", "session_id" + session_id);

        ApplicationController.getInstance().showLoading(RegisterStep3Activity.this);
        ApplicationController.getApi().createProfile(fname, lname, "-", bday, String.valueOf(sex), "1", "1", phone).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                ApplicationController.getInstance().hideLoading();
                Log.d("App", response.raw().toString());
                if (response.isSuccessful()) {
                    if (response.body().code == 0) {
                        response.body().getAuthModel().save(RegisterStep3Activity.this);
                        //getProfile();
                        ApplicationController.showError(RegisterStep3Activity.this, response.body().message);
                        Intent i = new Intent(RegisterStep3Activity.this, AuthActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        ApplicationController.showError(RegisterStep3Activity.this, response.body().message);
                    }
                } else {
                    ApplicationController.getInstance().hideLoading();
                    ApplicationController.showError(RegisterStep3Activity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(RegisterStep3Activity.this, getString(R.string.connection_error));
            }
        });

    }

    public void getProfile() {
        ApplicationController.getInstance().showLoading(RegisterStep3Activity.this);
        ApplicationController.getApi().profile(AuthModel.getInstance(RegisterStep3Activity.this).sessionId).enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {
                Log.d("LoginProfile", response.raw().toString());
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    response.body().getProfileModel().save(RegisterStep3Activity.this);
                    Intent i = new Intent(RegisterStep3Activity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    ApplicationController.getInstance().hideLoading();
                    ApplicationController.showError(RegisterStep3Activity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {
                Log.d("LoginProfile", t.toString());
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(RegisterStep3Activity.this, getString(R.string.connection_error));
            }
        });
    }
}
