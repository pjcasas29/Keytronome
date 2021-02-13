package com.example.keytronome.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import java.util.ArrayList;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    PickerAdapter adapter;
    PickerAdapter adapterOrders;
    MainActivityViewModel viewModel;

    private View chromaticButton;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backArrow = getActivity().findViewById(R.id.backArrowOrder);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //Order buttons

        View chromaticButton = getActivity().findViewById(R.id.chromaticButton);

        View fifthsButton = getActivity().findViewById(R.id.fifthsButton);

        View thirdsButton = getActivity().findViewById(R.id.thirdsButton);



        //Set the scrollable starting keys
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