package com.example.engspace.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.engspace.MainActivity;
import com.example.engspace.R;
import com.example.engspace.adapter.homefragment.BannerRecyclerViewAdapter;
import com.example.engspace.adapter.homefragment.FolderRecyclerViewAdapter;
import com.example.engspace.adapter.homefragment.SetRecentRecyclerViewAdapter;
import com.example.engspace.adapter.homefragment.SetRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.database.DatabaseHandler;
import com.example.engspace.folder.ListSetsByFolderActivity;
import com.example.engspace.model.Banner;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FoldersResponse;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetTable;
import com.example.engspace.model.SetsResponse;
import com.example.engspace.profile.ManageFolderActivity;
import com.example.engspace.profile.ManageSetActivity;
import com.example.engspace.profile.ProfileActivity;
import com.example.engspace.set.LearnActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView bannerRecyclerView;
    BannerRecyclerViewAdapter bannerAdapter;
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    RecyclerView folderRecyclerView;
    FolderRecyclerViewAdapter folderAdapter;
    RecyclerView setRecentRecyclerView;
    SetRecentRecyclerViewAdapter setRecentAdapter;
    TextView noSetTextView;
    TextView noFolderTextView;
    TextView noSetRecentTextView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SearchView searchView = view.findViewById(R.id.main_search_view);
        searchView.setInputType(InputType.TYPE_NULL);
        LinearLayout searchViewLayout = view.findViewById(R.id.main_search_view_layout);
        searchViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).moveToSearch();
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        TextView homeUsername = (TextView) view.findViewById(R.id.home_username);
        homeUsername.setText(sharedPreferences.getString("username", ""));

        // Su kien click vao avatar thi hien thi super profile activity cua ban than
        ImageView avatarImageView = (ImageView) view.findViewById(R.id.home_profile_btn);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        //Banner RecyclerView
        bannerRecyclerView = view.findViewById(R.id.rcv_banner);
        bannerAdapter = new BannerRecyclerViewAdapter(getActivity().getApplicationContext());
        LinearLayoutManager bannerHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecyclerView.setLayoutManager(bannerHorizontalLayout);
        bannerAdapter.setData(getListBanner());
        bannerRecyclerView.setAdapter(bannerAdapter);

        //Set RecyclerView
        noSetTextView = view.findViewById(R.id.no_set);
        setRecyclerView = view.findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getActivity().getApplicationContext(), new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), LearnActivity.class);
                intent.putExtras(bundle);
                someActivityResultLauncher.launch(intent);
            }
        });
        LinearLayoutManager setHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        setRecyclerView.setLayoutManager(setHorizontalLayout);
        getListSetsByUser(sharedPreferences.getInt("user_id", 0));
        setRecyclerView.setAdapter(setAdapter);
        LinearLayout btnViewAllSet = view.findViewById(R.id.view_all_set_btn);
        btnViewAllSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageSetActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        //Folder RecyclerView
        noFolderTextView = view.findViewById(R.id.no_folder);
        folderRecyclerView = view.findViewById(R.id.rcv_folder);
        folderAdapter = new FolderRecyclerViewAdapter(getActivity().getApplicationContext(), new FolderRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<FolderResponse> folderResponseArrayList = folderAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folderResponseArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), ListSetsByFolderActivity.class);
                intent.putExtras(bundle);
                someActivityResultLauncher.launch(intent);
            }
        });
        LinearLayoutManager folderHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        folderRecyclerView.setLayoutManager(folderHorizontalLayout);
        getListFolders(sharedPreferences.getInt("user_id", 0));
        folderRecyclerView.setAdapter(folderAdapter);
        LinearLayout btnViewAllFolder = view.findViewById(R.id.view_all_folder_btn);
        btnViewAllFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageFolderActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        //Set Recent RecyclerView
        noSetRecentTextView = view.findViewById(R.id.no_set_recent);
        setRecentRecyclerView = view.findViewById(R.id.rcv_recent_set);
        setRecentAdapter = new SetRecentRecyclerViewAdapter(getActivity().getApplicationContext(), new SetRecentRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetTable> setTableArrayList = setRecentAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setTableArrayList.get(position).getSet());
                Intent intent = new Intent(getActivity(), LearnActivity.class);
                intent.putExtras(bundle);
                someActivityResultLauncher.launch(intent);
            }
        });
        LinearLayoutManager setRecentVerticalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        setRecentRecyclerView.setLayoutManager(setRecentVerticalLayout);
        getListRecentSets();
        setRecentRecyclerView.setAdapter(setRecentAdapter);


        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListSetsByUser(sharedPreferences.getInt("user_id", 0));
                getListFolders(sharedPreferences.getInt("user_id", 0));
                getListRecentSets();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayout btnDeleteSetRecent = view.findViewById(R.id.delete_set_recent);
        btnDeleteSetRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                databaseHandler.deleteAllSets();
                getListRecentSets();
            }
        });

        return view;
    }

    private List<Banner> getListBanner() {
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner("Learning", "Never Stop Learning.\nBecause life never stop Teaching.", "#26202C", R.drawable.ad_1));
        bannerList.add(new Banner("Sharing", "Share your experience with others.\nBecause it's free.", "#FEC260", R.drawable.ad_3));
        bannerList.add(new Banner("Improving", "Try hard. Fighting more!\nBe the wisest learner.", "#17B978", R.drawable.ad_2));
        return bannerList;
    }

    private void getListSetsByUser(int user_id) {
        Call<SetsResponse> setsResponseCall = ApiClient.getSetService(getContext()).getSetsByUser(user_id);
        setsResponseCall.enqueue(new Callback<SetsResponse>() {
            @Override
            public void onResponse(Call<SetsResponse> call, Response<SetsResponse> response) {
                if (response.isSuccessful()) {
                    SetsResponse setsResponse = response.body();
                    if (setsResponse.getResults().isEmpty()) {
                        noSetTextView.setVisibility(View.VISIBLE);
                        setRecyclerView.setVisibility(View.GONE);
                    } else {
                        noSetTextView.setVisibility(View.GONE);
                        setRecyclerView.setVisibility(View.VISIBLE);
                    }
                    setAdapter.setData(setsResponse.getResults());
                } else {
                    Toast.makeText(getActivity(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SetsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getListRecentSets() {
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<SetTable> setTableArrayList = databaseHandler.getAllSets();
        if (setTableArrayList.isEmpty()) {
            setRecentRecyclerView.setVisibility(View.GONE);
            noSetRecentTextView.setVisibility(View.VISIBLE);
        } else {
            setRecentRecyclerView.setVisibility(View.VISIBLE);
            noSetRecentTextView.setVisibility(View.GONE);
        }
        setRecentAdapter.setData(setTableArrayList);
    }

    private void getListFolders(int user_id) {
        Call<FoldersResponse> foldersResponseCall = ApiClient.getFolderService(getContext()).getFoldersByUser(user_id);
        foldersResponseCall.enqueue(new Callback<FoldersResponse>() {
            @Override
            public void onResponse(Call<FoldersResponse> call, Response<FoldersResponse> response) {
                if (response.isSuccessful()) {
                    FoldersResponse foldersResponse = response.body();
                    if (foldersResponse.getResults().isEmpty()) {
                        noFolderTextView.setVisibility(View.VISIBLE);
                        folderRecyclerView.setVisibility(View.GONE);
                    } else {
                        noFolderTextView.setVisibility(View.GONE);
                        folderRecyclerView.setVisibility(View.VISIBLE);
                    }
                    folderAdapter.setData(foldersResponse.getResults());
                } else {
                    Toast.makeText(getActivity(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FoldersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK || result.getResultCode() == Activity.RESULT_CANCELED) {
                        getListRecentSets();
                    }
                }
            });
}