package com.example.engspace.folder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.folderactivity.SetRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FolderSetRequest;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFolderToSetActivity extends AppCompatActivity {

    FrameLayout progressOverlay;
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setRecyclerViewAdapter;
    int folder_id;
    Bundle bundle;
    ArrayList<SetResponse> sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder_to_set);

        bundle = getIntent().getExtras();
        folder_id = 0;
        if (bundle != null) {
            folder_id = bundle.getInt("folder_id", 0);
        } else {
            finish();
        }

        if (folder_id == 0) {
            finish();
        }

        sets = new ArrayList<SetResponse>();

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int user_id = sharedPreferences.getInt("user_id", 0);

        //Set RecyclerView
        setRecyclerView = findViewById(R.id.rcv_set);
        setRecyclerViewAdapter = new SetRecyclerViewAdapter(getApplicationContext(),new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResponseArrayList = setRecyclerViewAdapter.getData();
                //Select this set
                boolean flag = false;
                for (int i = 0; i < sets.size(); i++) {
                    if (sets.get(i).getId() == setResponseArrayList.get(position).getId()) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    removeSet(setResponseArrayList.get(position).getId());
                } else {
                    addSet(setResponseArrayList.get(position).getId());
                }
                setRecyclerViewAdapter.setBorder(position);
            }
        });
        LinearLayoutManager folderVerticalLayout = new LinearLayoutManager(AddFolderToSetActivity.this, LinearLayoutManager.VERTICAL, false);
        setRecyclerView.setLayoutManager(folderVerticalLayout);
        getListSetsByUser(user_id);
        setRecyclerView.setAdapter(setRecyclerViewAdapter);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
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
                    setRecyclerViewAdapter.setData(setsResponse.getResults());
                    getFolderById(folder_id);
                } else {
                    Toast.makeText(AddFolderToSetActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SetsResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(AddFolderToSetActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFolderById(int id) {
        setVisible();
        Call<FolderResponse> folderResponseCall = ApiClient.getFolderService(getApplicationContext()).getFolder(id);
        folderResponseCall.enqueue(new Callback<FolderResponse>() {
            @Override
            public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FolderResponse folderResponse = response.body();
                    if (folderResponse != null) {
                        if (folderResponse.getSets() != null && !folderResponse.getSets().isEmpty()) {
                            sets = folderResponse.getSets();
                            setRecyclerViewAdapter.setBorderByArrayId(folderResponse.getSets());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FolderResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addSet(int set_id) {
        setVisible();
        FolderSetRequest folderSetRequest = new FolderSetRequest();
        folderSetRequest.setSet_id(set_id);
        folderSetRequest.setFolder_id(folder_id);
        Call<Void> folderSetResponseCall = ApiClient.getFolderService(getApplicationContext()).addSet(folderSetRequest);
        folderSetResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setInvisible();
                    SetResponse setResponse = new SetResponse();
                    setResponse.setId(set_id);
                    sets.add(setResponse);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void removeSet(int set_id) {
        setVisible();
        FolderSetRequest folderSetRequest = new FolderSetRequest();
        folderSetRequest.setSet_id(set_id);
        folderSetRequest.setFolder_id(folder_id);
        Call<Void> folderSetResponseCall = ApiClient.getFolderService(getApplicationContext()).deleteSet(folderSetRequest);
        folderSetResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setInvisible();
                    for (int i = 0; i < sets.size(); i++) {
                        if (sets.get(i).getId() == set_id) {
                            sets.remove(i);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setInvisible();
                for (int i = 0; i < sets.size(); i++) {
                    if (sets.get(i).getId() == set_id) {
                        sets.remove(i);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

}