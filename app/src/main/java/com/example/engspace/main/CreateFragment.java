package com.example.engspace.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.engspace.R;
import com.example.engspace.create.CreateFolderActivity;
import com.example.engspace.create.CreateSetActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateFragment extends BottomSheetDialogFragment {
    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container,
                false);
        RelativeLayout btnCreateFolder = (RelativeLayout) view.findViewById(R.id.btn_create_folder);
        btnCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                final Intent intent = new Intent(getActivity(), CreateFolderActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout btnCreateSet = (RelativeLayout) view.findViewById(R.id.btn_create_set);
        btnCreateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                final Intent intent = new Intent(getActivity(), CreateSetActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
}