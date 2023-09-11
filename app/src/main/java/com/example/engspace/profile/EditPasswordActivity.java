package com.example.engspace.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.ChangePasswordRequest;
import com.example.engspace.model.ChangePasswordResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {

    FrameLayout progressOverlay;
    TextInputLayout passwordInputLayout;
    TextInputLayout passwordConfirmInputLayout;
    TextInputLayout oldPasswordInputLayout;
    TextInputEditText passwordInput;
    TextInputEditText passwordConfirmInput;
    TextInputEditText oldPasswordInput;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);

        //Lấy thông tin từ share preferences lưu trong máy local
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Lấy cái layout của các khung input password để set error khi có lỗi
        passwordInputLayout = (TextInputLayout) findViewById(R.id.password_input_layout);
        passwordConfirmInputLayout = (TextInputLayout) findViewById(R.id.password2_input_layout);
        oldPasswordInputLayout = (TextInputLayout) findViewById(R.id.old_password_input_layout);

        //Lấy button save để gọi api save khi ấn button
        saveButton = (ImageButton) findViewById(R.id.save_btn);

        //Lấy các khung input password để lấy password và gọi api lưu lên backend
        passwordInput = (TextInputEditText) findViewById(R.id.password_input);
        passwordConfirmInput = (TextInputEditText) findViewById(R.id.password2_input);
        oldPasswordInput = (TextInputEditText) findViewById(R.id.old_password_input);

        //Khi ấn nút save
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisible();
                passwordInputLayout.setError(null);
                passwordConfirmInputLayout.setError(null);
                oldPasswordInputLayout.setError(null);
                ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                changePasswordRequest.setPassword(passwordInput.getText().toString());
                changePasswordRequest.setPassword2(passwordConfirmInput.getText().toString());
                changePasswordRequest.setOld_password(oldPasswordInput.getText().toString());
                int user_id = sharedPreferences.getInt("user_id", 1);
                Call<ChangePasswordResponse> changePasswordResponseCall = ApiClient.getUserService(getApplicationContext()).changePassword(changePasswordRequest, user_id);
                changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
                    @Override
                    public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            finish();
                        } else {
                            Gson gson = new Gson();
                            ChangePasswordResponse changePasswordError = gson.fromJson(response.errorBody().charStream(), ChangePasswordResponse.class);
                            if (changePasswordError.getPassword() != null && !changePasswordError.getPassword().isEmpty()) {
                                passwordInputLayout.setError(changePasswordError.getPassword().get(0));
                            }
                            if (changePasswordError.getPassword2() != null && !changePasswordError.getPassword2().isEmpty()) {
                                passwordConfirmInputLayout.setError(changePasswordError.getPassword2().get(0));
                            } else if (changePasswordError.getNon_field_errors() != null && !changePasswordError.getNon_field_errors().isEmpty()) {
                                passwordConfirmInputLayout.setError(changePasswordError.getNon_field_errors().get(0));
                            }
                            if (changePasswordError.getOld_password() != null && !changePasswordError.getOld_password().isEmpty()) {
                                oldPasswordInputLayout.setError(changePasswordError.getOld_password().get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                        setInvisible();
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}