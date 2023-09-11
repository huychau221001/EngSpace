package com.example.engspace.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckMatchingActivity extends AppCompatActivity {

    int set_id;
    int amount;
    int function;
    FrameLayout progressOverlay;
    LinearLayout stopLayout;
    LinearLayout readyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_matching);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);

        Bundle bundle = getIntent().getExtras();
        set_id = 0;
        amount = 0;
        function = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id");
            amount = bundle.getInt("amount");
            function = bundle.getInt("function");
        }

        if (set_id == 0) {
            finish();
        }

        final MediaPlayer mp = MediaPlayer.create(CheckMatchingActivity.this, R.raw.start);
        mp.start();

        stopLayout = findViewById(R.id.stop_layout);
        readyLayout = findViewById(R.id.ready_layout);

        if (amount == 0) {
            stopLayout.setVisibility(View.VISIBLE);
            readyLayout.setVisibility(View.GONE);
        } else {
            if (function == 1) {
                TextView successContent = findViewById(R.id.success_content);
                successContent.setText("Chức năng học đã sẵn sàng cho bạn trải nghiệm.");
            }
            stopLayout.setVisibility(View.GONE);
            readyLayout.setVisibility(View.VISIBLE);
        }

        stopLayout = findViewById(R.id.stop_layout);
        readyLayout = findViewById(R.id.ready_layout);

        AppCompatButton btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (function == 1) {
                    intent = new Intent(CheckMatchingActivity.this, CardLearnActivity.class);
                } else {
                    intent = new Intent(CheckMatchingActivity.this, CardMatchingActivity.class);
                }
                Bundle bundle1 = new Bundle();
                bundle1.putInt("set_id", set_id);
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();
            }
        });

        AppCompatButton btnBack1 = findViewById(R.id.btn_back1);
        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
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