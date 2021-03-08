package com.example.keytronome.ui.fragments;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TempoScrollerFragment extends Fragment {

    PickerAdapter adapter;
    MainActivityViewModel viewModel;

    public TempoScrollerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tempo_scroller, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backArrow = getActivity().findViewById(R.id.backArrowTempo);
        backArrow.setOnClickListener(view1 -> getFragmentManager().popBackStack());

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