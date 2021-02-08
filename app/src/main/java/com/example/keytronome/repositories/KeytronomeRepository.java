package com.example.keytronome.repositories;

import android.util.Pair;

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

    public LiveData<ArrayList<String>> getActiveKeysList(){
        return model.getActiveKeysList();
    }

    public LiveData<String> getStartingKey() {
        return model.getStartingKey();
    }

    public void setStartingKey(String key) {
        model.setStartingKey(key);
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return model.getNextKey();
    }

    public MutableLiveData<Integer> getCycles() {
        return model.getCycles();
    }

    public int getMaxCycles() {
        return model.getMaxCycles();
    }

    public void setCycles(int newCycles) {
        model.setCycles(newCycles);
    }

    public ArrayList<String> getKeys() {
        return model.getKeys();
    }

    public void setIsPlaying(boolean isplaying) {
        model.setIsPlaying(isplaying);
    }

    public void goToNextKey() {
        model.goToNextKey();
    }

    public LiveData<Pair<Integer, String>> getCurrentKey() {
        return model.getCurrentKey();
    }

    public LiveData<Boolean> getIsPlaying() {
        return model.isPlaying();
    }
}
