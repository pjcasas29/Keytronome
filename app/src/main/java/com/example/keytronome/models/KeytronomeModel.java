package com.example.keytronome.models;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.R;
import com.example.keytronome.tasks.MetronomeTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * POJO for values of the settings of the keytronome model.
 */
public class KeytronomeModel {
    //Default values
    private final Integer MINIMUM_TEMPO = 40;
    private final int MAX_TEMPO = 300;
    private final int DEFAULT_TEMPO = 120;
    private final String DEFAULT_TIMESIG = "4/4";
    private final String DEFAULT_KEY_ORDER = "chromatic";
    private final String DEFAULT_STARTING_KEY = "C";
    private String[] mKeys = {"C", "C#/Db", "D", "Eb", "E", "F", "F#/Gb", "G", "Ab", "A", "Bb", "B"};
    private final ArrayList<String> keys = new ArrayList<>(Arrays.asList(mKeys));

    public MutableLiveData<String> startingKey = new MutableLiveData<>();
    public MutableLiveData<String> keyOrder = new MutableLiveData<>();
    public MutableLiveData<Integer> tempo = new MutableLiveData<>();
    public MutableLiveData<String> timeSig = new MutableLiveData<>();

    public KeytronomeModel() {
        tempo.setValue(DEFAULT_TEMPO);
        timeSig.setValue(DEFAULT_TIMESIG);
        keyOrder.setValue(DEFAULT_KEY_ORDER);
        startingKey.setValue(DEFAULT_STARTING_KEY);
    }

    public void setTempo(int bpm) {
        if (bpm <= 300 && bpm >= 20) {
            tempo.setValue(bpm);
        } else {
            throw new IllegalArgumentException("VALUE OF TEMPO MUST BE BETWEEN 0 AND 300");
        }
    }

    public MutableLiveData<Integer> getBpm() {
        return this.tempo;
    }

    public Integer getMinTempo() {
        return MINIMUM_TEMPO;
    }

    public int getMaxTempo() {
        return MAX_TEMPO;
    }

    public MutableLiveData<String> getTimeSig() {
        return timeSig;
    }

    public void setTimeSig(String timeSig) {
        this.timeSig.setValue(timeSig);
    }

    public void setKeyOrder(String order) {
        this.keyOrder.setValue(order);
    }

    public MutableLiveData<String> getStartingKey(){
        return this.startingKey;
    }

    public void setStartingKey(String startingKey) {
        this.startingKey.setValue(startingKey);
    }

    public ArrayList<String> getKeysList(){
        ArrayList<String> result = new ArrayList<>();
        int startingIndex = keys.indexOf(startingKey.getValue());

        Log.d("DEBUG MODEL", "Key order:" + this.keyOrder.getValue() + " Starting Key:" + startingKey.getValue() + " StartingIndex: " + startingIndex);

        if(this.keyOrder.getValue().equals("chromatic")){
            for(int i = 0; i < keys.size(); i++){
                result.add(keys.get((startingIndex+ i ) % keys.size()));
            }
        }else if(this.keyOrder.getValue().equals("random")){
            for(int i = 0; i < keys.size(); i++){
                result.add(keys.get(new Random().nextInt(keys.size())));
            }
        }
        else{
            result.add("No Keys");
        }
        return result;
    }
}
