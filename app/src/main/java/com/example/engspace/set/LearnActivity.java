package com.example.engspace.set;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.learnactivity.SetDetailRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.database.DatabaseHandler;
import com.example.engspace.learn.CheckMatchingActivity;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetTable;
import com.example.engspace.model.UserResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearnActivity extends AppCompatActivity {
    Bundle bundle1;
    int set_id;
    RecyclerView recyclerView;
    SetDetailRecyclerViewAdapter setDetailRecyclerViewAdapter;
    FrameLayout progressOverlay;
    ImageButton optionsBtn;
    TextView noTerm;
    TextView setName;
    TextView setUserName;
    TextView setNumber;
    TextView setDescribe;
    TextToSpeech textToSpeech;
    boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        bundle1 = new Bundle();

        Bundle bundle = getIntent().getExtras();
        set_id = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id");
        }

        if (set_id == 0) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        //Khoi tao TextToSpeech
        ready = false;
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    setTextToSpeechLanguage();
                }
            }
        });

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        setName = findViewById(R.id.set_name);
        setUserName = findViewById(R.id.set_user_name);
        setNumber = findViewById(R.id.set_number);
        setDescribe = findViewById(R.id.set_describe);

        noTerm = findViewById(R.id.no_term);
        optionsBtn = findViewById(R.id.options_btn);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_learn_card);
        setDetailRecyclerViewAdapter = new SetDetailRecyclerViewAdapter(getApplicationContext(), new SetDetailRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                SetDetailResponse setDetailResponse = setDetailRecyclerViewAdapter.getData().get(position);
                if (setDetailResponse.getTerm_lang().equals("en")) {
                    speakOut(setDetailResponse.getTerm());
                } else {
                    speakOut(setDetailResponse.getDefinition());
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LearnActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getSetById(set_id);
        recyclerView.setAdapter(setDetailRecyclerViewAdapter);

        //Su kien an button "options"
        optionsBtn = (ImageButton) findViewById(R.id.options_btn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPanelBottomSheetDialogFragment setPanelBottomSheetDialogFragment = SetPanelBottomSheetDialogFragment.newInstance();
                bundle1 = new Bundle();
                bundle1.putInt("set_id", set_id);
                setPanelBottomSheetDialogFragment.setArguments(bundle1);
                setPanelBottomSheetDialogFragment.show(getSupportFragmentManager(),
                        "set_panel_dialog_fragment");
            }
        });

        //Hoc
        MaterialCardView btnLearn = findViewById(R.id.function1);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnActivity.this, CheckMatchingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("set_id", set_id);
                bundle2.putInt("function", 1);
                bundle2.putInt("amount", setDetailRecyclerViewAdapter.getItemCount());
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });

        //Ghep the
        MaterialCardView btnMatching = findViewById(R.id.function3);
        btnMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnActivity.this, CheckMatchingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("set_id", set_id);
                bundle2.putInt("function", 3);
                bundle2.putInt("amount", setDetailRecyclerViewAdapter.getItemCount());
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });

        //Back
        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void getSetById(int id) {
        Call<SetResponse> setResponseCall = ApiClient.getSetService(getApplicationContext()).getSet(id);
        setResponseCall.enqueue(new Callback<SetResponse>() {
            @Override
            public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetResponse setResponse = response.body();
                    if (setResponse != null) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(LearnActivity.this);
                        SetTable setTable = databaseHandler.getSet(set_id);
                        if (setTable == null) {
                            SetTable setTable1 = new SetTable(set_id, setResponse.getName(), setResponse.getSet_details().size());
                            databaseHandler.addSet(setTable1);
                        } else {
                            SetTable setTable1 = new SetTable(set_id, setResponse.getName(), setResponse.getSet_details().size());
                            databaseHandler.updateSet(setTable1);
                        }
                        if (setResponse.getSet_details().isEmpty()) {
                            noTerm.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noTerm.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        setName.setText(setResponse.getName());
                        setNumber.setText(String.valueOf(setResponse.getSet_details().size())+" thuật ngữ");
                        if (setResponse.getDescription().equals("")) {
                            setDescribe.setVisibility(View.GONE);
                        } else {
                            setDescribe.setText(setResponse.getDescription());
                            setDescribe.setVisibility(View.VISIBLE);
                        }
                        UserResponse userResponse = setResponse.getUser();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if (userResponse.getId() == sharedPreferences.getInt("user_id", 0)) {
                            optionsBtn.setVisibility(View.VISIBLE);
                        } else {
                            optionsBtn.setVisibility(View.GONE);
                        }
                        setUserName.setText(userResponse.getUsername());
                        setDetailRecyclerViewAdapter.setData(setResponse.getSet_details());
                        setDetailRecyclerViewAdapter.notifyDataSetChanged();
                    }
                } else {
                    DatabaseHandler databaseHandler = new DatabaseHandler(LearnActivity.this);
                    SetTable setTable = databaseHandler.getSet(set_id);
                    if (setTable != null) {
                        databaseHandler.deleteSet(set_id);
                    }
                    Toast.makeText(getApplicationContext(), "Không lấy được dữ liệu.", Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SetResponse> call, Throwable t) {
                DatabaseHandler databaseHandler = new DatabaseHandler(LearnActivity.this);
                SetTable setTable = databaseHandler.getSet(set_id);
                if (setTable != null) {
                    databaseHandler.deleteSet(set_id);
                }
                setInvisible();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra.", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_CANCELED);
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
                        getSetById(set_id);
                    }
                }
            });

    //Khoi tao ngon ngu va toc do doc TextToSpeech
    private void setTextToSpeechLanguage() {
        int result = textToSpeech.setLanguage(Locale.US);
        textToSpeech.setSpeechRate(0.60f);
        if (result == TextToSpeech.LANG_MISSING_DATA) {
            this.ready = false;
            Toast.makeText(this, "Missing language data", Toast.LENGTH_SHORT).show();
            return;
        } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            this.ready = false;
            return;
        } else {
            this.ready = true;
        }
    }

    //Doc TextToSpeech : contentA -> speech
    private void speakOut(String contentA) {
        if (!this.ready) {
            return;
        }
        String utteranceId = UUID.randomUUID().toString();
        textToSpeech.speak(contentA, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}