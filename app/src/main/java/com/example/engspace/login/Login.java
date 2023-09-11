package com.example.engspace.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.LoginRequest;
import com.example.engspace.model.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        AppCompatButton login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText usernameInput = findViewById(R.id.username_input);
                TextInputEditText passwordInput = findViewById(R.id.password_input);
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(usernameInput.getText().toString());
                loginRequest.setPassword(passwordInput.getText().toString());

                Call<LoginResponse> loginResponseCall = ApiClient.getUserService(getApplicationContext()).userLogin(loginRequest);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                            sharedPreferences.edit().putString("access", loginResponse.getAccess()).apply();
                            sharedPreferences.edit().putString("refresh", loginResponse.getRefresh()).apply();
                            sharedPreferences.edit().putInt("user_id", loginResponse.getId()).apply();
                            sharedPreferences.edit().putString("username", String.valueOf(loginResponse.getUsername())).apply();
                            sharedPreferences.edit().putString("email", String.valueOf(loginResponse.getEmail())).apply();
                            String access_token = sharedPreferences.getString("access", "");
                            if (access_token != null && !access_token.equals("")) {
                                Intent intent = new Intent(Login.this, LoginSuccessfully.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(Login.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(Login.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        Button forgotPwd = (Button) findViewById(R.id.forgot_password_btn);
        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RequestPassword.class);
                startActivity(intent);
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        });
    }
}