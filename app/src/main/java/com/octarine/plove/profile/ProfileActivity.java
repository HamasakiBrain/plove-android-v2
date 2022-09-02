package com.octarine.plove.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.app.ApplicationController;
import com.vicmikhailau.maskededittext.MaskedEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    int selected_sex = 1;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        final EditText etLastName = (EditText) findViewById(R.id.editTextLastName);
        final EditText etFistName = (EditText) findViewById(R.id.editTextFirstName);
        final MaskedEditText etDBay = (MaskedEditText) findViewById(R.id.editTextBDay);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(view -> {

            ProfileModel user = ProfileModel.getInstance(ProfileActivity.this);
            user.firstname = etFistName.getText().toString();
            user.lastname = etLastName.getText().toString();
            user.birthday = etDBay.getText().toString();
            user.sex = String.valueOf(selected_sex);
            user.save(ProfileActivity.this);

            ApplicationController.getInstance().showLoading(ProfileActivity.this);

            Log.d("lname", user.lastname);
            Log.d("fname", user.firstname);
            Log.d("bday", user.birthday);
            Log.d("sex", user.sex);
            Log.d("sessionId", AuthModel.getInstance(ProfileActivity.this).sessionId);

            ApplicationController.getApi().updateProfile(AuthModel.getInstance(ProfileActivity.this).sessionId, user.firstname,
                    user.lastname, user.email, user.birthday, user.sex, "1", "1").enqueue(new Callback<AuthResponseModel>() {
                @Override
                public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                    Log.d("UpdateProfile", response.raw().toString());
                    ApplicationController.getInstance().hideLoading();
                    if (response.isSuccessful())
                        ApplicationController.showError(ProfileActivity.this, response.body().message);
                }

                @Override
                public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                    Log.d("UpdateProfile", t.toString());
                    ApplicationController.showError(ProfileActivity.this, getString(R.string.connection_error));
                    ApplicationController.getInstance().hideLoading();
                }
            });
        });

        final ImageView btnMale = (ImageView) findViewById(R.id.imageViewMCheck);
        final ImageView btnWomen = (ImageView) findViewById(R.id.imageViewWCheck);

        ProfileModel user = ProfileModel.getInstance(ProfileActivity.this);

        selected_sex = Integer.valueOf(user.sex);
        etLastName.setText(user.lastname);
        etFistName.setText(user.firstname);
        etDBay.setText(user.birthday);

        if (selected_sex == 1) {
            btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
            btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
        } else {
            btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
            btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
        }

        btnMale.setOnClickListener(view -> {
            if (selected_sex !=1) {
                btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
                btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
                selected_sex = 1;
            }
        });
        btnWomen.setOnClickListener(view -> {
            if (selected_sex == 1) {
                btnMale.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_off));
                btnWomen.setImageDrawable(getResources().getDrawable(R.drawable.radiobutton_on));
                selected_sex = 2;
            }
        });
    }
}
