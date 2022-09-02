package com.octarine.plove.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.auth.AuthActivity;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterLinkCardActivity extends AppCompatActivity {

    EditText etFistName;
    MaskedEditText etDBay;
    CoordinatorLayout coordinatorLayout;
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_link);

        coordinatorLayout = findViewById(R.id.coordinator);
        etFistName = findViewById(R.id.editTextFirstName);
        etDBay = findViewById(R.id.editTextBDay);

        String phone = getIntent().getExtras().getString("phone");

        Button buttonNext = findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(view -> {
            if (!checkData(etFistName.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_card);
            } else if (!checkDataBirthDay(etDBay.getText().toString())) {
                ApplicationController.getInstance().showMessage(coordinatorLayout, R.string.error_empty_bday);
            } else {
                searchAction(phone, etFistName.getText().toString(), etDBay.getText().toString(), AuthModel.getInstance(this).sessionId);
            }
        });


        Button noCard = findViewById(R.id.buttonNoCard);
        noCard.setOnClickListener(view -> {
            Intent i = new Intent(RegisterLinkCardActivity.this, RegisterStep3Activity.class);
            i.putExtra("phone", phone);
            startActivity(i);
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
        Intent i = new Intent(RegisterLinkCardActivity.this, AuthActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        super.onBackPressed();
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

    private void searchAction(String phone, final String card, final String birthday, String session_id)  {
        ApplicationController.getInstance().showLoading(RegisterLinkCardActivity.this);
        ApplicationController.getApi().mesearch(phone, card, birthday, session_id).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                ApplicationController.getInstance().hideLoading();
                if (response.isSuccessful()) {
                    response.body().getAuthModel().save(RegisterLinkCardActivity.this);
                    if (response.body().code == 800) {
                        ApplicationController.showError(RegisterLinkCardActivity.this, response.body().message);
                        Intent i = new Intent(RegisterLinkCardActivity.this, AuthActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        ApplicationController.showError(RegisterLinkCardActivity.this, response.body().message);
                    }
                } else {
                    ApplicationController.getInstance().hideLoading();
                    ApplicationController.showError(RegisterLinkCardActivity.this, getString(R.string.connection_error));
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(RegisterLinkCardActivity.this, getString(R.string.connection_error));
            }
        });

    }
}
