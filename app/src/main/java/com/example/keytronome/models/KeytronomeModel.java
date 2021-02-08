package com.example.keytronome.models;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * POJO for values of the settings of the keytronome model.
 */
public class KeytronomeModel {
    //Default values
    private final Integer MINIMUM_TEMPO = 40;
    private final int MAX_TEMPO = 300;
    private final int DEFAULT_TEMPO = 120;
    private final int MAX_CYCLES = 10;
    private final int DEFAULT_CYCLES = 1;
    private final String DEFAULT_TIMESIG = "4/4";
    private final String DEFAULT_KEY_ORDER = "chromatic";
    private final String DEFAULT_STARTING_KEY = "C";
    private String[] mKeys = {"C", "C#/Db", "D", "Eb", "E", "F", "F#/Gb", "G", "Ab", "A", "Bb", "B"};
    private final ArrayList<String> keys = new ArrayList<>(Arrays.asList(mKeys));

    public MutableLiveData<String> startingKey = new MutableLiveData<>();
    public MutableLiveData<String> keyOrder = new MutableLiveData<>();
    public MutableLiveData<Integer> tempo = new MutableLiveData<>();
    public MutableLiveData<String> timeSig = new MutableLiveData<>();
    public MutableLiveData<Pair<Integer, String>> currentKey = new MutableLiveData<>();
    public MutableLiveData<Pair<Integer, String>> nextKey = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> currentKeysList = new MutableLiveData<>();
    public MutableLiveData<Integer> cycles = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    public MutableLiveData<Float> progress= new MutableLiveData<>();

    public KeytronomeModel() {
        tempo.setValue(DEFAULT_TEMPO);
        timeSig.setValue(DEFAULT_TIMESIG);
        keyOrder.setValue(DEFAULT_KEY_ORDER);
        startingKey.setValue(DEFAULT_STARTING_KEY);
        cycles.setValue(DEFAULT_CYCLES);
        isPlaying.setValue(false);
        this.setActiveKeysList();
        resetKeyPositions();

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
        this.setActiveKeysList();
    }

    public MutableLiveData<String> getStartingKey(){
        return this.startingKey;
    }

    public void setStartingKey(String startingKey) {
        this.startingKey.setValue(startingKey);
        this.setActiveKeysList();
    }

    public LiveData<ArrayList<String>> getActiveKeysList(){
        return this.currentKeysList;
    }


    /**
     * Creates a list of keys to iterate over according to the keytronome parameters
     */
    public void setActiveKeysList(){
        ArrayList<String> result = new ArrayList<>();
        int startingIndex = keys.indexOf(startingKey.getValue());

        Log.d("DEBUG MODEL", "Key order:" + this.keyOrder.getValue() + " Starting Key:" + startingKey.getValue() + " StartingIndex: " + startingIndex);

        //This loop multiplies the list by the number of cycles
        for(int c = 0; c < this.cycles.getValue(); c++) {


            if (this.keyOrder.getValue().equals("chromatic")) {
                for (int i = 0; i < keys.size(); i++) {
                    result.add(keys.get((startingIndex + i) % keys.size()));
                }
            } else if (this.keyOrder.getValue().equals("random")) {
                for (int i = 0; i < keys.size(); i++) {
                    result.add(keys.get(new Random().nextInt(keys.size())));
                }
            } else {
                result.add("No Keys");
            }
        }
        this.currentKeysList.setValue(result);
        this.resetKeyPositions();
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return this.nextKey;
    }

    public LiveData<Pair<Integer, String>> getCurrentKey(){
        return this.currentKey;
    }

    public void setCycles(Integer newCycles) {
        this.cycles.setValue(newCycles);
    }

    public MutableLiveData<Integer> getCycles() {
        return this.cycles;
    }

    public int getMaxCycles() {
        return this.MAX_CYCLES;
    }

    public ArrayList<String> getKeys(){
        return keys;
    }

    private void resetKeyPositions(){
        this.currentKey.postValue(new Pair<Integer, String>(0, this.currentKeysList.getValue().get(0)));
        this.nextKey.postValue(new Pair<Integer, String>(1, this.currentKeysList.getValue().get(1)));
    }

    public void setIsPlaying(boolean changeIsPlaying) {
        if(!changeIsPlaying){
            resetKeyPositions();
            setProgress(0);
        }else//Set to true TODO: WHATEVER NEEDS TO GO HERE
        {

        }
        this.isPlaying.postValue(changeIsPlaying);
    }

    public LiveData<Boolean> isPlaying(){
        return this.isPlaying;
    }

    public void goToNextKey() {
        this.nextKey.postValue(this.currentKey.getValue());
        int nextIndex = (getCurrentKey().getValue().first + 1) % this.getActiveKeysList().getValue().size();
        this.currentKey.postValue(new Pair<Integer, String>(nextIndex, this.currentKeysList.getValue().get(nextIndex)));
    }


    public void setProgress(float progress) {
        this.progress.postValue(progress);
    }

    public LiveData<Float> getProgress() {
        return this.progress;
    }
}
