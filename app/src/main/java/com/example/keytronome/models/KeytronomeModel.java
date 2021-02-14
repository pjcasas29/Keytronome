package com.example.keytronome.models;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * POJO for values of the settings of the keytronome model.
 */
public class KeytronomeModel {
    //Default values
    private static final String CHROMATIC = "Chromatic";
    private static final String FOURTHS = "Fourths";
    private static final String FIFTHS = "Fifths";
    private static final String RANDOM = "Random";
    private static final String THIRDS = "Thirds";
    private static final String WHOLE_STEPS = "Whole Steps";
    private static final String DEFAULT_KEY_ORDER = CHROMATIC;
    private static final String[] ORDERS = {CHROMATIC, FOURTHS, FIFTHS, RANDOM, THIRDS, WHOLE_STEPS};
    private static final Integer MINIMUM_TEMPO = 40;
    private static final int MAX_TEMPO = 300;
    private static final int DEFAULT_TEMPO = 120;
    private static final int MAX_CYCLES = 10;
    private static final int DEFAULT_CYCLES = 1;
    private static final String DEFAULT_TIMESIG = "4/4";
    private static final String[] mKeys = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    private static final ArrayList<String> keys = new ArrayList<>(Arrays.asList(mKeys));
    private static final String DEFAULT_STARTING_KEY = keys.get(0);
    private static final int MAX_MPK = 6;
    private static final int DEFAULT_MPK = 2;

    //State variables
    public MutableLiveData<Boolean> ascending = new MutableLiveData<>();
    public MutableLiveData<String> startingKey = new MutableLiveData<>();
    public MutableLiveData<String> keyOrder = new MutableLiveData<>();
    public MutableLiveData<Integer> tempo = new MutableLiveData<>();
    public MutableLiveData<String> timeSig = new MutableLiveData<>();
    public MutableLiveData<Pair<Integer, String>> currentKey = new MutableLiveData<>();
    public MutableLiveData<Pair<Integer, String>> nextKey = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> currentKeysList = new MutableLiveData<>();
    public MutableLiveData<Integer> cycles = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    public MutableLiveData<Float> progress = new MutableLiveData<>();
    public MutableLiveData<Integer> mpk = new MutableLiveData<>();
    public MutableLiveData<List<String>> previewList = new MutableLiveData<>();
    public MutableLiveData<Boolean> isCueing = new MutableLiveData<>();

    //Initialize default values
    public KeytronomeModel() {
        tempo.setValue(DEFAULT_TEMPO);
        timeSig.setValue(DEFAULT_TIMESIG);
        keyOrder.setValue(DEFAULT_KEY_ORDER);
        startingKey.setValue(DEFAULT_STARTING_KEY);
        cycles.setValue(DEFAULT_CYCLES);
        isPlaying.setValue(false);
        isCueing.setValue(false);
        mpk.setValue(DEFAULT_MPK);
        ascending.setValue(true);
        this.setActiveKeysList();
        this.setPreviewList();
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

    public MutableLiveData<String> getStartingKey() {
        return this.startingKey;
    }

    public void setStartingKey(String startingKey) {
        this.startingKey.setValue(startingKey);
        this.setActiveKeysList();
        this.setPreviewList();
    }

    public LiveData<ArrayList<String>> getActiveKeysList() {
        return this.currentKeysList;
    }


    /**
     * Creates a list of keys to iterate over according to the keytronome parameters
     */
    public void setActiveKeysList() {
        ArrayList<String> result = new ArrayList<>();
        int startingIndex = keys.indexOf(startingKey.getValue());

        Log.d("DEBUG MODEL", "Key order:" + this.keyOrder.getValue() + " Starting Key:" + startingKey.getValue() + " StartingIndex: " + startingIndex);

        //This loop multiplies the list by the number of cycles
        for (int c = 0; c < this.cycles.getValue(); c++) {
            switch (this.keyOrder.getValue()) {
                case CHROMATIC:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get((startingIndex + i) % keys.size()));
                    }
                    break;
                case RANDOM:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get(new Random().nextInt(keys.size())));
                    }
                    break;
                case FOURTHS:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get(((startingIndex + (i * 5)) % keys.size())));
                    }
                    break;
                case THIRDS:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get(((startingIndex + (i * 4)) % keys.size())));
                    }
                    break;
                case FIFTHS:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get(((startingIndex + (i * 7)) % keys.size())));
                    }
                    break;
                case WHOLE_STEPS:
                    for (int i = 0; i < keys.size(); i++) {
                        result.add(keys.get(((startingIndex + (i * 2)) % keys.size())));
                    }
                    break;
                default:
                    result.add("No Keys");

            }
        }
        this.currentKeysList.setValue(result);
        this.resetKeyPositions();
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return this.nextKey;
    }

    public LiveData<Pair<Integer, String>> getCurrentKey() {
        return this.currentKey;
    }

    public void setCycles(Integer newCycles) {
        this.cycles.setValue(newCycles);
        this.setActiveKeysList();
    }

    public MutableLiveData<Integer> getCycles() {
        return this.cycles;
    }

    public int getMaxCycles() {
        return MAX_CYCLES;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    private void resetKeyPositions() {
        this.currentKey.postValue(new Pair<>(0, this.currentKeysList.getValue().get(0)));
        this.nextKey.postValue(new Pair<>(1, this.currentKeysList.getValue().get(1)));
    }

    public void setIsPlaying(boolean changeIsPlaying) {
        Log.d("MODEL", "SET IS PLAYING TO " + changeIsPlaying);

        if (!changeIsPlaying) { // Reset key positions and progress when app has stopped playing
            resetKeyPositions();
            setProgress(0);
        }

        this.isPlaying.postValue(changeIsPlaying);
    }

    public LiveData<Boolean> isPlaying() {
        return this.isPlaying;
    }

    public void goToNextKey() {
        Log.d("MODEL", "GO TO NEXT KEY");

        int nextCurrentIndex = (Objects.requireNonNull(getCurrentKey().getValue()).first + 1) % this.getActiveKeysList().getValue().size();
        this.currentKey.postValue(new Pair<Integer, String>(nextCurrentIndex, this.currentKeysList.getValue().get(nextCurrentIndex)));
        int nextNextIndex = (Objects.requireNonNull(getNextKey().getValue()).first + 1) % this.getActiveKeysList().getValue().size();
        this.nextKey.postValue(new Pair<Integer, String>(nextNextIndex, this.currentKeysList.getValue().get(nextNextIndex)));
    }


    public void setProgress(float progress) {
        this.progress.postValue(progress);
    }

    public LiveData<Float> getProgress() {
        return this.progress;
    }

    public int getMaxMpk() {
        return MAX_MPK;
    }

    public void setMpk(int newMpk) {
        mpk.setValue(newMpk);
    }

    public LiveData<Integer> getMpk() {
        return this.mpk;
    }

    public LiveData<String> getOrder() {
        return keyOrder;
    }

    public ArrayList<String> getOrders() {
        return new ArrayList<>(Arrays.asList(ORDERS));
    }

    public void setOrder(String newOrder) {
        keyOrder.setValue(newOrder);
        setActiveKeysList();
        setPreviewList();
    }

    private void setPreviewList() {
        previewList.setValue(Objects.requireNonNull(getActiveKeysList().getValue()).subList(0, 12));
    }

    public LiveData<List<String>> getPreviewList() {
        return previewList;
    }

    public LiveData<Boolean> isCueing() {
        return this.isCueing;
    }

    public void setIsCueing(boolean cueing) {
        this.isCueing.postValue(cueing);
    }
}
