package com.example.engspace.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.MainActivity;
import com.example.engspace.R;

public class LoginSuccessfully extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successfully);

        Button toHome = (Button) findViewById(R.id.continue_to_home_btn);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSuccessfully.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}