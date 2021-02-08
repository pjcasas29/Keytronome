package com.example.keytronome.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartingKeyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartingKeyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivityViewModel viewModel;
    PickerAdapter adapter;

    public StartingKeyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartingKeyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartingKeyFragment newInstance(String param1, String param2) {
        StartingKeyFragment fragment = new StartingKeyFragment();
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
        return inflater.inflate(R.layout.fragment_starting_key, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //Set the scrollablle tempo
        RecyclerView recyclerViewKeys = getActivity().findViewById(R.id.rvStartingKey);
        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getActivity(), PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setScaleDownBy(0.99f);
        pickerLayoutManager.setScaleDownDistance(0.95f);

        ArrayList<String> keysList = viewModel.getKeys();

        adapter = new PickerAdapter(getActivity(), keysList, recyclerViewKeys);
        recyclerViewKeys.setAdapter(adapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewKeys);
        recyclerViewKeys.setLayoutManager(pickerLayoutManager);
        pickerLayoutManager.scrollToPosition(keysList.indexOf(viewModel.getStartingKey().getValue()));
        pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                TextView selectedView = (TextView) view;
                viewModel.setStartingKey((String) selectedView.getText());
            }
        });
    }
}