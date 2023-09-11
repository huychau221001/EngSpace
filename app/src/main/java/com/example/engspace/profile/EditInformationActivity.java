package com.example.engspace.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.UserRequest;
import com.example.engspace.model.UserResponse;
import com.example.engspace.model.UserResponseError;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInformationActivity extends AppCompatActivity {

    TextInputLayout firstnameInputLayout;
    TextInputLayout lastnameInputLayout;
    TextInputLayout websiteInputLayout;
    TextInputLayout fbInputLayout;
    TextInputLayout bioInputLayout;
    TextInputLayout dobInputLayout;
    TextInputEditText firstnameInput;
    TextInputEditText lastnameInput;
    TextInputEditText websiteInput;
    TextInputEditText fbInput;
    TextInputEditText bioInput;
    TextInputEditText dobInput;
    FrameLayout progressOverlay;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        //Lấy cái layout của các khung input để set error khi có lỗi
        firstnameInputLayout = (TextInputLayout) findViewById(R.id.firstname_input_layout);
        lastnameInputLayout = (TextInputLayout) findViewById(R.id.lastname_input_layout);
        websiteInputLayout = (TextInputLayout) findViewById(R.id.website_input_layout);
        fbInputLayout = (TextInputLayout) findViewById(R.id.fb_input_layout);
        bioInputLayout = (TextInputLayout) findViewById(R.id.bio_input_layout);
        dobInputLayout = (TextInputLayout) findViewById(R.id.dob_input_layout);

        //Lấy button save để gọi api save khi ấn button
        saveButton = (ImageButton) findViewById(R.id.save_btn);

        //Lấy các khung input để hiển thị từ api và lấy để gọi api lưu lên backend
        firstnameInput = (TextInputEditText) findViewById(R.id.firstname_input);
        lastnameInput = (TextInputEditText) findViewById(R.id.lastname_input);
        websiteInput = (TextInputEditText) findViewById(R.id.website_input);
        fbInput = (TextInputEditText) findViewById(R.id.fb_input);
        bioInput = (TextInputEditText) findViewById(R.id.bio_input);
        dobInput = (TextInputEditText) findViewById(R.id.dob_input);

        Call<UserResponse> userResponseCall = ApiClient.getUserService(getApplicationContext()).getUserProfile();
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                //Tắt cái ProgressOverlay đi vì gọi api xong
                setInvisible();
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    firstnameInput.setText(userResponse.getFirst_name());
                    lastnameInput.setText(userResponse.getLast_name());
                    websiteInput.setText(userResponse.getWebsite());
                    fbInput.setText(userResponse.getFb_url());
                    bioInput.setText(userResponse.getBio());
                    dobInput.setText(userResponse.getDobString());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //Tắt cái ProgressOverlay đi vì gọi api thất bại
                setInvisible();
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }
        });

        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);

        CalendarConstraints.Builder calendarConstraintBuilder = setupConstraintsBuilder(currentYear, currentMonth, currentDay);

        final MaterialDatePicker.Builder materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        materialDatePickerBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        materialDatePickerBuilder.setTitleText("Chọn ngày sinh");
        materialDatePickerBuilder.setTheme(R.style.MaterialCalendarTheme);
        materialDatePickerBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        final MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();

        dobInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_CALENDER_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date((Long) selection);
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dobInput.setText(simpleDateFormat.format(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cho cái ProgressOverlay hiển thị lên vì đang gọi api save dữ liệu lên backend
                setVisible();
                UserRequest userRequest = new UserRequest();
                userRequest.setLast_name(lastnameInput.getText().toString());
                userRequest.setFirst_name(firstnameInput.getText().toString());
                userRequest.setWebsite(websiteInput.getText().toString());
                userRequest.setBio(bioInput.getText().toString());
                userRequest.setFb_url(fbInput.getText().toString());
                userRequest.setDob(dobInput.getText().toString());
                Call<UserResponse> userResponseCall1 = ApiClient.getUserService(getApplicationContext()).updateUserProfile(userRequest);
                userResponseCall1.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        //Tắt cái ProgressOverlay đi vì gọi api xong
                        setInvisible();
                        if (response.isSuccessful()) {
                            finish();
                        } else {
                            Gson gson = new Gson();
                            UserResponseError userResponseError = gson.fromJson(response.errorBody().charStream(), UserResponseError.class);
                            if (userResponseError.getFirst_name() != null && !userResponseError.getFirst_name().isEmpty()) {
                                firstnameInputLayout.setError(userResponseError.getFirst_name().get(0));
                            }
                            if (userResponseError.getLast_name() != null && !userResponseError.getLast_name().isEmpty()) {
                                lastnameInputLayout.setError(userResponseError.getLast_name().get(0));
                            }
                            if (userResponseError.getWebsite() != null && !userResponseError.getWebsite().isEmpty()) {
                                websiteInputLayout.setError(userResponseError.getWebsite().get(0));
                            }
                            if (userResponseError.getFb_url() != null && !userResponseError.getFb_url().isEmpty()) {
                                fbInputLayout.setError(userResponseError.getFb_url().get(0));
                            }
                            if (userResponseError.getBio() != null && !userResponseError.getBio().isEmpty()) {
                                bioInputLayout.setError(userResponseError.getBio().get(0));
                            }
                            if (userResponseError.getDob() != null && !userResponseError.getDob().isEmpty()) {
                                dobInputLayout.setError(userResponseError.getDob().get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        //Tắt cái ProgressOverlay đi vì gọi api thất bại
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

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    private CalendarConstraints.Builder setupConstraintsBuilder(int currentYear, int currentMonth, int currentDay) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(1960, 01, 01);
        Calendar c_end = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c_end.set(currentYear, currentMonth, currentDay);

        final long DEFAULT_START = c.getTimeInMillis();
        final long DEFAULT_END = c_end.getTimeInMillis();

        constraintsBuilder.setStart(DEFAULT_START);
        constraintsBuilder.setEnd(DEFAULT_END);
        constraintsBuilder.setOpenAt(DEFAULT_END);
        return constraintsBuilder;
    }
}