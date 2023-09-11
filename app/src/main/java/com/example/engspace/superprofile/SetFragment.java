package com.example.engspace.superprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engspace.R;
import com.example.engspace.adapter.setfragment.SetRecyclerViewAdapter;
import com.example.engspace.model.Set;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView setRecyclerView;
    SetRecyclerViewAdapter setAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetFragment newInstance(String param1, String param2) {
        SetFragment fragment = new SetFragment();
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
        View view = inflater.inflate(R.layout.fragment_set, container, false);

        //Set RecyclerView
        setRecyclerView = view.findViewById(R.id.rcv_set);
        setAdapter = new SetRecyclerViewAdapter(getActivity().getApplicationContext());
        LinearLayoutManager setHorizontalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        setRecyclerView.setLayoutManager(setHorizontalLayout);
        setAdapter.setData(getListSet());
        setRecyclerView.setAdapter(setAdapter);

        return view;
    }

    private List<Set> getListSet() {
        List<Set> setList = new ArrayList<>();
        setList.add(new Set("Khoa học", "Administrator", "01/11/2021", 15, R.drawable.d1));
        setList.add(new Set("Công nghệ", "Administrator", "01/11/2021", 15, R.drawable.d1));
        setList.add(new Set("Công nghệ", "Administrator", "01/11/2021", 15, R.drawable.d1));
        setList.add(new Set("Công nghệ", "Administrator", "01/11/2021", 15, R.drawable.d1));
        setList.add(new Set("Công nghệ", "Administrator", "01/11/2021", 15, R.drawable.d1));
        setList.add(new Set("Công nghệ", "Administrator", "01/11/2021", 15, R.drawable.d1));
        return setList;
    }
}