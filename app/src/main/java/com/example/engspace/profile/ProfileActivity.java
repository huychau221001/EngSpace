package com.example.engspace.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.engspace.R;
import com.example.engspace.SuperProfileActivity;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    FrameLayout progressOverlay;
    TextView fullnameTopLabel;
    TextView usernameLabel;
    TextView fullnameLabel;
    TextView dobLabel;
    TextView emailLabel;
    TextView descriptionLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        fullnameTopLabel = findViewById(R.id.fullname_top_label);
        usernameLabel = findViewById(R.id.username_label);
        fullnameLabel = findViewById(R.id.fullname_label);
        dobLabel = findViewById(R.id.dob_label);
        emailLabel = findViewById(R.id.email_label);
        descriptionLabel = findViewById(R.id.description_label);

        Call<UserResponse> userResponseCall = ApiClient.getUserService(getApplicationContext()).getUserProfile();
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        String fullname = userResponse.getLast_name() + " " + userResponse.getFirst_name();
                        fullnameTopLabel.setText(fullname);
                        usernameLabel.setText("Tên tài khoản: "+userResponse.getUsername());
                        fullnameLabel.setText("Họ và tên: "+fullname);
                        dobLabel.setText("Ngày sinh: "+userResponse.getDobString());
                        emailLabel.setText("Email: "+userResponse.getEmail());
                        if (userResponse.getBio().isEmpty()) {
                            descriptionLabel.setText("Không có mô tả.");
                        } else {
                            descriptionLabel.setText(userResponse.getBio());
                        }
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.avatar_follower);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SuperProfileActivity.class);
                startActivity(intent);
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