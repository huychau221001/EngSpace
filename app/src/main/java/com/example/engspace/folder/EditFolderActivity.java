package com.example.engspace.folder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.create.CreateFolderActivity;
import com.example.engspace.model.FolderRequest;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FolderResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFolderActivity extends AppCompatActivity {

    int folder_id;
    TextInputLayout nameInputLayout;
    TextInputLayout descriptionInputLayout;
    TextInputEditText nameInput;
    TextInputEditText descriptionInput;
    FrameLayout progressOverlay;
    ImageButton saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);

        Bundle bundle = getIntent().getExtras();
        folder_id = 0;
        if (bundle != null) {
            folder_id = bundle.getInt("folder_id");
        } else {
            finish();
        }

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);

        //Lấy cái layout của các khung input để set error khi có lỗi
        nameInputLayout = (TextInputLayout) findViewById(R.id.name_input_layout);
        descriptionInputLayout = (TextInputLayout) findViewById(R.id.description_input_layout);

        //Lấy button save để gọi api save khi ấn button
        saveButton = (ImageButton) findViewById(R.id.save_btn);

        //Lấy các khung input để hiển thị từ api và lấy để gọi api lưu lên backend
        nameInput = (TextInputEditText) findViewById(R.id.name_input);
        descriptionInput = (TextInputEditText) findViewById(R.id.description_input);

        getFolderById(folder_id);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisible();
                FolderRequest folderRequest = new FolderRequest();
                folderRequest.setIs_public(true);
                folderRequest.setName(nameInput.getText().toString());
                folderRequest.setDescription(descriptionInput.getText().toString());
                Call<FolderResponse> folderResponseCall = ApiClient.getFolderService(getApplicationContext()).updateFolder(folder_id, folderRequest);
                folderResponseCall.enqueue(new Callback<FolderResponse>() {
                    @Override
                    public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            FolderResponse folderResponse = response.body();
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Gson gson = new Gson();
                            FolderResponseError folderResponseError = gson.fromJson(response.errorBody().charStream(), FolderResponseError.class);
                            if (folderResponseError.getName() != null && !folderResponseError.getName().isEmpty()) {
                                nameInputLayout.setError(folderResponseError.getName().get(0));
                            } else {
                                nameInputLayout.setError(null);
                            }
                            if (folderResponseError.getDescription() != null && !folderResponseError.getDescription().isEmpty()) {
                                descriptionInputLayout.setError(folderResponseError.getDescription().get(0));
                            } else {
                                descriptionInputLayout.setError(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FolderResponse> call, Throwable t) {
                        setInvisible();
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                });
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

    private void getFolderById(int folder_id) {
        setVisible();
        Call<FolderResponse> folderResponseCall = ApiClient.getFolderService(getApplicationContext()).getFolder(folder_id);
        folderResponseCall.enqueue(new Callback<FolderResponse>() {
            @Override
            public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FolderResponse folderResponse = response.body();
                    if (folderResponse != null) {
                        nameInput.setText(folderResponse.getName());
                        descriptionInput.setText(folderResponse.getDescription());
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể lấy dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FolderResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
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