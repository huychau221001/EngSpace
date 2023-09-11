package com.example.engspace.superprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.adapter.folderfragment.FolderRecyclerViewAdapter;
import com.example.engspace.model.Folder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView folderRecyclerView;
    FolderRecyclerViewAdapter folderAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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
        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        //Folder RecyclerView
        folderRecyclerView = view.findViewById(R.id.rcv_folder);
        folderAdapter = new FolderRecyclerViewAdapter(getActivity().getApplicationContext());
        LinearLayoutManager folderHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        folderRecyclerView.setLayoutManager(folderHorizontalLayout);
        folderAdapter.setData(getListFolder());
        folderRecyclerView.setAdapter(folderAdapter);

        return view;
    }

    private List<Folder> getListFolder() {
        List<Folder> folderList = new ArrayList<>();
        folderList.add(new Folder("Folder 1", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 2", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 3", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 4", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 5", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 6", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 7", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 8", "Administrator", 15, R.drawable.d1));
        folderList.add(new Folder("Folder 9", "Administrator", 15, R.drawable.d1));
        return folderList;
    }
}