package com.example.engspace.topic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.topicactivity.SetRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.TopicResponse;
import com.example.engspace.set.LearnActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSetsByTopicActivity extends AppCompatActivity {
    int topic_id;
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    TextView noSet;
    TextView topicName;
    TextView topicAmount;
    TextView topicDescription;
    FrameLayout progressOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sets_by_topic);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();


        Bundle bundle = getIntent().getExtras();
        topic_id = 0;
        if (bundle != null) {
            topic_id = bundle.getInt("topic_id");
        }

        topicName = findViewById(R.id.topic_name);
        topicAmount = findViewById(R.id.topic_amount);
        topicDescription = findViewById(R.id.topic_describe);

        //Set RecyclerView
        noSet = findViewById(R.id.no_set);
        setRecyclerView = findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getApplicationContext(), new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(ListSetsByTopicActivity.this, LearnActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager setVerticalLayout = new LinearLayoutManager(ListSetsByTopicActivity.this, LinearLayoutManager.VERTICAL, false);
        setRecyclerView.setLayoutManager(setVerticalLayout);
        getListSetsByTopic(topic_id);
        setRecyclerView.setAdapter(setAdapter);

        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListSetsByTopic(topic_id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getListSetsByTopic(int topic_id) {
        Call<TopicResponse> topicResponseCall = ApiClient.getTopicService(getApplicationContext()).getTopic(topic_id);
        topicResponseCall.enqueue(new Callback<TopicResponse>() {
            @Override
            public void onResponse(Call<TopicResponse> call, Response<TopicResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    TopicResponse topicResponse = response.body();
                    if (topicResponse != null) {
                        if (topicResponse.getTopic_sets().isEmpty()) {
                            noSet.setVisibility(View.VISIBLE);
                            setRecyclerView.setVisibility(View.GONE);
                        } else {
                            noSet.setVisibility(View.GONE);
                            setRecyclerView.setVisibility(View.VISIBLE);
                        }
                        topicName.setText(topicResponse.getName());
                        topicAmount.setText(String.valueOf(topicResponse.getTopic_sets().size())+" học phần");
                        topicDescription.setText(topicResponse.getDescription());
                        setAdapter.setData(topicResponse.getTopic_sets());
                    }
                } else {
                    Toast.makeText(ListSetsByTopicActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TopicResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(ListSetsByTopicActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
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