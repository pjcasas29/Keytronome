package com.example.keytronome.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.keytronome.models.KeytronomeModel;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<KeytronomeModel> mKeytronomeSettings;

    public LiveData<KeytronomeModel> getSettings(){
        return mKeytronomeSettings;
    }
}
