package com.example.engspace.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardLearnActivity extends AppCompatActivity {
    Toast customToast;
    int set_id;
    FrameLayout progressOverlay;
    int current;
    ArrayList<SetDetailResponse> setDetailResponseArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_learn);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Bundle bundle = getIntent().getExtras();
        set_id = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id");
        }

        if (set_id == 0) {
            finish();
        }

        current = 1;

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        getData(set_id);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(int id) {
        Call<SetResponse> setResponseCall = ApiClient.getSetService(getApplicationContext()).getSet(id);
        setResponseCall.enqueue(new Callback<SetResponse>() {
            @Override
            public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetResponse setResponse = response.body();
                    if (setResponse != null) {
                        setDetailResponseArrayList = setResponse.getSet_details();
                        showLayout();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không lấy được dữ liệu.", Toast.LENGTH_LONG).show();
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

    private void showLayout() {
        Collections.shuffle(setDetailResponseArrayList);
        final MediaPlayer mp = MediaPlayer.create(CardLearnActivity.this, R.raw.error);
        final MediaPlayer mp1 = MediaPlayer.create(CardLearnActivity.this, R.raw.success);
        final MediaPlayer mp2 = MediaPlayer.create(CardLearnActivity.this, R.raw.done);
        int totalSize = setDetailResponseArrayList.size();
        TextView progress = findViewById(R.id.progress);
        TextView cardTerm = findViewById(R.id.card_term);
        TextView cardDefinition = findViewById(R.id.card_definition);
        LinearLayout cardDefinitionLayout = findViewById(R.id.card_definition_layout);
        TextInputLayout definitionInputLayout = findViewById(R.id.definition_input_layout);
        TextInputEditText definitionInput = findViewById(R.id.definition_input);
        AppCompatButton btnHint = findViewById(R.id.hint_btn);
        AppCompatButton btnCheck = findViewById(R.id.check_btn);
        progress.setText(current + "/" + totalSize);
        cardTerm.setText(setDetailResponseArrayList.get(current-1).getTerm());
        cardDefinition.setText(setDetailResponseArrayList.get(current-1).getDefinition());

        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardDefinitionLayout.setVisibility(View.VISIBLE);
                btnHint.setVisibility(View.GONE);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customToast != null) customToast.cancel();
                String text1 = Normalizer.normalize(convert(definitionInput.getText().toString().trim()), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
                String text2 = Normalizer.normalize(convert(cardDefinition.getText().toString().trim()), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
                if (text1.equalsIgnoreCase(text2)) {
                    if (mp1.isPlaying()) mp.stop();
                    mp1.start();
                    customToast = CustomToast.makeText(CardLearnActivity.this, "Chính xác", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS);
                    customToast.show();
                    if (current < totalSize) current++;
                    else {
                        if (mp1.isPlaying()) mp1.stop();
                        if (mp.isPlaying()) mp.stop();
                        mp2.start();
                        Dialog dialog = new Dialog(CardLearnActivity.this);
                        dialog.setContentView(R.layout.card_matching_dialog);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_rounded_button_5);
                        TextView content = dialog.findViewById(R.id.success_content);
                        AppCompatButton confirmBtn = dialog.findViewById(R.id.dialog_confirm_btn);
                        content.setText("Bạn đã hoàn thành bài học");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        confirmBtn.setOnClickListener(view12 -> {
                            dialog.dismiss();
                            finish();
                        });
                    }
                    progress.setText(current + "/" + totalSize);
                    cardTerm.setText(setDetailResponseArrayList.get(current-1).getTerm());
                    cardDefinition.setText(setDetailResponseArrayList.get(current-1).getDefinition());
                    cardDefinitionLayout.setVisibility(View.GONE);
                    btnHint.setVisibility(View.VISIBLE);
                    definitionInput.setText("");
                } else {
                    if (mp.isPlaying()) mp.stop();
                    mp.start();
                    definitionInput.setText("");
                    customToast = CustomToast.makeText(CardLearnActivity.this, "Sai", CustomToast.LENGTH_SHORT, CustomToast.ERROR);
                    customToast.show();
                }
            }
        });
    }

    public static String convert(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        return str;
    }

    public static class CustomToast extends Toast {

        public static int SUCCESS = 1;
        public static int ERROR = 2;

        private long SHORT = 4000;
        private long LONG = 7000;

        public CustomToast(Context context) {
            super(context);
        }

        public static Toast makeText(Context context, String message, int duration, int type) {
            Toast toast = new Toast(context);
            toast.setDuration(duration);
            View layout = LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false);
            TextView l1 = (TextView) layout.findViewById(R.id.toast_text);
            LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.toast_type);
            ImageView img = (ImageView) layout.findViewById(R.id.toast_icon);
            l1.setText(message);
            if (type == 1) {
                linearLayout.setBackgroundResource(R.drawable.success_shape);
                img.setImageResource(R.drawable.ic_outline_check_24);
            } else if (type == 2) {
                linearLayout.setBackgroundResource(R.drawable.error_shape);
                img.setImageResource(R.drawable.ic_close);
            }
            toast.setView(layout);
            return toast;
        }
    }

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
    }
}