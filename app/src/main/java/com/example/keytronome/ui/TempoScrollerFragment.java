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
 * Use the {@link TempoScrollerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempoScrollerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvHorizontalPicker;
    private TextView tvSelectedItem;
    PickerAdapter adapter;

    MainActivityViewModel viewModel;

    public TempoScrollerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TempoScrollerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TempoScrollerFragment newInstance(String param1, String param2) {
        TempoScrollerFragment fragment = new TempoScrollerFragment();
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

        return inflater.inflate(R.layout.fragment_tempo_scroller, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //Set the scrollablle tempo
        RecyclerView recyclerViewTempo = getActivity().findViewById(R.id.rvtempo);
        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getActivity(), PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(true);
        pickerLayoutManager.setScaleDownBy(0.99f);
        pickerLayoutManager.setScaleDownDistance(0.95f);


        adapter = new PickerAdapter(getActivity(), populateTempos(viewModel.getMaxTempo()), recyclerViewTempo);
        recyclerViewTempo.setAdapter(adapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewTempo);
        recyclerViewTempo.setLayoutManager(pickerLayoutManager);
        pickerLayoutManager.scrollToPosition(viewModel.getTempo().getValue() - viewModel.getMinTempo());
        pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                viewModel.setTempo(Integer.parseInt(((TextView) view).getText().toString()));
            }
        });

    }

    private List<String> populateTempos(int count){
        List<String> data = new ArrayList<>();
        for(int i = viewModel.getMinTempo(); i < count; i++){
            data.add(String.valueOf(i));
        }
        return data;
    }
}