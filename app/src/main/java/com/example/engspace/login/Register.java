package com.example.engspace.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.RegisterRequest;
import com.example.engspace.model.RegisterResponse;
import com.example.engspace.model.RegisterResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    TextInputEditText surname;
    TextInputEditText name;
    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText password1;
    TextInputLayout surnameInputLayout;
    TextInputLayout nameInputLayout;
    TextInputLayout usernameInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout password1InputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        surname = findViewById(R.id.surname_input);
        name = findViewById(R.id.name_input);
        username = findViewById(R.id.username_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        password1 = findViewById(R.id.confirm_password_input);

        surnameInputLayout = findViewById(R.id.surname_input_layout);
        nameInputLayout = findViewById(R.id.name_input_layout);
        usernameInputLayout = findViewById(R.id.username_input_layout);
        emailInputLayout = findViewById(R.id.email_input_layout);
        passwordInputLayout = findViewById(R.id.password_input_layout);
        password1InputLayout = findViewById(R.id.confirm_password_input_layout);

        Button registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals(password1.getText().toString())) {
                    surnameInputLayout.setError(null);
                    nameInputLayout.setError(null);
                    usernameInputLayout.setError(null);
                    emailInputLayout.setError(null);
                    passwordInputLayout.setError("Mật khẩu xác nhận không đúng");
                    password1InputLayout.setError("Mật khẩu xác nhận không đúng");
                    return;
                } else {
                    passwordInputLayout.setError(null);
                    password1InputLayout.setError(null);
                }
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setLast_name(surname.getText().toString());
                registerRequest.setFirst_name(name.getText().toString());
                registerRequest.setUsername(username.getText().toString());
                registerRequest.setEmail(email.getText().toString());
                registerRequest.setPassword(password.getText().toString());
                Call<RegisterResponse> registerRequestCall = ApiClient.getUserService(getApplicationContext()).userRegister(registerRequest);
                registerRequestCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Register.this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            RegisterResponseError registerResponseError = gson.fromJson(response.errorBody().charStream(), RegisterResponseError.class);
                            if (registerResponseError.getLast_name() != null && !registerResponseError.getLast_name().isEmpty()) {
                                surnameInputLayout.setError(registerResponseError.getLast_name().get(0));
                            } else {
                                surnameInputLayout.setError(null);
                            }
                            if (registerResponseError.getFirst_name() != null && !registerResponseError.getFirst_name().isEmpty()) {
                                nameInputLayout.setError(registerResponseError.getFirst_name().get(0));
                            } else {
                                nameInputLayout.setError(null);
                            }
                            if (registerResponseError.getUsername() != null && !registerResponseError.getUsername().isEmpty()) {
                                usernameInputLayout.setError(registerResponseError.getUsername().get(0));
                            } else {
                                usernameInputLayout.setError(null);
                            }
                            if (registerResponseError.getEmail() != null && !registerResponseError.getEmail().isEmpty()) {
                                emailInputLayout.setError(registerResponseError.getEmail().get(0));
                            } else {
                                emailInputLayout.setError(null);
                            }
                            if (registerResponseError.getPassword() != null && !registerResponseError.getPassword().isEmpty()) {
                                passwordInputLayout.setError(registerResponseError.getPassword().get(0));
                            } else {
                                passwordInputLayout.setError(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Toast.makeText(Register.this, "Tạo tài khoản thất bại", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.btn_back_register);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        });
    }
}