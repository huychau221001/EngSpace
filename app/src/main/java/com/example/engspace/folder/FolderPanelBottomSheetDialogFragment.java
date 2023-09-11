package com.example.engspace.folder;

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
import com.example.engspace.folder.AddFolderToSetActivity;
import com.example.engspace.folder.ListSetsByFolderActivity;
import com.example.engspace.login.Welcome;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.SetResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderPanelBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static FolderPanelBottomSheetDialogFragment newInstance() {
        return new FolderPanelBottomSheetDialogFragment();
    }

    int folder_id;
    RelativeLayout addBtn;
    RelativeLayout editBtn;
    RelativeLayout deleteBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.folder_control_panel_bottom_sheet, container,
                false);
        if (getArguments() != null) {
            folder_id = getArguments().getInt("folder_id", 0);
        } else {
            dismiss();
        }
        //them hoc phan vao thu muc
        addBtn = view.findViewById(R.id.panel_add_set_to_folder_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(getActivity(), AddFolderToSetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folder_id);
                intent.putExtras(bundle);
                ((ListSetsByFolderActivity)getActivity()).someActivityResultLauncher.launch(intent);
            }
        });

        //sua hoc phan
        editBtn = view.findViewById(R.id.panel_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folder_id);
                Intent intent = new Intent(getActivity(), EditFolderActivity.class);
                intent.putExtras(bundle);
                ((ListSetsByFolderActivity)getActivity()).someActivityResultLauncher.launch(intent);
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
                content.setText("Bạn chắc chắn muốn xóa hoàn toàn thư mục này?");
                confirmBtn.setText("Xóa thư mục");
                confirmBtn.setTextColor(getResources().getColor(R.color.es_text_red, null));
                dialog.show();
                cancelBtn.setOnClickListener(view1 -> {
                    dialog.dismiss();
                });
                confirmBtn.setOnClickListener(view12 -> {
                    dialog.dismiss();
                    Call<FolderResponse> setResponseCall = ApiClient.getFolderService(getContext()).deleteFolder(folder_id);
                    setResponseCall.enqueue(new Callback<FolderResponse>() {
                        @Override
                        public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                            if (response.isSuccessful()) {
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Không thể xóa thư mục này", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<FolderResponse> call, Throwable t) {
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
