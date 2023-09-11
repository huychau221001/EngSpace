package com.example.engspace.set;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.adapter.setfragment.SetDetailRecyclerViewAdapter;
import com.example.engspace.adapter.setfragment.SwipeToDeleteCallback;
import com.example.engspace.adapter.setfragment.TopicArrayAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetDetailRequest;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetRequest;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetResponseError;
import com.example.engspace.model.TopicResponse;
import com.example.engspace.model.TopicsResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditSetActivity extends AppCompatActivity {

    int set_id;
    ArrayList<SetDetailResponse> listSetDetails;
    TextInputEditText nameInput;
    TextInputEditText descriptionInput;
    TextInputLayout nameInputLayout;
    TextInputLayout descriptionInputLayout;
    TextInputLayout topicInputLayout;

    LinearLayout addSetLayout;
    FloatingActionButton floatingActionButton;
    SetDetailRecyclerViewAdapter setDetailRecyclerViewAdapter;
    RecyclerView setDetailRecyclerView;
    CoordinatorLayout coordinatorLayout;
    ImageButton saveButton;
    FrameLayout progressOverlay;
    MaterialAutoCompleteTextView topicInput;
    TopicArrayAdapter topicArrayAdapter;
    TopicResponse topic;
    ArrayList<TopicResponse> topicResponseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        Bundle bundle = getIntent().getExtras();
        set_id = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id");
        } else {
            finish();
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        addSetLayout = (LinearLayout) findViewById(R.id.add_set_layout);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        //Hiển thị topic lên
        topicResponseArrayList = new ArrayList<>();
        topicArrayAdapter = new TopicArrayAdapter(EditSetActivity.this, 1, topicResponseArrayList);
        topicInput = (MaterialAutoCompleteTextView) findViewById(R.id.topic_input);
        topicArrayAdapter.setNotifyOnChange(true);
        topicInput.setAdapter(topicArrayAdapter);


        //Data
        listSetDetails = new ArrayList<>();
        SetDetailResponse setDetailResponse = new SetDetailResponse("", "");
        //listSetDetails.add(setDetailResponse);
        //Get data
        getListTopics();



        topicInput.setOnItemClickListener((adapterView, view, i, l) -> topic = topicArrayAdapter.getItem(i));

        //RecyclerView
        setDetailRecyclerView = findViewById(R.id.rcv_set_detail);
        setDetailRecyclerViewAdapter = new SetDetailRecyclerViewAdapter(getApplicationContext(), listSetDetails,
                new SetDetailRecyclerViewAdapter.TextChangeCallback() {
                    @Override
                    public void textChangedAt(int position, String text) {
                        listSetDetails.get(position).setTerm(text);
                    }
                },
                new SetDetailRecyclerViewAdapter.TextChangeCallback() {
                    @Override
                    public void textChangedAt(int position, String text) {
                        listSetDetails.get(position).setDefinition(text);
                    }
                });
        LinearLayoutManager setDetailVerticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        setDetailRecyclerView.setLayoutManager(setDetailVerticalLayout);
        setDetailRecyclerView.setAdapter(setDetailRecyclerViewAdapter);
        //Bật vuốt để xóa và undo
        enableSwipeToDeleteAndUndo();

        //Thêm box mới để nhập từ vựng
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listSetDetails.add(new SetDetailResponse("", ""));
                setDetailRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        //Lấy khung input để gọi api lưu lên backend
        nameInput = (TextInputEditText) findViewById(R.id.name_input);
        descriptionInput = (TextInputEditText) findViewById(R.id.description_input);
        topicInputLayout = (TextInputLayout) findViewById(R.id.topic_input_layout);
        nameInputLayout = (TextInputLayout) findViewById(R.id.name_input_layout);
        descriptionInputLayout = (TextInputLayout) findViewById(R.id.description_input_layout);
        //Lấy button save để gọi api save khi ấn button
        saveButton = (ImageButton) findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisible();
                SetRequest setRequest = new SetRequest();
                setRequest.setIs_public(true);
                setRequest.setName(nameInput.getText().toString());
                setRequest.setDescription(descriptionInput.getText().toString());
                if (topic != null) {
                    setRequest.setTopic(topic.getId());
                } else {
                    setRequest.setTopic(0);
                }

                setRequest.setSet_details(listSetDetails);
                Call<SetResponse> setResponseCall = ApiClient.getSetService(getApplicationContext()).updateSet(set_id, setRequest);
                setResponseCall.enqueue(new Callback<SetResponse>() {
                    @Override
                    public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            SetResponse setResponse = response.body();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            SetResponseError setResponseError = gson.fromJson(response.errorBody().charStream(), SetResponseError.class);
                            if (setResponseError.getName() != null && !setResponseError.getName().isEmpty()) {
                                nameInputLayout.setError(setResponseError.getName().get(0));
                            } else {
                                nameInputLayout.setError(null);
                            }
                            if (setResponseError.getDescription() != null && !setResponseError.getDescription().isEmpty()) {
                                descriptionInputLayout.setError(setResponseError.getDescription().get(0));
                            } else {
                                descriptionInputLayout.setError(null);
                            }
                            if (setResponseError.getTopic() != null && !setResponseError.getTopic().isEmpty()) {
                                topicInputLayout.setError(setResponseError.getTopic().get(0));
                            } else {
                                topicInputLayout.setError(null);
                            }
                            if (setResponseError.getSet_details() != null && !setResponseError.getSet_details().isEmpty()) {
                                for (int i = 0; i < setResponseError.getSet_details().size(); i++) {
                                    if (setResponseError.getSet_details().get(i) != null) {
                                        setDetailRecyclerViewAdapter.setError(setResponseError.getSet_details().get(i), i);
                                    } else {
                                        setDetailRecyclerViewAdapter.setError(null, i);
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SetResponse> call, Throwable t) {
                        setInvisible();
                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //Nút quay về
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

    SetDetailResponse item;
    //Hàm dùng để bật phần vuốt đề xóa
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                item = setDetailRecyclerViewAdapter.getData().get(position);

                if (item.getId() != 0) {
                    Call<SetDetailResponse> setDetailResponseCall = ApiClient.getSetDetailService(getApplicationContext()).deleteSetDetail(item.getId());
                    setDetailResponseCall.enqueue(new Callback<SetDetailResponse>() {
                        @Override
                        public void onResponse(Call<SetDetailResponse> call, Response<SetDetailResponse> response) {
                            if (response.isSuccessful()) {
                                setDetailRecyclerViewAdapter.removeItem(position);
                            } else {
                                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SetDetailResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    setDetailRecyclerViewAdapter.removeItem(position);
                }


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Đã xóa item khỏi danh sách.", Snackbar.LENGTH_LONG);
                snackbar.setAction("HOÀN TÁC", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getId() != 0) {
                            SetDetailRequest setDetailRequest = new SetDetailRequest();
                            setDetailRequest.setTerm(item.getTerm());
                            setDetailRequest.setDefinition(item.getDefinition());
                            setDetailRequest.setSet(set_id);
                            Call<SetDetailResponse> setDetailResponseCall = ApiClient.getSetDetailService(getApplicationContext()).addSetDetail(setDetailRequest);
                            setDetailResponseCall.enqueue(new Callback<SetDetailResponse>() {
                                @Override
                                public void onResponse(Call<SetDetailResponse> call, Response<SetDetailResponse> response) {
                                    if (response.isSuccessful()) {
                                        SetDetailResponse setDetailResponse = response.body();
                                        item = setDetailResponse;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SetDetailResponse> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        setDetailRecyclerViewAdapter.restoreItem(item, position);
                        setDetailRecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(setDetailRecyclerView);
    }

    private void getListTopics() {
        Call<TopicsResponse> topicsResponseCall = ApiClient.getTopicService(getApplicationContext()).getTopics();
        topicsResponseCall.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    TopicsResponse topicsResponse = response.body();
                    for (int i = 0; i < topicsResponse.getResults().size(); i++) {
                        topicArrayAdapter.add(topicsResponse.getResults().get(i));
                    }
                    topicArrayAdapter.notifyDataSetChanged();
                    getSetById(set_id);
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSetById(int set_id) {
        setVisible();
        Call<SetResponse> setResponseCall = ApiClient.getSetService(getApplicationContext()).getSet(set_id);
        setResponseCall.enqueue(new Callback<SetResponse>() {
            @Override
            public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetResponse setResponse = response.body();
                    if (setResponse != null) {
                        nameInput.setText(setResponse.getName());
                        descriptionInput.setText(setResponse.getDescription());
                        String topic_name = "";
                        for (TopicResponse topicResponse : topicResponseArrayList) {
                            if (topicResponse.getId() == setResponse.getTopic()) {
                                topic_name = topicResponse.toString();
                                topic = topicResponse;
                            }
                        }
                        topicInput.setText(topic_name, false);
                        for (int i = 0; i < setResponse.getSet_details().size(); i++) {
                            if (setResponse.getSet_details().get(i) != null) {
                                int id = setResponse.getSet_details().get(i).getId();
                                String term = setResponse.getSet_details().get(i).getTerm();
                                String definition = setResponse.getSet_details().get(i).getDefinition();
                                listSetDetails.add(new SetDetailResponse(id, term, definition));
                                setDetailRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể lấy dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SetResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }
}