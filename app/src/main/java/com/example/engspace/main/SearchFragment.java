package com.example.engspace.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.engspace.R;
import com.example.engspace.adapter.homefragment.SetRecyclerViewAdapter;
import com.example.engspace.adapter.searchfragment.FolderResultRecyclerViewAdapter;
import com.example.engspace.adapter.searchfragment.SetResultRecyclerViewAdapter;
import com.example.engspace.adapter.searchfragment.TopicRecyclerViewAdapter;
import com.example.engspace.api.ApiClient;
import com.example.engspace.folder.ListSetsByFolderActivity;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FoldersResponse;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetsResponse;
import com.example.engspace.model.TopicResponse;
import com.example.engspace.model.TopicsResponse;
import com.example.engspace.set.LearnActivity;
import com.example.engspace.topic.ListSetsByTopicActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    RecyclerView topicRecyclerView;
    TopicRecyclerViewAdapter topicAdapter;
    SearchView searchView;
    RecyclerView setResultRecyclerView;
    SetResultRecyclerViewAdapter setResultRecyclerViewAdapter;
    RecyclerView folderResultRecyclerView;
    FolderResultRecyclerViewAdapter folderResultRecyclerViewAdapter;
    RelativeLayout searchResultContainer;
    AppCompatButton btnLoadMore;
    LinearLayout discoveryContainer;
    FrameLayout progressOverlay;
    Integer type;
    Integer page;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //ProgressOverlay hiển thị màn hình xám loading khi đang gọi api
        progressOverlay = view.findViewById(R.id.progress_overlay);

        //Khai bao searchview
        searchView = view.findViewById(R.id.main_search_view);

        //Khai bao bottomsheet
        View bottomSheetDialogView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.activity_search_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        RadioGroup numberTermRadioGroup = bottomSheetDialog.findViewById(R.id.number_term_radio_group);
        RadioGroup typeRadioGroup = bottomSheetDialog.findViewById(R.id.type_radio_group);
        type = 0;
        page = 1;
        int type_checked = typeRadioGroup.getCheckedRadioButtonId();
        if (type_checked==R.id.rbtn_type_folder) {
            type = 1;
        } else {
            type = 0;
        }
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                page = 1;
                if (i==R.id.rbtn_type_folder) {
                    type = 1;
                    if (!searchView.getQuery().toString().equals("")) {
                        getListFolderResults(searchView.getQuery().toString());
                    }
                } else {
                    type = 0;
                    if (!searchView.getQuery().toString().equals("")) {
                        getListSetResults(searchView.getQuery().toString());
                    }
                }
            }
        });

        discoveryContainer = view.findViewById(R.id.discovery_container);
        // Search
        searchResultContainer = view.findViewById(R.id.search_result_container);
        btnLoadMore = view.findViewById(R.id.load_more);
        //Set Search Result RecyclerView
        setResultRecyclerView = view.findViewById(R.id.rcv_container);
        setResultRecyclerViewAdapter = new SetResultRecyclerViewAdapter(getActivity().getApplicationContext(), new SetResultRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setResultRecyclerViewAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), LearnActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager setVerticalLayout1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        setResultRecyclerView.setLayoutManager(setVerticalLayout1);
        setResultRecyclerView.setAdapter(setResultRecyclerViewAdapter);
        //Folder Search Result RecyclerView
        folderResultRecyclerView = view.findViewById(R.id.rcv_container1);
        folderResultRecyclerViewAdapter = new FolderResultRecyclerViewAdapter(getActivity().getApplicationContext(),new FolderResultRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<FolderResponse> folderResponseArrayList = folderResultRecyclerViewAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("folder_id", folderResponseArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), ListSetsByFolderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager folderVerticalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        folderResultRecyclerView.setLayoutManager(folderVerticalLayout);
        folderResultRecyclerView.setAdapter(folderResultRecyclerViewAdapter);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    page = 1;
                    searchResultContainer.setVisibility(View.GONE);
                    btnLoadMore.setVisibility(View.GONE);
                    discoveryContainer.setVisibility(View.VISIBLE);
                } else {
                    page = 1;
                    if (type == 1) {
                        getListFolderResults(query);
                    } else {
                        getListSetResults(query);
                    }
                    searchResultContainer.setVisibility(View.VISIBLE);
                    btnLoadMore.setVisibility(View.VISIBLE);
                    discoveryContainer.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    page = 1;
                    searchResultContainer.setVisibility(View.GONE);
                    btnLoadMore.setVisibility(View.GONE);
                    discoveryContainer.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchView.getQuery().toString().equals("")) {
                    page++;
                    if (type == 0) {
                        getListSetResults(searchView.getQuery().toString());
                    } else {
                        getListFolderResults(searchView.getQuery().toString());
                    }
                }
            }
        });

        //Su kien an button "Loc"
        Button btnSearchFilter = (Button) view.findViewById(R.id.search_filter_btn);
        btnSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        //Set RecyclerView
        setRecyclerView = view.findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getActivity().getApplicationContext(), new SetRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<SetResponse> setResposeArrayList = setAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("set_id", setResposeArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), LearnActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager setHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        setHorizontalLayout.setStackFromEnd(true);
        setRecyclerView.setLayoutManager(setHorizontalLayout);
        getListSetsLatest();
        setRecyclerView.setAdapter(setAdapter);

        //Topic RecyclerView
        topicRecyclerView = view.findViewById(R.id.rcv_topic);
        topicAdapter = new TopicRecyclerViewAdapter(getActivity().getApplicationContext(), new TopicRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void itemClickAt(int position) {
                ArrayList<TopicResponse> topicResponseArrayList = topicAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putInt("topic_id", topicResponseArrayList.get(position).getId());
                Intent intent = new Intent(getActivity(), ListSetsByTopicActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager topicGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        topicRecyclerView.setLayoutManager(topicGridLayoutManager);
        getListTopics();
        topicRecyclerView.setAdapter(topicAdapter);



        //Vuốt xuống để refresh lại trang, mục đích để refresh lại data recycler view
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getListSetsLatest();
                getListTopics();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void getListSetsLatest() {
        Call<SetsResponse> setsResponseCall = ApiClient.getSetService(getContext()).getLatestSets();
        setsResponseCall.enqueue(new Callback<SetsResponse>() {
            @Override
            public void onResponse(Call<SetsResponse> call, Response<SetsResponse> response) {
                if (response.isSuccessful()) {
                    SetsResponse setsResponse = response.body();
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

    private void getListTopics() {
        Call<TopicsResponse> topicsResponseCall = ApiClient.getTopicService(getContext()).getTopics();
        topicsResponseCall.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                if (response.isSuccessful()) {
                    TopicsResponse topicsResponse = response.body();
                    topicAdapter.setData(topicsResponse.getResults());
                } else {
                    Toast.makeText(getActivity(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getListSetResults(String query) {
        folderResultRecyclerViewAdapter.setData(null);
        setVisible();
        Call<SetsResponse> setsResponseCall = ApiClient.getSetService(getContext()).searchSets(query, page);
        setsResponseCall.enqueue(new Callback<SetsResponse>() {
            @Override
            public void onResponse(Call<SetsResponse> call, Response<SetsResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    SetsResponse setsResponse = response.body();
                    if (page == 1) {
                        setResultRecyclerViewAdapter.setData(setsResponse.getResults());
                    } else {
                        ArrayList<SetResponse> setResponseArrayList = new ArrayList<>(setResultRecyclerViewAdapter.getData());
                        ArrayList<SetResponse> setResponseArrayList1 = new ArrayList<>(setsResponse.getResults());
                        setResponseArrayList1.removeAll(setResponseArrayList);
                        setResponseArrayList.addAll(setResponseArrayList1);
                        setResultRecyclerViewAdapter.setData(setResponseArrayList);
                    }
                } else {
                    if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Đang ở trang cuối cùng", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SetsResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getListFolderResults(String query) {
        setResultRecyclerViewAdapter.setData(null);
        setVisible();
        Call<FoldersResponse> foldersResponseCall = ApiClient.getFolderService(getContext()).searchFolders(query, page);
        foldersResponseCall.enqueue(new Callback<FoldersResponse>() {
            @Override
            public void onResponse(Call<FoldersResponse> call, Response<FoldersResponse> response) {
                setInvisible();
                if (response.isSuccessful()) {
                    FoldersResponse foldersResponse = response.body();
                    if (page == 1) {
                        folderResultRecyclerViewAdapter.setData(foldersResponse.getResults());
                    } else {
                        ArrayList<FolderResponse> folderResponseArrayList = new ArrayList<>(folderResultRecyclerViewAdapter.getData());
                        ArrayList<FolderResponse> folderResponseArrayList1 = new ArrayList<>(foldersResponse.getResults());
                        folderResponseArrayList1.removeAll(folderResponseArrayList);
                        folderResponseArrayList.addAll(folderResponseArrayList1);
                        folderResultRecyclerViewAdapter.setData(folderResponseArrayList);
                    }
                } else {
                    if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Đang ở trang cuối cùng", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FoldersResponse> call, Throwable t) {
                setInvisible();
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
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
}