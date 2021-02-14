package com.example.keytronome.viewmodels;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.repositories.KeytronomeRepository;
import com.example.keytronome.tasks.MetronomeTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLSession;


/**
 * The view model keeps track of the live state of the application
 */
public class MainActivityViewModel extends AndroidViewModel {

    private KeytronomeRepository mRepo;
    private final HashMap<String, Integer> beatsPerMeasureMap = new HashMap<String, Integer>() {{
        put("4/4", 4);
        put("3/4", 3);
        put("2/4", 2);
        put("6/8", 6);
    }};

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (mRepo != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance();
    }

    public LiveData<Integer> getTempo() {
        return mRepo.getTempo();
    }

    public void setTempo(int bpm) {
        Log.d("DEBUG", "Setting tempo to " + bpm);
        mRepo.setTempo(bpm);
    }

    public LiveData<Boolean> getIsPlaying() {
        return mRepo.getIsPlaying();
    }

    public void setIsPlaying(boolean playing) {
        if (playing) {
            Log.d("VIEWMODEL", "SET IS PLAYING TRUE");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new MetronomeTask(this, this.getApplication()));
        }
        mRepo.setIsPlaying(playing);
    }

    public Integer getMinTempo() {
        return mRepo.getMinTempo();
    }

    public int getMaxTempo() {
        return mRepo.getMaxTempo();
    }

    public LiveData<String> getTimeSig() {
        return mRepo.getTimeSig();
    }

    public Integer getBeatsPerMeasure() {
        return beatsPerMeasureMap.get(mRepo.getTimeSig().getValue());
    }

    public void setTimeSig(String timeSig) {
        mRepo.setTimeSig(timeSig);
    }

    public LiveData<ArrayList<String>> getActiveKeysList() {
        return mRepo.getActiveKeysList();
    }

    public LiveData<String> getStartingKey() {
        return mRepo.getStartingKey();
    }

    public void setStartingKey(String key) {
        mRepo.setStartingKey(key);
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return mRepo.getNextKey();
    }

    public MutableLiveData<Integer> getCycles() {
        return mRepo.getCycles();
    }

    public int getMaxCycles() {
        return mRepo.getMaxCycles();
    }

    public void setCycles(int newCycles) {
        mRepo.setCycles(newCycles);
    }

    public ArrayList<String> getKeys() {
        return mRepo.getKeys();
    }

    public void goToNextKey() {
        mRepo.goToNextKey();
    }

    public LiveData<Pair<Integer, String>> getCurrentKey() {
        return mRepo.getCurrentKey();
    }

    public void setProgress(float progress) {
        mRepo.setProgress(progress);
    }

    public LiveData<Float> getProgress() {
        return mRepo.getProgress();
    }

    public int getMaxMpk() {
        return mRepo.getMaxMpk();
    }

    public void setMpk(int newMpk) {
        mRepo.setMpk(newMpk);
    }

    public LiveData<Integer> getMpk() {
        return mRepo.getMpk();
    }

    public LiveData<String> getOrder() {
        return mRepo.getOrder();
    }

    public ArrayList<String> getOrders() {
        return mRepo.getOrders();
    }

    public void setOrder(String newOrder) {
        mRepo.setOrder(newOrder);
    }

}