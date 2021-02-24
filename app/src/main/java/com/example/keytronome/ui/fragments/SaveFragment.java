package com.example.keytronome.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SaveFragment extends Fragment {

    MainActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        //Set onclick listeners
        ImageView backArrow = getActivity().findViewById(R.id.backArrowTs);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        // Create save dialog
        Button save = getActivity().findViewById(R.id.savePresetButton);
        EditText presetNameTv = new EditText(getActivity());
        presetNameTv.setHint(R.string.save_hint);
        save.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Save Preset");
            if(presetNameTv.getParent() != null) {
                ((ViewGroup)presetNameTv.getParent()).removeView(presetNameTv);
            }
            alertDialog.setView(presetNameTv);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                    (dialog, which) -> {
                        String presetNameInput = presetNameTv.getText().toString();
                        //Save and check If preset exists
                        viewModel.savePreset(presetNameInput).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        Toast.makeText(getActivity(), "Preset named " + presetNameInput + " Saved!", Toast.LENGTH_SHORT).show();
                                        presetNameTv.setText("");
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                        Toast.makeText(getActivity(), "Preset name already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                });;
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        Button load = getActivity().findViewById(R.id.loadButton);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment loadF = new LoadFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                        .replace(R.id.gridLayout, loadF)
                        .addToBackStack(null)
                        .commit();
            }
        });



    }
}