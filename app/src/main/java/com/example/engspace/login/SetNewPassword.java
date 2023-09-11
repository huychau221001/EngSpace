package com.example.engspace.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.ResetPasswordRequest;
import com.example.engspace.model.ResetPasswordResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNewPassword extends AppCompatActivity {
    TextInputLayout codeInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout confirmInputLayout;
    TextInputEditText codeInput;
    TextInputEditText passwordInput;
    TextInputEditText confirmInput;
    String email = "";
    FrameLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
        }

        codeInputLayout = findViewById(R.id.confirm_code_input_layout);
        passwordInputLayout = findViewById(R.id.new_password_input_layout);
        confirmInputLayout = findViewById(R.id.confirm_new_password_input_layout);
        codeInput = findViewById(R.id.confirm_code_input);
        passwordInput = findViewById(R.id.new_password_input);
        confirmInput = findViewById(R.id.confirm_new_password_input);

        Button confirmNewPwd = (Button) findViewById(R.id.confirm_new_password_btn);
        confirmNewPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeInputLayout.setError(null);
                passwordInputLayout.setError(null);
                confirmInputLayout.setError(null);
                progressOverlay = findViewById(R.id.progress_overlay);
                setVisible();
                ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
                resetPasswordRequest.setOTP(codeInput.getText().toString());
                resetPasswordRequest.setPassword(passwordInput.getText().toString());
                resetPasswordRequest.setPassword2(confirmInput.getText().toString());

                Call<ResetPasswordResponse> resetPasswordResponseCall = ApiClient.getUserService(getApplicationContext()).resetPasswordPost(resetPasswordRequest, email);
                resetPasswordResponseCall.enqueue(new Callback<ResetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SetNewPassword.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            ResetPasswordResponse resetPasswordResponse = gson.fromJson(response.errorBody().charStream(), ResetPasswordResponse.class);
                            if (resetPasswordResponse.getOTP() != null && !resetPasswordResponse.getOTP().isEmpty()) {
                                codeInputLayout.setError(resetPasswordResponse.getOTP().get(0));
                            }
                            if (resetPasswordResponse.getPassword() != null && !resetPasswordResponse.getPassword().isEmpty()) {
                                passwordInputLayout.setError(resetPasswordResponse.getPassword().get(0));
                            }
                            if (resetPasswordResponse.getPassword2() != null && !resetPasswordResponse.getPassword2().isEmpty()) {
                                confirmInputLayout.setError(resetPasswordResponse.getPassword2().get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                        setInvisible();
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_back_set_new_pwd);
        back.setOnClickListener(new View.OnClickListener() {
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