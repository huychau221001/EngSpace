package com.example.engspace.set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.engspace.R;
import com.example.engspace.api.ApiClient;
import com.example.engspace.login.Welcome;
import com.example.engspace.model.SetResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPanelBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static SetPanelBottomSheetDialogFragment newInstance() {
        return new SetPanelBottomSheetDialogFragment();
    }

    int set_id;
    ArrayList<Integer> set_folders;
    RelativeLayout addBtn;
    RelativeLayout editBtn;
    RelativeLayout deleteBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_control_panel_bottom_sheet, container,
                false);
        if (getArguments() != null) {
            set_id = getArguments().getInt("set_id", 0);
        } else {
            dismiss();
        }
        //them hoc phan vao thu muc
        addBtn = view.findViewById(R.id.panel_add_to_folder_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(getActivity(), AddSetToFolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", set_id);
                intent.putExtras(bundle);
                ((LearnActivity)getActivity()).someActivityResultLauncher.launch(intent);
            }
        });

        //sua hoc phan
        editBtn = view.findViewById(R.id.panel_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", set_id);
                Intent intent = new Intent(getActivity(), EditSetActivity.class);
                intent.putExtras(bundle);
                ((LearnActivity)getActivity()).someActivityResultLauncher.launch(intent);
            }
        });

        //delete hoc phan
        deleteBtn = view.findViewById(R.id.panel_del_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_rounded_button_5);
                TextView content = dialog.findViewById(R.id.dialog_content);
                TextView cancelBtn = dialog.findViewById(R.id.dialog_cancel_btn);
                TextView confirmBtn = dialog.findViewById(R.id.dialog_confirm_btn);
                content.setText("Bạn chắc chắn muốn xóa hoàn toàn học phần này?");
                confirmBtn.setText("Xóa học phần");
                confirmBtn.setTextColor(getResources().getColor(R.color.es_text_red, null));
                dialog.show();
                cancelBtn.setOnClickListener(view1 -> {
                    dialog.dismiss();
                });
                confirmBtn.setOnClickListener(view12 -> {
                    dialog.dismiss();
                    Call<SetResponse> setResponseCall = ApiClient.getSetService(getContext()).deleteSet(set_id);
                    setResponseCall.enqueue(new Callback<SetResponse>() {
                        @Override
                        public void onResponse(Call<SetResponse> call, Response<SetResponse> response) {
                            if (response.isSuccessful()) {
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Không thể xóa học phần này", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<SetResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                        }
                    });
                });
            }
        });
        //

        return view;

    }
}
