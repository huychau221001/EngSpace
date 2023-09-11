package com.example.engspace.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engspace.R;
import com.example.engspace.adapter.cardmatchingactivity.SetDetailRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardMatchingActivity extends AppCompatActivity {
    Toast customToast;
    ArrayList<Integer> cards;
    int set_id;
    FrameLayout progressOverlay;
    ArrayList<SetDetailResponse> setDetailResponseArrayList;
    RecyclerView setDetailRecyclerView;
    SetDetailRecyclerViewAdapter setDetailRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_matching);

        Bundle bundle = getIntent().getExtras();
        set_id = 0;
        if (bundle != null) {
            set_id = bundle.getInt("set_id");
        }

        if (set_id == 0) {
            finish();
        }

        cards = new ArrayList<>();

        setDetailResponseArrayList = new ArrayList<>();

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = findViewById(R.id.progress_overlay);
        //Cho nó hiển thị lên
        setVisible();

        final MediaPlayer mp = MediaPlayer.create(CardMatchingActivity.this, R.raw.error);
        final MediaPlayer mp1 = MediaPlayer.create(CardMatchingActivity.this, R.raw.success);
        final MediaPlayer mp2 = MediaPlayer.create(CardMatchingActivity.this, R.raw.done);

        //Set detail RecyclerView
        setDetailRecyclerView = findViewById(R.id.rcv_card);
        setDetailRecyclerViewAdapter = new SetDetailRecyclerViewAdapter(getApplicationContext(), new SetDetailRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetDetailResponse> setDetailResponseArrayList = setDetailRecyclerViewAdapter.getData();
                //Select this set detail
                if (cards.contains(position)) {
                    cards.remove(new Integer(position));
                    setDetailRecyclerViewAdapter.setBorder(position);
                } else {
                    cards.add(position);
                    setDetailRecyclerViewAdapter.setBorder(position);
                    if (cards.size() == 2) {
                        if (setDetailRecyclerViewAdapter.getData().get(cards.get(0)).getId() == setDetailRecyclerViewAdapter.getData().get(cards.get(1)).getId()) {
                            if (mp1.isPlaying()) mp1.stop();
                            if (mp.isPlaying()) mp.stop();
                            mp1.start();
                            if (customToast != null) customToast.cancel();
                            customToast = CustomToast.makeText(CardMatchingActivity.this, "Chính xác", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS);
                            customToast.show();
                            if (cards.get(0) > cards.get(1)) {
                                setDetailRecyclerViewAdapter.removeItem(cards.get(0));
                                setDetailRecyclerViewAdapter.removeItem(cards.get(1));
                            } else {
                                setDetailRecyclerViewAdapter.removeItem(cards.get(1));
                                setDetailRecyclerViewAdapter.removeItem(cards.get(0));
                            }
                            if (setDetailRecyclerViewAdapter.getItemCount() == 0) {
                                Dialog dialog = new Dialog(CardMatchingActivity.this);
                                dialog.setContentView(R.layout.card_matching_dialog);
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_rounded_button_5);
                                TextView content = dialog.findViewById(R.id.success_content);
                                AppCompatButton confirmBtn = dialog.findViewById(R.id.dialog_confirm_btn);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                if (mp1.isPlaying()) mp1.stop();
                                if (mp.isPlaying()) mp.stop();
                                mp2.start();
                                confirmBtn.setOnClickListener(view12 -> {
                                    dialog.dismiss();
                                    finish();
                                });
                            }
                        } else {
                            if (mp1.isPlaying()) mp1.stop();
                            if (mp.isPlaying()) mp.stop();
                            mp.start();
                            if (customToast != null) customToast.cancel();
                            customToast = CustomToast.makeText(CardMatchingActivity.this, "Sai", CustomToast.LENGTH_SHORT, CustomToast.ERROR);
                            customToast.show();
                        }
                        cards = new ArrayList<>();
                        setDetailRecyclerViewAdapter.resetBorder();
                    }
                }

            }
        });
        GridLayoutManager topicGridLayoutManager = new GridLayoutManager(CardMatchingActivity.this, 3);
        setDetailRecyclerView.setLayoutManager(topicGridLayoutManager);
        getData(set_id);
        setDetailRecyclerView.setAdapter(setDetailRecyclerViewAdapter);

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
                        Collections.shuffle(setDetailResponseArrayList);
                        ArrayList<SetDetailResponse> setDetailResponseArrayList1 = new ArrayList<>();
                        ArrayList<SetDetailResponse> setDetailResponseArrayList2 = new ArrayList<>();
                        for (int i = 0; i < setDetailResponseArrayList.size(); i++) {
                            if (i < 6) {
                                SetDetailResponse setDetailResponse = new SetDetailResponse();
                                setDetailResponse.setTerm(setDetailResponseArrayList.get(i).getTerm());
                                setDetailResponse.setId(setDetailResponseArrayList.get(i).getId());
                                setDetailResponse.setType(0);
                                SetDetailResponse setDetailResponse1 = new SetDetailResponse();
                                setDetailResponse1.setDefinition(setDetailResponseArrayList.get(i).getDefinition());
                                setDetailResponse1.setId(setDetailResponseArrayList.get(i).getId());
                                setDetailResponse1.setType(1);
                                setDetailResponseArrayList1.add(setDetailResponse);
                                setDetailResponseArrayList1.add(setDetailResponse1);
                            }
                        }
                        Collections.shuffle(setDetailResponseArrayList1, new Random(12));
                        setDetailRecyclerViewAdapter.setData(setDetailResponseArrayList1);

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

    //2 cái này để hiển thị màn hình loading
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);
    }

    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
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
}