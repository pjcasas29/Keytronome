package com.example.keytronome.repositories;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.db.Keytronome;
import com.example.keytronome.db.KeytronomeDao;
import com.example.keytronome.db.KeytronomeRoomDatabase;
import com.example.keytronome.models.KeytronomeModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KeytronomeRepository {

    private static KeytronomeRepository instance;
    private final KeytronomeModel model = new KeytronomeModel();
    private LiveData<List<Keytronome>> mAllPresets;

    private KeytronomeDao keytronomeDao;
    private KeytronomeRoomDatabase keytronomeDB;

    public static KeytronomeRepository getInstance(Application context){
        if(instance == null){
            instance = new KeytronomeRepository(context);
        }
        return instance;
    }

    public KeytronomeRepository(Application context){
         keytronomeDB = KeytronomeRoomDatabase.getDatabase(context);
         keytronomeDao = keytronomeDB.keytronomeDao();
    }

    public LiveData<List<Keytronome>> getPresets(){
        mAllPresets = keytronomeDao.getAllPresets();
        return mAllPresets;
    }

    public Completable savePreset(String name){
        model.setName(name);
        Keytronome entity = model.toEntity();
        return Completable.fromCallable(()-> keytronomeDB.keytronomeDao().insert(entity));
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

    public void setIsPlaying(boolean isPlaying) {
        Log.d("REPO", "SET IS PLAYING TO " + isPlaying);
        model.setIsPlaying(isPlaying);
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

    public void setProgress(float progress) {
        model.setProgress(progress);
    }

    public LiveData<Float> getProgress() {
        return model.getProgress();
    }

    public int getMaxMpk() {
        return model.getMaxMpk();
    }

    public void setMpk(int newMpk) {
        model.setMpk(newMpk);
    }

    public LiveData<Integer> getMpk() {
        return model.getMpk();
    }

    public LiveData<String> getOrder() {
        return model.getOrder();
    }

    public ArrayList<String> getOrders() {
        return model.getOrders();
    }

    public void setOrder(String newOrder) {
        model.setOrder(newOrder);
    }

    public LiveData<List<String>> getPreviewList() {
        return model.getPreviewList();
    }

    public LiveData<Boolean> getIsCueing() {
        return model.isCueing();
    }

    public void setIsCueing(boolean cueing) {

        model.setIsCueing(cueing);
    }
}
