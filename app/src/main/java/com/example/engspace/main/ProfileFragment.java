package com.example.engspace.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.engspace.api.ApiClient;
import com.example.engspace.database.DatabaseHandler;
import com.example.engspace.model.DeleteUserResponse;
import com.example.engspace.profile.ManageFolderActivity;
import com.example.engspace.profile.ManageSetActivity;
import com.example.engspace.profile.ProfileActivity;
import com.example.engspace.R;
import com.example.engspace.login.Welcome;
import com.example.engspace.profile.EditEmailActivity;
import com.example.engspace.profile.EditInformationActivity;
import com.example.engspace.profile.EditPasswordActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //On Click avatar profile
        RelativeLayout relativeLayoutProfile = view.findViewById(R.id.btn_my_profile);
        relativeLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout btnManageSet = view.findViewById(R.id.btn_manage_set);
        btnManageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageSetActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout btnManageFolder = view.findViewById(R.id.btn_manage_folder);
        btnManageFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageFolderActivity.class);
                startActivity(intent);
            }
        });

        //Hien thi sharedpreferences voi 2 gia tri username va email ra layout
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        TextView profileUsername = (TextView) view.findViewById(R.id.profile_username);
        TextView profileEmail = (TextView) view.findViewById(R.id.profile_email);
        profileUsername.setText(sharedPreferences.getString("username", ""));
        profileEmail.setText(sharedPreferences.getString("email", ""));

        //Click button change email
        RelativeLayout btnChangeEmail = (RelativeLayout) view.findViewById(R.id.btn_email);
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), EditEmailActivity.class);
                startActivity(intent);
            }
        });

        //Click button change password
        RelativeLayout btnChangePassword = (RelativeLayout) view.findViewById(R.id.btn_password);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        //Click button change information
        RelativeLayout btnChangeInformation = (RelativeLayout) view.findViewById(R.id.btn_information);
        btnChangeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), EditInformationActivity.class);
                startActivity(intent);
            }
        });

        //Vuốt xuống để refresh lại trang, mục đích để refresh lại cái textview của email sau khi đổi email
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            profileUsername.setText(sharedPreferences.getString("username", ""));
            profileEmail.setText(sharedPreferences.getString("email", ""));
            swipeRefreshLayout.setRefreshing(false);
        });


        //Logout
        RelativeLayout logoutRelativeLayout = view.findViewById(R.id.btn_logout);
        logoutRelativeLayout.setOnClickListener(view13 -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_rounded_button_5);
            TextView content = dialog.findViewById(R.id.dialog_content);
            TextView cancelBtn = dialog.findViewById(R.id.dialog_cancel_btn);
            TextView confirmBtn = dialog.findViewById(R.id.dialog_confirm_btn);
            content.setText("Bạn chắc chắn muốn đăng xuất?");
            confirmBtn.setText("Đăng xuất");
            confirmBtn.setTextColor(getResources().getColor(R.color.es_text_red, null));
            dialog.show();
            cancelBtn.setOnClickListener(view1 -> dialog.dismiss());
            confirmBtn.setOnClickListener(view12 -> {
                dialog.dismiss();
                sharedPreferences.edit().putString("access", "").apply();
                sharedPreferences.edit().putString("refresh", "").apply();
                sharedPreferences.edit().putInt("user_id", 0).apply();
                sharedPreferences.edit().putString("username", "").apply();
                sharedPreferences.edit().putString("email", "").apply();
                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                databaseHandler.deleteAllSets();
                Intent i = new Intent(getActivity(), Welcome.class);
                startActivity(i);
                getActivity().finish();
            });
        });

        //Logout
        RelativeLayout deleteAccountRelativeLayout = view.findViewById(R.id.btn_delete_account);
        deleteAccountRelativeLayout.setOnClickListener(view14 -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_rounded_button_5);
            TextView content = dialog.findViewById(R.id.dialog_content);
            TextView cancelBtn = dialog.findViewById(R.id.dialog_cancel_btn);
            TextView confirmBtn = dialog.findViewById(R.id.dialog_confirm_btn);
            content.setText("Bạn chắc chắn muốn xóa hoàn toàn tài khoản này khỏi dịch vụ của chúng tôi?");
            confirmBtn.setText("Xóa tài khoản");
            confirmBtn.setTextColor(getResources().getColor(R.color.es_text_red, null));
            dialog.show();
            cancelBtn.setOnClickListener(view1 -> dialog.dismiss());
            confirmBtn.setOnClickListener(view12 -> {
                dialog.dismiss();
                Integer user_id = sharedPreferences.getInt("user_id", 0);
                Call<DeleteUserResponse> deleteUserResponseCall = ApiClient.getUserService(getContext()).deleteUser(user_id);
                deleteUserResponseCall.enqueue(new Callback<DeleteUserResponse>() {
                    @Override
                    public void onResponse(Call<DeleteUserResponse> call, Response<DeleteUserResponse> response) {
                        if (response.isSuccessful()) {
                            sharedPreferences.edit().putString("access", "").apply();
                            sharedPreferences.edit().putString("refresh", "").apply();
                            sharedPreferences.edit().putInt("user_id", 0).apply();
                            sharedPreferences.edit().putString("username", "").apply();
                            sharedPreferences.edit().putString("email", "").apply();
                            Intent i = new Intent(getActivity(), Welcome.class);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Không thể xóa tài khoản này", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteUserResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                });

            });
        });

        return view;
    }
}