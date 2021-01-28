package com.example.keytronome.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.repositories.KeytronomeRepository;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<KeytronomeModel> mKeytronomeSettings;
    private KeytronomeRepository mRepo;

    public void init() {
        if (mKeytronomeSettings != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance();
        mKeytronomeSettings = mRepo.getDefaultSettings();

    }

    public LiveData<KeytronomeModel> getKnmSettings() {
        return mKeytronomeSettings;
    }

}