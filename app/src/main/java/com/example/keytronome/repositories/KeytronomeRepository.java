package com.example.keytronome.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.models.KeytronomeModel;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class KeytronomeRepository {

    private static KeytronomeRepository instance;
    private KeytronomeModel model = new KeytronomeModel();

    public static KeytronomeRepository getInstance(){
        if(instance == null){
            instance = new KeytronomeRepository();
        }
        return instance;
    }


    public MutableLiveData<KeytronomeModel> getDefaultSettings(){
        setDefaultSettings();
        MutableLiveData<KeytronomeModel> mSettings = new MutableLiveData<>();
        mSettings.setValue(model);
        return mSettings;
    }

    private void setDefaultSettings(){
        model = new KeytronomeModel();
    }
}
