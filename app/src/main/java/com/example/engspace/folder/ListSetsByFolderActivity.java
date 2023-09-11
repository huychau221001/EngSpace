package com.example.engspace.folder;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.folderactivity.SetRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.UserResponse;
import com.example.engspace.set.LearnActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSetsByFolderActivity extends AppCompatActivity {
    ImageButton optionsBtn;
    int folder_id;
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    TextView noSet;
    TextView folderName;
    ImageView folderUserPhoto;
    TextView folderAmount;
    TextView folderDescription;
    FrameLayout progressOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sets_by_folder);

        optionsBtn = findViewById(R.id.options_btn);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        Bundle bundle = getIntent().getExtras();
        folder_id = 0;
        if (bundle != null) {
            folder_id = bundle.getInt("folder_id");
        }

        folderName = findViewById(R.id.folder_name);
        folderUserPhoto = findViewById(R.id.folder_user_photo);
        folderAmount = findViewById(R.id.folder_user);
        folderDescription = findViewById(R.id.folder_describe);

        //Set RecyclerView
        noSet = findViewById(R.id.no_set);
        setRecyclerView = findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getApplicationContext(), new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(ListSetsByFolderActivity.this, LearnActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager setVerticalLayout = new LinearLayoutManager(ListSetsByFolderActivity.this, LinearLayoutManager.VERTICAL, false);
        setRecyclerView.setLayoutManager(setVerticalLayout);
        getListSetsByFolder(folder_id);
        setRecyclerView.setAdapter(setAdapter);

        //Su kien an button "options"
        optionsBtn = (ImageButton) findViewById(R.id.options_btn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FolderPanelBottomSheetDialogFragment folderPanelBottomSheetDialogFragment = FolderPanelBottomSheetDialogFragment.newInstance();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("folder_id", folder_id);
                folderPanelBottomSheetDialogFragment.setArguments(bundle1);
                folderPanelBottomSheetDialogFragment.show(getSupportFragmentManager(),
                        "folder_panel_dialog_fragment");
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListSetsByFolder(folder_id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void getListSetsByFolder(int folder_id) {
        Call<FolderResponse> folderResponseCall = ApiClient.getFolderService(getApplicationContext()).getFolder(folder_id);
        folderResponseCall.enqueue(new Callback<FolderResponse>() {
            @Override
            public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FolderResponse folderResponse = response.body();
                    if (folderResponse != null) {
                        if (folderResponse.getSets().isEmpty()) {
                            noSet.setVisibility(View.VISIBLE);
                            setRecyclerView.setVisibility(View.GONE);
                        } else {
                            noSet.setVisibility(View.GONE);
                            setRecyclerView.setVisibility(View.VISIBLE);
                        }
                        folderName.setText(folderResponse.getName());
                        folderUserPhoto.setImageResource(R.drawable.d1);
                        UserResponse userResponse = folderResponse.getUser();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if (userResponse.getId() == sharedPreferences.getInt("user_id", 0)) {
                            optionsBtn.setVisibility(View.VISIBLE);
                        } else {
                            optionsBtn.setVisibility(View.GONE);
                        }
                        folderAmount.setText(userResponse.getUsername() + " | " + String.valueOf(folderResponse.getSets().size())+" học phần");
                        folderDescription.setText(folderResponse.getDescription());
                        setAdapter.setData(folderResponse.getSets());
                    }
                } else {
                    Toast.makeText(ListSetsByFolderActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FolderResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(ListSetsByFolderActivity.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getListSetsByFolder(folder_id);
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