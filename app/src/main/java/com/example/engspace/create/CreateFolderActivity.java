package com.example.engspace.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.folder.ListSetsByFolderActivity;
import com.example.engspace.model.FolderRequest;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FolderResponseError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFolderActivity extends AppCompatActivity {

    TextInputLayout nameInputLayout;
    TextInputLayout descriptionInputLayout;
    TextInputEditText nameInput;
    TextInputEditText descriptionInput;
    FrameLayout progressOverlay;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisible();
                FolderRequest folderRequest = new FolderRequest();
                folderRequest.setIs_public(true);
                folderRequest.setName(nameInput.getText().toString());
                folderRequest.setDescription(descriptionInput.getText().toString());
                Call<FolderResponse> folderResponseCall = ApiClient.getFolderService(getApplicationContext()).createFolders(folderRequest);
                folderResponseCall.enqueue(new Callback<FolderResponse>() {
                    @Override
                    public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                        setInvisible();
                        if (response.isSuccessful()) {
                            FolderResponse folderResponse = response.body();
                            Bundle bundle = new Bundle();
                            bundle.putInt("folder_id", folderResponse.getId());
                            Intent intent = new Intent(CreateFolderActivity.this, ListSetsByFolderActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
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

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}