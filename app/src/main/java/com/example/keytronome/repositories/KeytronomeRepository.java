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

    public MutableLiveData<Integer> getTempo(){
        MutableLiveData<Integer> tempo = new MutableLiveData<>();
        tempo.postValue(model.getBpm());
        return tempo;
    }

    public void setTempo(int bpm) {
        model.setTempo(bpm);
    }
}
