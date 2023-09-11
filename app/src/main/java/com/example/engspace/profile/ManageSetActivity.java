package com.example.engspace.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.profileactivity.SetRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetsResponse;
import com.example.engspace.set.LearnActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageSetActivity extends AppCompatActivity {
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    FrameLayout progressOverlay;
    TextView noSetTextView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_set);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Set RecyclerView
        noSetTextView = findViewById(R.id.no_set);
        setRecyclerView = findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getApplicationContext(), new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(ManageSetActivity.this, LearnActivity.class);
                intent.putExtras(bundle);
                someActivityResultLauncher.launch(intent);
            }
        });
        LinearLayoutManager setVerticalLayout = new LinearLayoutManager(ManageSetActivity.this, LinearLayoutManager.VERTICAL, false);
        setRecyclerView.setLayoutManager(setVerticalLayout);
        getListSetsByUser(sharedPreferences.getInt("user_id", 0));
        setRecyclerView.setAdapter(setAdapter);

        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListSetsByUser(sharedPreferences.getInt("user_id", 0));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //Back
        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getListSetsByUser(int user_id) {
        Call<SetsResponse> setsResponseCall = ApiClient.getSetService(getApplicationContext()).getSetsByUser(user_id);
        setsResponseCall.enqueue(new Callback<SetsResponse>() {
            @Override
            public void onResponse(Call<SetsResponse> call, Response<SetsResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetsResponse setsResponse = response.body();
                    if (setsResponse.getResults().isEmpty()) {
                        noSetTextView.setVisibility(View.VISIBLE);
                        setRecyclerView.setVisibility(View.GONE);
                    } else {
                        noSetTextView.setVisibility(View.GONE);
                        setRecyclerView.setVisibility(View.VISIBLE);
                    }
                    setAdapter.setData(setsResponse.getResults());
                } else {
                    Toast.makeText(ManageSetActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SetsResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(ManageSetActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getListSetsByUser(sharedPreferences.getInt("user_id", 0));
                    }
                }
            });

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}