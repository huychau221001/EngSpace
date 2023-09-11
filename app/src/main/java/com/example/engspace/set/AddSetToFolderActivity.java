package com.example.engspace.set;

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
import com.example.engspace.adapter.setfragment.FolderRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FolderSetRequest;
import com.example.engspace.model.FoldersResponse;
import com.example.engspace.model.SetResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSetToFolderActivity extends AppCompatActivity {

    FrameLayout progressOverlay;
    RecyclerView folderRecyclerView;
    FolderRecyclerViewAdapter folderRecyclerViewAdapter;
    int set_id;
    Bundle bundle;
    ArrayList<Integer> set_folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set_to_folder);

        bundle = getIntent().getExtras();
        set_id = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id", 0);
        } else {
            finish();
        }

        if (set_id == 0) {
            finish();
        }

        set_folders = new ArrayList<Integer>();

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int user_id = sharedPreferences.getInt("user_id", 0);

        //Folder RecyclerView
        folderRecyclerView = findViewById(R.id.rcv_folder);
        folderRecyclerViewAdapter = new FolderRecyclerViewAdapter(getApplicationContext(),new FolderRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<FolderResponse> folderResponseArrayList = folderRecyclerViewAdapter.getData();
                //Select this folder
                if (set_folders.contains(folderResponseArrayList.get(position).getId())) {
                    removeSet(folderResponseArrayList.get(position).getId());
                } else {
                    addSet(folderResponseArrayList.get(position).getId());
                }
                folderRecyclerViewAdapter.setBorder(position);
            }
        });
        LinearLayoutManager folderVerticalLayout = new LinearLayoutManager(AddSetToFolderActivity.this, LinearLayoutManager.VERTICAL, false);
        folderRecyclerView.setLayoutManager(folderVerticalLayout);
        getListFolderByUser(user_id);
        folderRecyclerView.setAdapter(folderRecyclerViewAdapter);

        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
    private void getListFolderByUser(int user_id) {
        Call<FoldersResponse> foldersResponseCall = ApiClient.getFolderService(getApplicationContext()).getFoldersByUser(user_id);
        foldersResponseCall.enqueue(new Callback<FoldersResponse>() {
            @Override
            public void onResponse(Call<FoldersResponse> call, Response<FoldersResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FoldersResponse foldersResponse = response.body();
                    folderRecyclerViewAdapter.setData(foldersResponse.getResults());
                    getSetById(set_id);
                } else {
                    Toast.makeText(AddSetToFolderActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FoldersResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(AddSetToFolderActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSetById(int id) {
        setVisible();
        Call<SetResponse> setResponseCall = ApiClient.getSetService(getApplicationContext()).getSet(id);
        setResponseCall.enqueue(new Callback<SetResponse>() {
            @Override
            public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetResponse setResponse = response.body();
                    if (setResponse != null) {
                       if (setResponse.getSet_folders() != null && !setResponse.getSet_folders().isEmpty()) {
                           set_folders = setResponse.getSet_folders();
                           folderRecyclerViewAdapter.setBorderByArrayId(setResponse.getSet_folders());
                       }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SetResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addSet(int folder_id) {
        setVisible();
        FolderSetRequest folderSetRequest = new FolderSetRequest();
        folderSetRequest.setFolder_id(folder_id);
        folderSetRequest.setSet_id(set_id);
        Call<Void> folderSetResponseCall = ApiClient.getFolderService(getApplicationContext()).addSet(folderSetRequest);
        folderSetResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setInvisible();
                    set_folders.add(folder_id);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void removeSet(int folder_id) {
        setVisible();
        FolderSetRequest folderSetRequest = new FolderSetRequest();
        folderSetRequest.setFolder_id(folder_id);
        folderSetRequest.setSet_id(set_id);
        Call<Void> folderSetResponseCall = ApiClient.getFolderService(getApplicationContext()).deleteSet(folderSetRequest);
        folderSetResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setInvisible();
                    set_folders.remove(new Integer(folder_id));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setInvisible();
                set_folders.remove(new Integer(folder_id));
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