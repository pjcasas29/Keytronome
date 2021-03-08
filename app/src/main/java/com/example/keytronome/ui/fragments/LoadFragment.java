package com.example.keytronome.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.keytronome.R;
import com.example.keytronome.db.Keytronome;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoadFragment extends Fragment {

    private PresetListAdapter presetListAdapter;


    public LoadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivityViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //Set onclick listeners
        ImageView backArrow = getActivity().findViewById(R.id.backArrowTs);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        RecyclerView presetsRecyclerView = getActivity().findViewById(R.id.presetRv);
        presetListAdapter = new PresetListAdapter(getActivity(), new ListItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                //Save
            }
        });
        presetsRecyclerView.setAdapter(presetListAdapter);
        presetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getPresets().observe(getActivity(), new Observer<List<Keytronome>>() {
            @Override
            public void onChanged(List<Keytronome> presets) {
                presetListAdapter.setPresets(presets);
            }
        });
    }
}