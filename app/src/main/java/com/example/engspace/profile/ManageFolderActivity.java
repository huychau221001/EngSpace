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
import com.example.engspace.adapter.profileactivity.FolderRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.folder.ListSetsByFolderActivity;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FoldersResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageFolderActivity extends AppCompatActivity {
    RecyclerView folderRecyclerView;
    FolderRecyclerViewAdapter folderRecyclerViewAdapter;
    FrameLayout progressOverlay;
    TextView noFolderTextView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_folder);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Folder RecyclerView
        noFolderTextView = findViewById(R.id.no_folder);
        folderRecyclerView = findViewById(R.id.rcv_folder);
        folderRecyclerViewAdapter = new FolderRecyclerViewAdapter(getApplicationContext(), new FolderRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<FolderResponse> folderResponseArrayList = folderRecyclerViewAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folderResponseArrayList.get(position).getId());
                Intent intent = new Intent(ManageFolderActivity.this, ListSetsByFolderActivity.class);
                intent.putExtras(bundle);
                someActivityResultLauncher.launch(intent);
            }
        });
        LinearLayoutManager setVerticalLayout = new LinearLayoutManager(ManageFolderActivity.this, LinearLayoutManager.VERTICAL, false);
        folderRecyclerView.setLayoutManager(setVerticalLayout);
        getListFoldersByUser(sharedPreferences.getInt("user_id", 0));
        folderRecyclerView.setAdapter(folderRecyclerViewAdapter);

        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListFoldersByUser(sharedPreferences.getInt("user_id", 0));
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

    private void getListFoldersByUser(int user_id) {
        Call<FoldersResponse> foldersResponseCall = ApiClient.getFolderService(getApplicationContext()).getFoldersByUser(user_id);
        foldersResponseCall.enqueue(new Callback<FoldersResponse>() {
            @Override
            public void onResponse(Call<FoldersResponse> call, Response<FoldersResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FoldersResponse foldersResponse = response.body();
                    if (foldersResponse.getResults().isEmpty()) {
                        noFolderTextView.setVisibility(View.VISIBLE);
                        folderRecyclerView.setVisibility(View.GONE);
                    } else {
                        noFolderTextView.setVisibility(View.GONE);
                        folderRecyclerView.setVisibility(View.VISIBLE);
                    }
                    folderRecyclerViewAdapter.setData(foldersResponse.getResults());
                } else {
                    Toast.makeText(ManageFolderActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FoldersResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(ManageFolderActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getListFoldersByUser(sharedPreferences.getInt("user_id", 0));
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