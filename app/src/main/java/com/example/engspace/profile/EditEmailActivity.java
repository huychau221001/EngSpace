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
import com.example.engspace.model.UserRequest;
import com.example.engspace.model.UserResponse;
import com.example.engspace.model.UserResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEmailActivity extends AppCompatActivity {
    TextInputEditText emailInput;
    ImageButton saveButton;
    TextInputLayout textInputLayout;
    FrameLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        //Lấy thông tin từ share preferences lưu trong máy local
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Lấy cái layout của khung input email để set error khi có lỗi
        textInputLayout = (TextInputLayout) findViewById(R.id.email_input_layout);

        //Lấy button save để gọi api save khi ấn button
        saveButton = (ImageButton) findViewById(R.id.save_btn);

        //Lấy khung input email để hiển thị email từ api và lấy email để gọi api lưu lên backend
        emailInput = (TextInputEditText) findViewById(R.id.email_input);

        Call<UserResponse> userResponseCall = ApiClient.getUserService(getApplicationContext()).getUserProfile();
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                //Tắt cái ProgressOverlay đi vì gọi api xong
                setInvisible();
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    emailInput.setText(userResponse.getEmail());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //Tắt cái ProgressOverlay đi vì gọi api thất bại
                setInvisible();
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cho cái ProgressOverlay hiển thị lên vì đang gọi api save dữ liệu lên backend
                setVisible();
                UserRequest userRequest = new UserRequest();
                userRequest.setEmail(emailInput.getText().toString());
                Call<UserResponse> userResponseCall1 = ApiClient.getUserService(getApplicationContext()).updateUserProfile(userRequest);
                userResponseCall1.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        //Tắt cái ProgressOverlay đi vì gọi api xong
                        setInvisible();
                        if (response.isSuccessful()) {
                            sharedPreferences.edit().putString("email", userRequest.getEmail()).apply();
                            finish();
                        } else {
                            Gson gson = new Gson();
                            UserResponseError userResponseError = gson.fromJson(response.errorBody().charStream(), UserResponseError.class);
                            if (userResponseError.getEmail() != null && !userResponseError.getEmail().isEmpty()) {
                                textInputLayout.setError(userResponseError.getEmail().get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        //Tắt cái ProgressOverlay đi vì gọi api thất bại
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