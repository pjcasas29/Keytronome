package com.example.keytronome.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.models.KeytronomeModel;

import java.util.ArrayList;

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
        return model.getBpm();
    }

    public void setTempo(int bpm) {
        model.setTempo(bpm);
    }

    public Integer getMinTempo() {
        return model.getMinTempo();
    }

    public int getMaxTempo() {
        return model.getMaxTempo();
    }

    public MutableLiveData<String> getTimeSig() {
        return model.getTimeSig();
    }

    public void setTimeSig(String timeSig) {
        model.setTimeSig(timeSig);
    }

    public ArrayList<String> getKeysList(){
        return model.getKeysList();
    }

    public LiveData<String> getStartingKey() {
        return model.getStartingKey();
    }

    public void setStartingKey(String key) {
        model.setStartingKey(key);
    }
}
