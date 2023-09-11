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
import com.example.engspace.model.ResetPasswordResponse;
import com.example.engspace.model.UserResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPassword extends AppCompatActivity {
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    FrameLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_password);

        textInputLayout = findViewById(R.id.email_input_layout);
        textInputEditText = findViewById(R.id.email_input);

        Button sendEmail = (Button) findViewById(R.id.send_email_btn);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressOverlay = findViewById(R.id.progress_overlay);
                setVisible();
                String email = textInputEditText.getText().toString();
                Call<ResetPasswordResponse> resetPasswordResponseCall = ApiClient.getUserService(getApplicationContext()).resetPassword(email);
                resetPasswordResponseCall.enqueue(new Callback<ResetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            Intent intent = new Intent(RequestPassword.this, SetNewPassword.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Gson gson = new Gson();
                            ResetPasswordResponse resetPasswordResponse = gson.fromJson(response.errorBody().charStream(), ResetPasswordResponse.class);
                            if (resetPasswordResponse.getDetail() != null && !resetPasswordResponse.getDetail().equals("")) {
                                textInputLayout.setError(resetPasswordResponse.getDetail());
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

        ImageButton backBtn = (ImageButton) findViewById(R.id.btn_back_request_pwd);
        backBtn.setOnClickListener(new View.OnClickListener() {
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